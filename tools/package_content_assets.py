#!/usr/bin/env python3
"""Package game-owned assets without the recovered Cocos project.

Paths under SOURCE become resource paths. *.rgba.gz files are authored lossless
compressed framebuffers and are unpacked to *.rgba for the existing renderer.
All other files are copied verbatim. No external processes or image tools needed.
"""
from pathlib import Path
import gzip
import shutil
import sys


def package(source: Path, output: Path) -> int:
    source, output = source.resolve(), output.resolve()
    if source == output or source in output.parents or output in source.parents:
        raise ValueError('source and output must be separate directories')
    if not source.is_dir():
        raise ValueError(f'asset source directory does not exist: {source}')
    entries = {}
    for path in sorted(source.rglob('*')):
        if path.is_symlink():
            raise ValueError(f'asset symlinks are not supported: {path}')
        if not path.is_file() or path.name in ('README.md', '.DS_Store'):
            continue
        relative = path.relative_to(source)
        if path.name.endswith('.rgba.gz'):
            relative = relative.with_suffix('')
        if relative in entries:
            raise ValueError(f'duplicate resource path: {relative}')
        entries[relative] = path
    if output.exists():
        shutil.rmtree(output)
    output.mkdir(parents=True)
    for relative, path in entries.items():
        target = output / relative
        target.parent.mkdir(parents=True, exist_ok=True)
        if path.name.endswith('.rgba.gz'):
            with gzip.open(path, 'rb') as src, target.open('wb') as dst:
                shutil.copyfileobj(src, dst)
        else:
            shutil.copyfile(path, target)
    return len(entries)


if __name__ == '__main__':
    if len(sys.argv) != 3:
        raise SystemExit('usage: package_content_assets.py SOURCE_DIR OUTPUT_DIR')
    print(f'Packaged {package(Path(sys.argv[1]), Path(sys.argv[2]))} content assets')
