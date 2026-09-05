"""Canonical late-battle observations transcribed from the source scenarios."""

# Sources:
#   sgccz-desktop/decompiled-python/S_52.py lines 274-328
#   sgccz-desktop/decompiled-python/S_57.py lines 144-198, 778-857
# S_57's two mutually exclusive branches share the center sequence and differ
# only in the authored setUnitStatus rectangle.
EXACT_CONTRACTS = {
    "S_52": {
        "objectsPrefix": (
            "transition:objects:1:17:61,7,15;62,7,16",
            "transition:objects:1:17:69,4,8;70,5,8",
            "transition:objects:1:17:67,7,10;68,7,11",
            "transition:objects:1:17:65,12,15;66,12,16",
            "transition:objects:1:17:71,12,5;72,12,6",
            "transition:objects:1:17:73,14,3;74,15,3",
        ),
    },
    "S_57": {
        "objectsPrefix": (
            "transition:objects:1:17:93,19,23;94,20,23",
            "transition:objects:1:17:85,19,27;86,20,27",
            "transition:objects:1:17:91,16,19;92,16,20",
            "transition:objects:1:17:83,12,19;84,12,20",
            "transition:objects:1:17:95,19,16;96,20,16",
            "transition:objects:1:17:87,19,12;88,20,12",
        ),
        "center": (
            "transition:camera:center:5:20",
            "transition:camera:center:11:20",
            "transition:camera:center:13:20",
            "transition:camera:center:11:20",
            "transition:camera:center:13:20",
            "transition:camera:center:11:20",
            "transition:camera:center:13:20",
            "transition:camera:center:11:20",
            "transition:camera:center:13:20",
        ),
        "setUnitStatusAlternatives": (
            ("transition:setUnitStatus:rect=0,0,12,39,39:hp=0:mp=0:states=9",),
            ("transition:setUnitStatus:rect=0,12,0,39,39:hp=0:mp=0:states=9",),
        ),
    },
}


def canonical_observation(value: str) -> str:
    """Remove path-dependent resolved IDs while retaining authored payload."""
    return value.split(":resolved=", 1)[0]


def exact_contract_errors(scenario: str, sequences: dict[str, list[str]]) -> list[str]:
    exact = EXACT_CONTRACTS.get(scenario, {})
    errors: list[str] = []
    objects_prefix = exact.get("objectsPrefix")
    if objects_prefix is not None and tuple(sequences.get("objects", ())[:len(objects_prefix)]) != objects_prefix:
        errors.append("authored objects payload/order does not match canonical source prefix")
    centers = exact.get("center")
    if centers is not None and tuple(sequences.get("center", ())) != centers:
        errors.append("authored center payload/order does not match canonical source sequence")
    alternatives = exact.get("setUnitStatusAlternatives")
    if alternatives is not None and tuple(sequences.get("setUnitStatus", ())) not in alternatives:
        errors.append("authored setUnitStatus payload/order does not match either canonical source branch")
    return errors
