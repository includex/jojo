#!/usr/bin/env python3
"""Compare original/port full-battle traces without comparing wall-clock time.

The two engines need not make the same tactical decisions when their random
streams have diverged.  This verifier consequently has two deliberately
separate outputs:

* ``decisionDivergences`` lists action/move episodes that cannot be paired;
* ``orderMismatches`` compares callback milestones only inside an episode
  whose round, camp, actor, action and observed target are common.

Frames are treated as ordered observation buckets.  Events in the same frame
are unordered, which avoids inventing an order that neither recorder observes.
No timestamp, frame count, animation duration or time-scale value participates
in equality.
"""
from __future__ import annotations

import argparse
import json
import re
import sys
from collections import defaultdict
from dataclasses import dataclass
from difflib import SequenceMatcher
from pathlib import Path
from typing import Any, Iterable

FORMAT = "jojo-yingchuan-full-battle-trace/v1"
MOVE_ACTION = 20
ATTACK_ACTIONS = frozenset({21, 25, 48, 49})
REACTION_ACTIONS = frozenset({2, 3, 26, 27, 32})
DEATH_ACTIONS = frozenset({23, 24, 28, 47})
EPISODE_ACTIONS = ATTACK_ACTIONS | DEATH_ACTIONS | {MOVE_ACTION}


@dataclass(frozen=True)
class UnitView:
    character: int
    camp: int
    tile: tuple[int, int]
    hp: int
    mp: int
    direction: int
    action: int
    visible: bool
    exists: bool
    visual: tuple[float, float] | None
    statuses: tuple[int, ...] | None
    status_rounds: tuple[int, ...] | None


@dataclass
class Episode:
    round: int
    camp: int | None
    actor: int
    action: int
    start: int
    end: int
    target: int | None
    buckets: list[tuple[str, ...]]
    movement: dict[str, Any] | None
    camera: dict[str, Any] | None

    @property
    def decision(self) -> tuple[Any, ...]:
        if self.action == MOVE_ACTION:
            # A different destination is a tactical divergence, not a callback
            # ordering defect inside an otherwise common move.
            movement = self.movement or {}
            return self.round, self.camp, self.actor, self.action, tuple(movement.get("from") or ()), tuple(movement.get("to") or ())
        # Hit/miss/guard/death are adjudication outcomes. If they differ (for
        # example because RNG streams diverged), comparing their callback
        # topology would falsely label a tactical result as renderer ordering.
        outcome: set[str] = set()
        for marker in (item for bucket in self.buckets for item in bucket):
            if marker.startswith("camera:"):
                continue
            parts = marker.split(":")
            if len(parts) >= 2 and parts[0].lstrip("-").isdigit():
                kind = parts[1]
                if kind == "reaction":
                    outcome.add(":".join(parts[1:3]))
                elif kind in {"hp", "hp-zero", "death", "hidden"}:
                    outcome.add(kind + (f":{parts[2]}" if kind in {"hp", "death"} and len(parts) > 2 else ""))
        return self.round, self.camp, self.actor, self.action, self.target, tuple(sorted(outcome))

    def summary(self) -> dict[str, Any]:
        return {
            "round": self.round,
            "camp": self.camp,
            "actor": self.actor,
            "action": self.action,
            "target": self.target,
            "frameRange": [self.start, self.end],
            "callbackBuckets": [list(bucket) for bucket in self.buckets],
            "movement": self.movement,
            "camera": self.camera,
        }


def _number_pair(value: Any) -> tuple[float, float] | None:
    if not isinstance(value, list) or len(value) < 2:
        return None
    if not all(isinstance(part, (int, float)) for part in value[:2]):
        return None
    return float(value[0]), float(value[1])


def _integer_sequence(value: Any) -> tuple[int, ...] | None:
    if not isinstance(value, list) or not value or not all(isinstance(part, int) for part in value):
        return None
    return tuple(value)


def unit_view(row: list[Any]) -> UnitView | None:
    if not isinstance(row, list) or len(row) < 17 or not isinstance(row[1], int):
        return None
    metadata = row[17] if len(row) > 17 and isinstance(row[17], dict) else {}
    # Port stores the rendered action in slot 8. The original v1 recorder used
    # unit.unit().action() there (an action-point value), but its playing clip
    # name in slot 14 is authoritative whenever present.
    action = row[8]
    playing = row[14] if len(row) > 14 else None
    if isinstance(playing, str):
        match = re.search(r"(?:anime|action)[_-]?(\d+)", playing, re.IGNORECASE)
        if match:
            action = int(match.group(1))
    return UnitView(
        character=row[1], camp=row[2], tile=(row[3], row[4]), hp=row[5], mp=row[6],
        direction=row[7], action=action, visible=bool(row[9]), exists=bool(row[10]),
        visual=_number_pair(metadata.get("visual")),
        statuses=_integer_sequence(metadata.get("statuses")),
        status_rounds=_integer_sequence(metadata.get("statusRounds")),
    )


def frame_units(frame: dict[str, Any]) -> dict[int, UnitView]:
    result: dict[int, UnitView] = {}
    for row in frame.get("units", []):
        view = unit_view(row)
        if view is not None:
            result[view.character] = view
    return result


def validate_trace(trace: dict[str, Any], label: str) -> list[str]:
    errors: list[str] = []
    if trace.get("format") != FORMAT:
        errors.append(f"{label}: unexpected format {trace.get('format')!r}")
    frames = trace.get("frames")
    if not isinstance(frames, list) or not frames:
        errors.append(f"{label}: frames must be a non-empty array")
        return errors
    previous_frame_number: int | float | None = None
    for index, frame in enumerate(frames):
        if not isinstance(frame, dict):
            errors.append(f"{label}: frame[{index}] is not an object")
            continue
        if not isinstance(frame.get("units"), list):
            errors.append(f"{label}: frame[{index}].units is not an array")
        if "round" not in frame or "camp" not in frame:
            errors.append(f"{label}: frame[{index}] lacks round/camp")
        frame_number = frame.get("f")
        if not isinstance(frame_number, (int, float)):
            errors.append(f"{label}: frame[{index}].f is not numeric")
        elif previous_frame_number is not None and frame_number < previous_frame_number:
            errors.append(f"{label}: frame[{index}].f moves backwards")
        else:
            # Equal values are intentional: the recorder emits micro-samples
            # around synchronous mutations within one renderer frame.
            previous_frame_number = frame_number
    return errors


def capabilities(trace: dict[str, Any]) -> dict[str, bool]:
    frames = trace.get("frames", [])
    action_transitions = tile_transitions = health_transitions = visibility_transitions = False
    previous: dict[int, UnitView] = {}
    for frame in frames:
        current = frame_units(frame)
        for character in previous.keys() & current.keys():
            before, after = previous[character], current[character]
            action_transitions |= before.action != after.action
            tile_transitions |= before.tile != after.tile
            health_transitions |= before.hp != after.hp or before.mp != after.mp
            # Slot 10 is source BattleUnit.isExist() (often false immediately
            # at HP zero), while port v1 writes a roster-presence constant.
            # Only visible/removal is a shared hide callback observation.
            visibility_transitions |= before.visible != after.visible
        visibility_transitions |= bool(previous.keys() - current.keys())
        previous = current
    dialogue_identities = {
        (frame.get("dialogueRevision"), frame.get("dialogueIdentity"))
        for frame in frames if frame.get("dialogue")
    }
    dialogue_identities_with_content = {
        (frame.get("dialogueRevision"), frame.get("dialogueIdentity"))
        for frame in frames
        if frame.get("dialogue") and str(frame.get("dialogueSpeakerId", "")) and
        str(frame.get("dialogueText", ""))
    }
    return {
        "camera": bool(frames) and all(_number_pair(frame.get("camera")) is not None for frame in frames),
        "dialogue": bool(frames) and all("dialogue" in frame for frame in frames),
        # A boolean sampled once per frame cannot observe close+open in one
        # engine tick.  Sequence parity requires the explicit identity edge.
        "dialogueIdentity": bool(frames) and all(
            "dialogueRevision" in frame and "dialogueIdentity" in frame and
            (not frame.get("dialogue") or (
                isinstance(frame.get("dialogueRevision"), (int, float)) and
                frame.get("dialogueRevision", 0) > 0 and
                bool(frame.get("dialogueIdentity"))
            ))
            for frame in frames
        ),
        "dialogueContent": bool(frames) and all(
            "dialogueSpeakerId" in frame and "dialogueText" in frame for frame in frames
        ) and dialogue_identities.issubset(dialogue_identities_with_content),
        "script": any(frame.get("script") is not None for frame in frames),
        "aiPresentation": any(frame.get("aiPresentation") is not None for frame in frames),
        "explicitCallbacks": any(frame.get("callbacks") for frame in frames) or
            any("callback" in str(event.get("kind", "")).lower() for event in trace.get("events", []) if isinstance(event, dict)),
        "visualPosition": any(view.visual is not None for frame in frames for view in frame_units(frame).values()),
        "statusState": any(
            view.statuses is not None and view.status_rounds is not None
            for frame in frames for view in frame_units(frame).values()
        ),
        "actionTransitions": action_transitions,
        "authoredActionEpisodes": any(
            view.action in EPISODE_ACTIONS for frame in frames for view in frame_units(frame).values()
        ),
        "tileTransitions": tile_transitions,
        "healthTransitions": health_transitions,
        "visibilityTransitions": visibility_transitions,
    }


def transition_buckets(frames: list[dict[str, Any]]) -> list[tuple[str, ...]]:
    """Return frame-indexed milestone buckets derived from shared fields."""
    buckets: list[tuple[str, ...]] = [tuple() for _ in frames]
    previous_units: dict[int, UnitView] = {}
    previous_camp: tuple[Any, Any] | None = None
    previous_dialogue = False
    for index, frame in enumerate(frames):
        current = frame_units(frame)
        events: list[str] = []
        camp = (frame.get("round"), frame.get("camp"))
        if camp != previous_camp:
            events.append(f"camp:{camp[0]}:{camp[1]}")
        dialogue = bool(frame.get("dialogue"))
        if dialogue != previous_dialogue:
            events.append("dialogue:open" if dialogue else "dialogue:close")
        for character in sorted(previous_units.keys() | current.keys()):
            before, after = previous_units.get(character), current.get(character)
            if before is None and after is not None:
                events.append(f"unit:{character}:shown")
                continue
            if before is not None and after is None:
                events.append(f"unit:{character}:hidden")
                continue
            assert before is not None and after is not None
            if before.action != after.action:
                events.append(f"unit:{character}:action:{before.action}>{after.action}")
                if after.action in REACTION_ACTIONS:
                    events.append(f"unit:{character}:reaction:{after.action}")
                if after.action in DEATH_ACTIONS:
                    events.append(f"unit:{character}:death:{after.action}")
            if before.tile != after.tile:
                events.append(f"unit:{character}:tile:{before.tile[0]},{before.tile[1]}>{after.tile[0]},{after.tile[1]}")
            if before.hp != after.hp:
                direction = "damage" if after.hp < before.hp else "heal"
                events.append(f"unit:{character}:hp:{direction}:{before.hp}>{after.hp}")
                if before.hp > 0 and after.hp <= 0:
                    events.append(f"unit:{character}:hp-zero")
            if before.mp != after.mp:
                direction = "spend" if after.mp < before.mp else "gain"
                events.append(f"unit:{character}:mp:{direction}:{before.mp}>{after.mp}")
            if before.statuses is not None and after.statuses is not None and before.statuses != after.statuses:
                for status, (old, new) in enumerate(zip(before.statuses, after.statuses)):
                    if old != new:
                        events.append(f"unit:{character}:status:{status}:{old}>{new}")
            if (before.status_rounds is not None and after.status_rounds is not None and
                    before.status_rounds != after.status_rounds):
                for status, (old, new) in enumerate(zip(before.status_rounds, after.status_rounds)):
                    if old != new:
                        events.append(f"unit:{character}:status-round:{status}:{old}>{new}")
            if before.visible and not after.visible:
                events.append(f"unit:{character}:hidden")
        callbacks = frame.get("callbacks")
        if isinstance(callbacks, list):
            events.extend(f"callback:{json.dumps(item, ensure_ascii=False, sort_keys=True)}" for item in callbacks)
        buckets[index] = tuple(sorted(set(events)))
        previous_units = current
        previous_camp = camp
        previous_dialogue = dialogue
    return buckets


def _episode_target(actor: int, action: int, buckets: Iterable[tuple[str, ...]]) -> int | None:
    # unitDeath has no target. A still-running reaction belonging to the
    # preceding attack may overlap the first death frame, but assigning that
    # unit as the death target corrupts episode matching.
    if action in DEATH_ACTIONS:
        return None
    for bucket in buckets:
        candidates: list[int] = []
        for event in bucket:
            if not event.startswith("unit:"):
                continue
            parts = event.split(":")
            if len(parts) < 3 or not parts[1].lstrip("-").isdigit():
                continue
            character = int(parts[1])
            if character != actor and parts[2] in {"reaction", "hp", "hp-zero", "death", "hidden"}:
                candidates.append(character)
        if candidates:
            return min(candidates)
    return None


def _callback_projection(actor: int, action: int, buckets: list[tuple[str, ...]], include_camera: list[str | None]) -> list[tuple[str, ...]]:
    projected: list[tuple[str, ...]] = []
    camera_was_moving = False
    for offset, bucket in enumerate(buckets):
        markers: list[str] = []
        for event in bucket:
            if event.startswith("unit:"):
                parts = event.split(":")
                character = int(parts[1])
                kind = parts[2]
                if action in DEATH_ACTIONS and character != actor:
                    continue
                if kind in {"reaction", "death", "hidden", "hp-zero"}:
                    markers.append(f"{character}:{kind}" + (f":{parts[3]}" if len(parts) > 3 and kind in {"reaction", "death"} else ""))
                elif kind == "hp":
                    markers.append(f"{character}:hp:{parts[3]}")
                elif kind in {"status", "status-round"}:
                    markers.append(f"{character}:{kind}:{parts[3]}:{parts[4]}")
                elif kind == "tile" and character == actor:
                    markers.append(f"{character}:tile")
        camera_marker = include_camera[offset] if offset < len(include_camera) else None
        if camera_marker == "moving" and not camera_was_moving:
            markers.append("camera:move-begin")
            camera_was_moving = True
        elif camera_marker == "settled" and camera_was_moving:
            markers.append("camera:settle")
            camera_was_moving = False
        if markers:
            projected.append(tuple(sorted(set(markers))))
    return projected


def camera_states(frames: list[dict[str, Any]]) -> list[str | None]:
    result: list[str | None] = [None for _ in frames]
    points = [_number_pair(frame.get("camera")) for frame in frames]
    if not frames or any(point is None for point in points):
        return result
    moving = False
    for index in range(1, len(points)):
        changed = points[index] != points[index - 1]
        if changed:
            result[index] = "moving"
            moving = True
        elif moving:
            result[index] = "settled"
            moving = False
    if moving:
        result[-1] = "settled"
    return result


def camera_summary(frames: list[dict[str, Any]]) -> dict[str, Any] | None:
    points = [_number_pair(frame.get("camera")) for frame in frames]
    if not points or any(point is None for point in points):
        return None
    rounded = [tuple(round(value, 3) for value in point) for point in points if point is not None]
    return {"from": list(rounded[0]), "to": list(rounded[-1]), "changed": len(set(rounded)) > 1}


def canonical_episode_frames(frames: list[dict[str, Any]]) -> list[tuple[int, dict[str, Any]]]:
    """Select action-authoritative rows while retaining true transient clips.

    A source ``setAction2`` hook runs before some completion callbacks commit
    their durable state (notably ``move2`` commits x/y just after switching to
    idle).  If that action observation agrees with the next ordinary rendered
    frame, the ordinary row is authoritative and the hook row is redundant.
    An observation that *disagrees* with the next ordinary row represents an
    action that began and ended wholly between rendered frames, so it must be
    retained.  Camp/dialogue micro-rows remain in the raw trace consumers but
    do not participate in action episode boundaries.
    """
    next_regular: list[int | None] = [None] * len(frames)
    following: int | None = None
    for index in range(len(frames) - 1, -1, -1):
        next_regular[index] = following
        if not frames[index].get("observation"):
            following = index

    result: list[tuple[int, dict[str, Any]]] = []
    for index, frame in enumerate(frames):
        observation = frame.get("observation")
        if not observation:
            result.append((index, frame))
            continue
        match = re.fullmatch(r"transition:action:(-?\d+)", str(observation))
        if not match:
            continue
        actor = int(match.group(1))
        future_index = next_regular[index]
        current = frame_units(frame).get(actor)
        # move2 may switch MOVE -> idle, commit x/y, then immediately start
        # its next presentation action, all at one `f`.  The idle hook is an
        # implementation midpoint rather than the episode boundary.  A later
        # hook for this actor in the same regular-frame gap supersedes that
        # non-episode observation; the eventual regular row will close MOVE
        # with the committed tile included.
        superseded_non_episode = False
        if current is not None and current.action not in EPISODE_ACTIONS:
            stop = future_index if future_index is not None else len(frames)
            for later in frames[index + 1:stop]:
                later_match = re.fullmatch(r"transition:action:(-?\d+)", str(later.get("observation", "")))
                if later_match and int(later_match.group(1)) == actor:
                    superseded_non_episode = True
                    break
        if superseded_non_episode:
            continue
        future = frame_units(frames[future_index]).get(actor) if future_index is not None else None
        if current is not None and future is not None and current.action == future.action:
            continue
        result.append((index, frame))
    return result


def foreign_camera_transition_at_boundary(
        actor: int, raw_frames: list[dict[str, Any]], previous_raw: int, terminal_raw: int) -> bool:
    """Whether another actor took camera ownership before our terminal sample."""
    previous_camera = _number_pair(raw_frames[previous_raw].get("camera"))
    for frame in raw_frames[previous_raw + 1:terminal_raw + 1]:
        camera = _number_pair(frame.get("camera"))
        if camera != previous_camera:
            match = re.fullmatch(r"transition:action:(-?\d+)", str(frame.get("observation", "")))
            if match and int(match.group(1)) != actor:
                return True
        previous_camera = camera
    return False


def extract_episodes(trace: dict[str, Any]) -> list[Episode]:
    raw_frames = trace["frames"]
    indexed_frames = canonical_episode_frames(raw_frames)
    frames = [frame for _, frame in indexed_frames]
    buckets = transition_buckets(frames)
    result: list[Episode] = []
    active: dict[int, tuple[int, int]] = {}
    previous: dict[int, UnitView] = {}
    for index, frame in enumerate(frames):
        current = frame_units(frame)
        for actor in sorted(previous.keys() | current.keys()):
            before, after = previous.get(actor), current.get(actor)
            before_action = before.action if before else None
            after_action = after.action if after else None
            if actor in active and (after is None or after_action != active[actor][0]):
                action, start = active.pop(actor)
                end = index
                local = buckets[start:end + 1]
                # Camera setup immediately before an action belongs to the
                # boundary between episodes, not to the new action's callback
                # order. Recompute states inside this episode so its first
                # row is not labelled `move-begin` merely because it differs
                # from the preceding actor's terminal row.
                local_camera_states = camera_states(frames[start:end + 1])
                camera_frames = frames[start:end + 1]
                if end > start and foreign_camera_transition_at_boundary(
                        actor, raw_frames, indexed_frames[end - 1][0], indexed_frames[end][0]):
                    # The terminal row remains necessary for the actor's
                    # action/tile callback, but its camera already belongs to
                    # the next actor. Do not attach that focus edge or endpoint
                    # to the episode being closed.
                    local_camera_states[-1] = None
                    camera_frames = camera_frames[:-1]
                movement = movement_summary(actor, frames[start:end + 1]) if action == MOVE_ACTION else None
                result.append(Episode(
                    round=frames[start].get("round"), camp=frames[start].get("camp"), actor=actor,
                    action=action, start=indexed_frames[start][0], end=indexed_frames[end][0],
                    target=_episode_target(actor, action, local),
                    buckets=_callback_projection(actor, action, local, local_camera_states), movement=movement,
                    camera=camera_summary(camera_frames),
                ))
            if after is not None and after_action in EPISODE_ACTIONS and before_action != after_action:
                active[actor] = (after_action, index)
        previous = current
    for actor, (action, start) in active.items():
        local = buckets[start:]
        result.append(Episode(
            round=frames[start].get("round"), camp=frames[start].get("camp"), actor=actor,
            action=action, start=indexed_frames[start][0], end=indexed_frames[-1][0],
            target=_episode_target(actor, action, local),
            buckets=_callback_projection(actor, action, local, camera_states(frames[start:])),
            movement=movement_summary(actor, frames[start:]) if action == MOVE_ACTION else None,
            camera=camera_summary(frames[start:]),
        ))
    return sorted(result, key=lambda episode: (episode.start, episode.actor))


def movement_summary(actor: int, frames: list[dict[str, Any]]) -> dict[str, Any]:
    samples = [frame_units(frame).get(actor) for frame in frames]
    samples = [sample for sample in samples if sample is not None]
    if not samples:
        return {"from": None, "to": None, "direction": None, "visualInterpolation": False}
    visual = [sample.visual for sample in samples if sample.visual is not None]
    return {
        "from": list(samples[0].tile), "to": list(samples[-1].tile),
        "direction": samples[0].direction,
        "visualInterpolation": len(set(visual)) > 1,
    }


def sequence_diff(source: list[Any], port: list[Any]) -> dict[str, Any] | None:
    if source == port:
        return None

    def frozen(value: Any) -> Any:
        if isinstance(value, dict):
            return tuple(sorted((key, frozen(item)) for key, item in value.items()))
        if isinstance(value, (list, tuple)):
            return tuple(frozen(item) for item in value)
        return value

    matcher = SequenceMatcher(a=[frozen(item) for item in source], b=[frozen(item) for item in port], autojunk=False)
    blocks = []
    for tag, i1, i2, j1, j2 in matcher.get_opcodes():
        if tag != "equal":
            blocks.append({"operation": tag, "source": source[i1:i2], "port": port[j1:j2]})
    return {"source": source, "port": port, "differences": blocks}


def compare_episodes(source: list[Episode], port: list[Episode]) -> tuple[list[dict[str, Any]], list[dict[str, Any]], int]:
    source_keys = [episode.decision for episode in source]
    port_keys = [episode.decision for episode in port]
    matcher = SequenceMatcher(a=source_keys, b=port_keys, autojunk=False)
    divergences: list[dict[str, Any]] = []
    mismatches: list[dict[str, Any]] = []
    common = 0
    for tag, i1, i2, j1, j2 in matcher.get_opcodes():
        if tag == "equal":
            for source_episode, port_episode in zip(source[i1:i2], port[j1:j2]):
                common += 1
                source_buckets, port_buckets = source_episode.buckets, port_episode.buckets
                if source_episode.camera is None or port_episode.camera is None:
                    source_buckets = [tuple(marker for marker in bucket if not marker.startswith("camera:")) for bucket in source_buckets]
                    port_buckets = [tuple(marker for marker in bucket if not marker.startswith("camera:")) for bucket in port_buckets]
                    source_buckets = [bucket for bucket in source_buckets if bucket]
                    port_buckets = [bucket for bucket in port_buckets if bucket]
                callback_diff = sequence_diff(source_buckets, port_buckets)
                movement_diff = None
                if source_episode.action == MOVE_ACTION:
                    movement_diff = sequence_diff(
                        [source_episode.movement], [port_episode.movement]
                    )
                camera_diff = None
                if source_episode.camera is not None and port_episode.camera is not None:
                    camera_diff = sequence_diff([source_episode.camera], [port_episode.camera])
                if callback_diff or movement_diff or camera_diff:
                    mismatches.append({
                        "decision": list(source_episode.decision),
                        "sourceFrameRange": [source_episode.start, source_episode.end],
                        "portFrameRange": [port_episode.start, port_episode.end],
                        "callbackOrder": callback_diff,
                        "movementContract": movement_diff,
                        "cameraContract": camera_diff,
                    })
        else:
            divergences.append({
                "operation": tag,
                "source": [episode.summary() for episode in source[i1:i2]],
                "port": [episode.summary() for episode in port[j1:j2]],
            })
    return divergences, mismatches, common


def contract_violations(trace: dict[str, Any]) -> list[dict[str, Any]]:
    """Check ordering properties observable without guessing an attacker."""
    frames = trace["frames"]
    episodes = extract_episodes(trace)
    violations: list[dict[str, Any]] = []
    visual_observable = capabilities(trace)["visualPosition"]
    for episode in episodes:
        flat = [marker for bucket in episode.buckets for marker in bucket]
        if episode.action == MOVE_ACTION:
            movement = episode.movement or {}
            if visual_observable and movement.get("from") != movement.get("to") and not movement.get("visualInterpolation"):
                violations.append({"kind": "move-without-visible-interpolation", "episode": episode.summary()})
        if episode.action in ATTACK_ACTIONS:
            # Every reaction/harm observed in this matched attacker clip must be
            # inside the episode; extraction already enforces that boundary.
            hp_markers = [marker for marker in flat if ":hp:damage" in marker]
            reaction_markers = [marker for marker in flat if ":reaction:" in marker]
            if hp_markers and not reaction_markers:
                violations.append({"kind": "damage-without-reaction-callback", "episode": episode.summary()})
    # Camp may not advance while a visible move/attack/death callback is open.
    for episode in episodes:
        initial = (episode.round, episode.camp)
        for index in range(episode.start + 1, min(episode.end + 1, len(frames))):
            current = (frames[index].get("round"), frames[index].get("camp"))
            if current != initial:
                violations.append({
                    "kind": "camp-advanced-before-action-callback", "episode": episode.summary(),
                    "observedAt": index, "from": list(initial), "to": list(current),
                })
                break
    return violations


def compact_boundary_sequence(trace: dict[str, Any]) -> list[list[Any]]:
    # Preserve trace order rather than sorting, because backwards/skipped camp
    # flow matters.
    previous: tuple[Any, Any] | None = None
    result = []
    for frame in trace["frames"]:
        key = (frame.get("round"), frame.get("camp"))
        if key != previous:
            result.append([key[0], key[1]])
            previous = key
    return result


def dialogue_sequence(trace: dict[str, Any]) -> list[str]:
    result: list[str] = []
    previous = False
    previous_identity: tuple[Any, Any] | None = None
    revision_capable = any("dialogueRevision" in frame for frame in trace["frames"])
    for frame in trace["frames"]:
        current = bool(frame.get("dialogue"))
        identity = (frame.get("dialogueRevision"), frame.get("dialogueIdentity")) if current else None
        # SayLayer can close and its successor can open in the same engine
        # tick. The boolean remains true, while revision/identity preserves
        # the two lifecycle edges.
        if revision_capable and current and previous and identity != previous_identity:
            result.extend(("close", "open"))
        elif current != previous:
            result.append("open" if current else "close")
        previous = current
        previous_identity = identity
    return result


def dialogue_content_sequence(trace: dict[str, Any]) -> list[tuple[str, str]]:
    """Return one normalized speaker/text pair per authored SayLayer."""
    result: list[tuple[str, str]] = []
    previous_identity: tuple[Any, Any] | None = None
    for frame in trace["frames"]:
        if not frame.get("dialogue"):
            previous_identity = None
            continue
        identity = frame.get("dialogueRevision"), frame.get("dialogueIdentity")
        if identity == previous_identity:
            continue
        text = re.sub(r"(?i)<br\s*/?>", "\n", str(frame.get("dialogueText", "")))
        text = "\n".join(line.rstrip() for line in text.replace("\r\n", "\n").replace("\r", "\n").split("\n")).strip()
        result.append((str(frame.get("dialogueSpeakerId", "")), text))
        previous_identity = identity
    return result


def control_sequence(trace: dict[str, Any], include_dialogue: bool = True) -> list[str]:
    """Camp/dialogue order, excluding tactical actions that may diverge."""
    if not include_dialogue:
        return [f"camp:{round_}:{camp}" for round_, camp in compact_boundary_sequence(trace)]
    result: list[str] = []
    previous_camp: tuple[Any, Any] | None = None
    previous_dialogue = False
    previous_dialogue_identity: tuple[Any, Any] | None = None
    revision_capable = any("dialogueRevision" in frame for frame in trace["frames"])
    for frame in trace["frames"]:
        camp = (frame.get("round"), frame.get("camp"))
        if camp != previous_camp:
            result.append(f"camp:{camp[0]}:{camp[1]}")
            previous_camp = camp
        if not include_dialogue:
            continue
        dialogue = bool(frame.get("dialogue"))
        identity = (frame.get("dialogueRevision"), frame.get("dialogueIdentity")) if dialogue else None
        if revision_capable and dialogue and previous_dialogue and identity != previous_dialogue_identity:
            result.extend(("dialogue:close", "dialogue:open"))
        elif dialogue != previous_dialogue:
            result.append("dialogue:open" if dialogue else "dialogue:close")
        previous_dialogue = dialogue
        previous_dialogue_identity = identity
    return result


def build_report(source: dict[str, Any], port: dict[str, Any]) -> dict[str, Any]:
    source_errors = validate_trace(source, "source")
    port_errors = validate_trace(port, "port")
    if source_errors or port_errors:
        return {"schemaErrors": source_errors + port_errors, "passed": False}
    source_caps, port_caps = capabilities(source), capabilities(port)
    source_episodes, port_episodes = extract_episodes(source), extract_episodes(port)
    if source_caps["authoredActionEpisodes"] and port_caps["authoredActionEpisodes"]:
        divergences, mismatches, common = compare_episodes(source_episodes, port_episodes)
        action_comparison_blocked = None
    else:
        divergences, mismatches, common = [], [], 0
        action_comparison_blocked = {
            "reason": "authored action clips are not observable in both traces",
            "sourceObservable": source_caps["authoredActionEpisodes"],
            "portObservable": port_caps["authoredActionEpisodes"],
            "sourceSchemaNote": "Original v1 slot 8 may be unit.unit().action() rather than the playing BattleUnit action; slot 14 must contain the playing anime clip.",
        }
    camp_diff = sequence_diff(compact_boundary_sequence(source), compact_boundary_sequence(port))
    comparable_dialogue_identity = source_caps["dialogueIdentity"] == port_caps["dialogueIdentity"]
    dialogue_diff = sequence_diff(dialogue_sequence(source), dialogue_sequence(port)) if comparable_dialogue_identity else None
    comparable_dialogue_content = source_caps["dialogueContent"] and port_caps["dialogueContent"]
    dialogue_content_diff = sequence_diff(
        dialogue_content_sequence(source), dialogue_content_sequence(port)
    ) if comparable_dialogue_content else None
    control_diff = sequence_diff(
        control_sequence(source, include_dialogue=comparable_dialogue_identity),
        control_sequence(port, include_dialogue=comparable_dialogue_identity),
    )
    source_contract = contract_violations(source)
    port_contract = contract_violations(port)
    capability_gaps = [
        {"observation": name, "source": source_caps[name], "port": port_caps[name]}
        for name in source_caps if source_caps[name] != port_caps[name]
    ]
    # These observations are inputs to comparisons that otherwise silently
    # disappear when a recorder omits their fields.  Treat absence as a
    # blocked verification, even when both traces share the same omission.
    required_observations = ("camera", "dialogueIdentity", "dialogueContent")
    comparison_blockers = [
        {"observation": name, "source": source_caps[name], "port": port_caps[name]}
        for name in required_observations
        if not source_caps[name] or not port_caps[name]
    ]
    # Tactical divergence is evidence, not a callback-order failure. However,
    # having no common episode makes a parity conclusion impossible.
    insufficient = common == 0
    incomplete = bool(source.get("_partial") or port.get("_partial"))
    source_terminal = bool(source.get("summary", {}).get("end")) or bool(source["frames"][-1].get("end"))
    port_terminal = bool(port.get("summary", {}).get("end")) or bool(port["frames"][-1].get("end"))
    terminal_mismatch = None if source_terminal and port_terminal else {
        "reason": "both traces must reach an observed battle terminal state",
        "sourceTerminal": source_terminal,
        "portTerminal": port_terminal,
    }
    config_mismatch = None
    source_config, port_config = source.get("config", {}), port.get("config", {})
    shared_seed_fields = [name for name in ("toolSeed", "mathSeed") if name in source_config and name in port_config]
    changed_seeds = {
        name: [source_config[name], port_config[name]]
        for name in shared_seed_fields if source_config[name] != port_config[name]
    }
    if changed_seeds:
        config_mismatch = {"seedDifferences": changed_seeds}
    # Every reported parity defect must participate in the gate.  In
    # particular, tactical divergence and authored dialogue-content drift are
    # not informational-only: accepting either would let a visibly different
    # play-through pass this verifier.
    passed = not (
        source_contract or port_contract or divergences or mismatches or
        camp_diff or dialogue_diff or dialogue_content_diff or control_diff or
        comparison_blockers or terminal_mismatch or config_mismatch or
        insufficient or incomplete
    )
    return {
        "format": "jojo-full-battle-order-verification/v1",
        "comparisonPolicy": {
            "timestampsIgnored": True,
            "frameCountsIgnored": True,
            "sameFrameEventsUnordered": True,
            "tacticalDivergenceIsNotParity": True,
            "onlyCommonDecisionEpisodesCompared": True,
        },
        "source": {"engine": source.get("engine"), "frames": len(source["frames"]), "capabilities": source_caps},
        "port": {"engine": port.get("engine"), "frames": len(port["frames"]), "capabilities": port_caps},
        "capabilityGaps": capability_gaps,
        "comparisonBlockers": comparison_blockers,
        "commonDecisionEpisodes": common,
        "sourceEpisodeCount": len(source_episodes),
        "portEpisodeCount": len(port_episodes),
        "decisionDivergences": divergences,
        "actionComparisonBlocked": action_comparison_blocked,
        "orderMismatches": mismatches,
        "campSequenceMismatch": camp_diff,
        "dialogueSequenceMismatch": dialogue_diff,
        "dialogueContentMismatch": dialogue_content_diff,
        "dialogueComparisonBlocked": None if comparable_dialogue_identity else {
            "reason": "dialogue close+open identity is not observable in both traces",
            "sourceObservable": source_caps["dialogueIdentity"],
            "portObservable": port_caps["dialogueIdentity"],
        },
        "controlSequenceMismatch": control_diff,
        "sourceContractViolations": source_contract,
        "portContractViolations": port_contract,
        "insufficientCommonCoverage": insufficient,
        "incompleteTrace": {
            "source": source.get("_partial"), "port": port.get("_partial"),
        } if incomplete else None,
        "terminalMismatch": terminal_mismatch,
        "configMismatch": config_mismatch,
        "terminalDiagnostics": {
            "source": terminal_diagnostics(source), "port": terminal_diagnostics(port),
        },
        "passed": passed,
    }


def terminal_diagnostics(trace: dict[str, Any]) -> dict[str, Any]:
    frames = trace.get("frames", [])
    if not frames:
        return {"terminal": False, "trailingStableFrames": 0, "hangCandidate": True}

    def signature(frame: dict[str, Any]) -> tuple[Any, ...]:
        unit_state = tuple(
            (character, view.tile, view.hp, view.mp, view.action, view.visible, view.exists)
            for character, view in sorted(frame_units(frame).items())
        )
        return (frame.get("round"), frame.get("camp"), bool(frame.get("end")),
                bool(frame.get("dialogue")), json.dumps(frame.get("script"), sort_keys=True), unit_state)

    last_signature = signature(frames[-1])
    start = len(frames) - 1
    while start > 0 and signature(frames[start - 1]) == last_signature:
        start -= 1
    stable_seconds = None
    if isinstance(frames[start].get("t"), (int, float)) and isinstance(frames[-1].get("t"), (int, float)):
        stable_seconds = round(frames[-1]["t"] - frames[start]["t"], 6)
    terminal = bool(frames[-1].get("end"))
    return {
        "terminal": terminal,
        "lastRound": frames[-1].get("round"), "lastCamp": frames[-1].get("camp"),
        "lastFrame": frames[-1].get("f"), "lastTime": frames[-1].get("t"),
        "trailingStableFrames": len(frames) - start,
        "trailingStableTraceSeconds": stable_seconds,
        "hangCandidate": bool(trace.get("_partial")) and not terminal,
        "note": "An interrupted non-terminal writer is only a hang candidate; frame evidence cannot distinguish SIGINT, renderer deadlock, or an external kill."
            if trace.get("_partial") and not terminal else None,
    }


def load_trace(path: Path) -> dict[str, Any]:
    """Load a complete trace, or recover all complete frames from an interrupted writer."""
    text = path.read_text(encoding="utf-8")
    try:
        return json.loads(text)
    except json.JSONDecodeError as original_error:
        marker = '"frames":['
        marker_at = text.find(marker)
        if marker_at < 0:
            raise original_error
        array_at = marker_at + len(marker) - 1
        header = json.loads(text[:array_at] + "[]}")
        decoder = json.JSONDecoder()
        cursor = array_at + 1
        frames: list[dict[str, Any]] = []
        while cursor < len(text):
            while cursor < len(text) and text[cursor] in " \t\r\n,":
                cursor += 1
            if cursor >= len(text) or text[cursor] == "]":
                break
            try:
                frame, end = decoder.raw_decode(text, cursor)
            except json.JSONDecodeError:
                break
            if not isinstance(frame, dict):
                break
            frames.append(frame)
            cursor = end
        if not frames:
            raise original_error
        header["frames"] = frames
        header["_partial"] = {
            "path": str(path), "completeFrames": len(frames), "stoppedAtByte": cursor,
            "fileBytes": len(text.encode("utf-8")), "parseError": str(original_error),
        }
        return header


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path, nargs="?", default=Path("/tmp/jojo-source-yingchuan-full.json"))
    parser.add_argument("port", type=Path, nargs="?", default=Path("build/reports/yingchuan-battle-regression-trace.json"))
    parser.add_argument("--output", type=Path)
    parser.add_argument("--allow-order-mismatch", action="store_true", help="write diagnostics but exit zero")
    args = parser.parse_args(argv)
    missing = [str(path) for path in (args.source, args.port) if not path.is_file()]
    if missing:
        print(json.dumps({"passed": False, "missingTraces": missing}, indent=2), file=sys.stderr)
        return 2
    source = load_trace(args.source)
    port = load_trace(args.port)
    report = build_report(source, port)
    encoded = json.dumps(report, ensure_ascii=False, indent=2)
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(encoded + "\n", encoding="utf-8")
    print(encoded)
    return 0 if report.get("passed") or args.allow_order_mismatch else 1


if __name__ == "__main__":
    raise SystemExit(main())
