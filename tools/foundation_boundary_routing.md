# Core foundation boundary routing

The isolated foundation trace uses a shared in-memory boundary on both sides.

- `UserDefault` maps Cocos localStorage and JSB `UserData.json` to separate
  in-memory stores and records each write.  Its recovered MD5/XOR envelope is
  exercised on the reference JS side; the game keeps the same local/global routing and
  flush ordering.
- `JSEvent` substitutes Manager's outer `ERROR` dispatcher with an event log;
  listener iteration, self-removal, once removal, queued dispatch and cleanup
  are evaluated by the recovered factory.
- `StatusManager` has no external boundary. Its trace preserves transition
  ordering rather than just final status.
- UUID/Tool/MD5 are pure recovered algorithms and do not use a runtime API.
