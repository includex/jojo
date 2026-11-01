"""Render each mod battle using the unchanged production JojoGame/BattleScreen."""
import pathlib
import subprocess
import sys
import shutil

root = pathlib.Path(__file__).resolve().parents[1]
build = subprocess.run([str(root / 'gradlew'), ':verification:classes', ':verification:printVerificationClasspath'],
                       cwd=root, text=True, capture_output=True)
if build.returncode:
    print(build.stdout + build.stderr)
    raise SystemExit(build.returncode)
classpath = next(line for line in build.stdout.splitlines() if line.startswith(str(root)) and 'kotlin-stdlib' in line)
# Keep this run's resources stable if another verification task rebuilds the jar.
snapshot = root / 'verification/build/linchong-smoke/runtime'
snapshot.mkdir(parents=True, exist_ok=True)
paths = []
for entry in classpath.split(':'):
    source = pathlib.Path(entry)
    if source.is_file() and source.suffix == '.jar' and source.is_relative_to(root):
        target = snapshot / source.name
        shutil.copy2(source, target)
        paths.append(str(target))
    else:
        paths.append(entry)
classpath = ':'.join(paths)
scenarios = sys.argv[1:] or [f'S_{i}' for i in range(90, 98)]
for scenario in scenarios:
    assert scenario in [f'S_{i}' for i in range(90, 98)], scenario
    out = root / 'verification/build/linchong-smoke' / scenario
    out.parent.mkdir(parents=True, exist_ok=True)
    cmd = ['java'] + (['-XstartOnFirstThread'] if sys.platform == 'darwin' else [])
    cmd += ['--enable-native-access=ALL-UNNAMED', '-cp', classpath,
            'com.jojo.game.verification.LinchongModSmokeLauncher', scenario, str(out)]
    result = subprocess.run(cmd, cwd=root, text=True, capture_output=True, timeout=110)
    out.with_suffix('.log').write_text(result.stdout + result.stderr)
    if result.returncode or f'LINCHONG_SMOKE_PASS {scenario}' not in result.stdout:
        print(result.stdout + result.stderr)
        raise SystemExit(result.returncode or 1)
    print(f'PASS {scenario}: production battle ready; screenshots in {out.parent}', flush=True)
