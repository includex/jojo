"""Load canonical dialogue text catalogs from core game resources."""
import json
from pathlib import Path


_REPO_ROOT = Path(__file__).resolve().parents[1]
_CATALOG_PATH = _REPO_ROOT / "core/src/main/resources/scenarios/dialogue-text.json"
_DOCUMENT = json.loads(_CATALOG_PATH.read_text(encoding="utf-8"))
_CATALOG = {**_DOCUMENT["entries"], **_DOCUMENT["aliases"]}


def dialogue_text(key):
    return _CATALOG[key]
