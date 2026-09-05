# Render log comparator

`compare_render_logs.py` compares semantic render logs without reading or
capturing images. It accepts reference Cocos fixture snapshots, current game
composition traces, a canonical draw log, and the line-delimited JSON event
stream emitted by `--render-event-log` in both runtimes.

```sh
python3 tools/compare_render_logs.py \
  ../jojo_mobile/sgccz-desktop/build/python-source-hall-buy-fixture.json \
  desktop/.verification-work/nonbattle/current/game-hall-buy-agent.json \
  --json-out build/render-log-diff.json \
  --text-out build/render-log-diff.txt
```

The command exits `0` for equality, `1` for semantic differences, and `2` for
invalid input. Timestamps and timing-only keys are discarded. The default
`1e-5` absolute float tolerance exists only for serialization noise; draw
type, text, paths, order, assets, opacity, blend state, and visibility remain
exact. `phase`, owning `layer`, `frame`, and `timestamp` are recorded for
diagnostics, but phase/layer are implementation metadata and frame/timestamp
are timing metadata, so they are intentionally
excluded so timing drift cannot hide or create a semantic rendering failure.

For new renderers, emit the canonical format so every requested field is
directly comparable:

```json
{
  "viewport": [1280, 688],
  "draws": [
    {
      "path": "dialogue/panel",
      "phase": "street-dialogue-stable",
      "layer": "DialogueLayer",
      "drawType": "sprite",
      "rect": [184.0, 12.0, 912.0, 180.0],
      "asset": "ui/dialogue-panel",
      "opacity": 255,
      "blend": {"src": 770, "dst": 771},
      "visible": true,
      "text": null
    }
  ]
}
```

Run the focused tests with:

```sh
python3 tools/test_compare_render_logs.py
```

Before any final screenshot pass, re-run the inputs named by every accepted
comparison report. This prevents an old green report from opening the image
gate after renderer code or logs changed:

```sh
python3 tools/verify_render_parity_reports.py build/render-events/*-final.json
```

The complete scope is tracked separately. A state with no report, a missing
report, a stale report, or a newly failing comparison keeps image capture
blocked:

```sh
python3 tools/verify_render_parity_scope.py
```

The scope also points at `tools/render_layer_inventory.json`.  That inventory
classifies every recovered JavaScript module under the visible UI, battle,
platform, misc, framework, and game-data surfaces.  Every module classified
as `required` must be named by at least one state's `sourceLayers`; every
non-rendering module must carry an explicit reason.  A newly recovered,
deleted, duplicated, unclassified, or unmapped module blocks the same gate,
so a green percentage cannot be produced by silently omitting a scene root,
actor renderer, modal, or conditional layer.
