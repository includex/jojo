"""Load the canonical dialogue text catalog from authored scenario content."""
import json
from pathlib import Path


_REPO_ROOT = Path(__file__).resolve().parents[1]
_CATALOG_PATH = _REPO_ROOT / "content/scenarios/dialogue-text.json"
_DOCUMENT = json.loads(_CATALOG_PATH.read_text(encoding="utf-8"))
_CATALOG = {**_DOCUMENT["entries"], **_DOCUMENT["aliases"]}
_LEGACY_KEYS = _DOCUMENT.get("legacyKeys", {})


def dialogue_text(key):
    return _CATALOG[_LEGACY_KEYS.get(key, key)]
