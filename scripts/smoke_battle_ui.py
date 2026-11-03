"""Capture production battle overlays at two window sizes for visual review."""
from pathlib import Path
import subprocess,shutil,sys
root=Path(__file__).resolve().parents[1]
build=subprocess.run([str(root/'gradlew'),':verification:classes',':verification:printVerificationClasspath'],cwd=root,text=True,capture_output=True)
if build.returncode:print(build.stdout+build.stderr);raise SystemExit(build.returncode)
classpath=next(line for line in build.stdout.splitlines() if line.startswith(str(root)) and 'kotlin-stdlib' in line)
snapshot=root/'verification/build/battle-ui/runtime';snapshot.mkdir(parents=True,exist_ok=True)
paths=[]
for entry in classpath.split(':'):
 source=Path(entry)
 if source.is_file() and source.suffix=='.jar' and source.is_relative_to(root):
  target=snapshot/source.name;shutil.copy2(source,target);paths.append(str(target))
 else:paths.append(entry)
mode = sys.argv[1] if len(sys.argv) > 1 else "battle"
assert mode in ("battle", "scenario")
launcher = "ScenarioUiSmokeLauncher" if mode == "scenario" else "BattleUiSmokeLauncher"
for width,height in ([(1280,688)] if mode == "scenario" else [(1280,688),(960,540)]):
 out=root/'verification/build/scenario-ui' if mode == 'scenario' else root/f'verification/build/battle-ui/{width}x{height}';out.mkdir(parents=True,exist_ok=True)
 command=['java']+(['-XstartOnFirstThread'] if sys.platform=='darwin' else [])+['--enable-native-access=ALL-UNNAMED','-cp',':'.join(paths),'com.jojo.game.verification.' + launcher,str(out),str(width),str(height)]
 result=subprocess.run(command,cwd=root,text=True,capture_output=True,timeout=230)
 (out/'run.log').write_text(result.stdout+result.stderr)
 if result.returncode or ('SCENARIO_UI_SMOKE_PASS' if mode == 'scenario' else 'BATTLE_UI_SMOKE_PASS') not in result.stdout:print(result.stdout+result.stderr);raise SystemExit(result.returncode or 1)
 print(f'PASS {width}x{height}: {out}',flush=True)
