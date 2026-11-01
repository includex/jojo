# Runtime Lua rules

Copy this directory to a writable location, then start the desktop JVM with
`-Djojo.rules.dir=/absolute/path/to/copied-directory`. It must contain
`lua-rules.yaml`; script paths are relative to that directory. The full registry
is loaded again when a new battle is assembled, so edit scripts and YAML and
start another battle without rebuilding. An empty registry disables custom rules.
The packaged registry is empty to preserve existing gameplay.

Replace the sample character/equipment ids with ids from your content. Bindings
are sidecar metadata and do not change character or equipment binary schemas.
Global bindings omit `id`. Character/equipment bindings require a nonnegative id.
Rules execute in priority/id order. Parameters are scalar values exposed as strings.
Modules return a table containing `evaluate(ctx, params)`; that function returns a
dense array of effect tables. Every invocation uses an isolated sandbox.

`ctx.actor`, `ctx.target`, and `ctx.owner` contain unit snapshots; `ctx.owner.resources`
contains declared custom resource values. `ctx.sourceId` identifies the current source
when available. `ctx.phase` is `POWER` or `DAMAGE` for combat calculations. Available
triggers are `TURN_START`, `TURN_END`, `ATTACK`, `HIT`, `ACTION_COMPLETED`, and
`DAMAGE_CALCULATION`. ATTACK runs for the attacker; HIT runs for the defender.
Use `AttackPowerModifier` during POWER and `DamageModifier` during DAMAGE.
Recovery effects can change HP/MP; resource/status effects require declared ids.
Scripts cannot read files, access the network, import libraries, or retain state.
Instruction, recursion, source, config, and effect limits reject runaway scripts.
Invalid packages fail loading rather than partially replacing the registry.
