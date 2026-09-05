# Framework source contract inventory

Authoritative recovered files are `modules/framework/UILayer.js`, `UIScene.js`,
`UIFrame.js`, `Sound.js`, `serviceLayer.js`, and `skmLayer.js`.

Required isolated fixture axes before a gate is accepted:

1. UIScene direct/resource-missing add, same-id replacement, modal queue while
   `m_modeling`, queue dequeue after model removal, stack/top lookup, and node
   destroy release.
2. UILayer delegation, `[m_id]` self removal, cancel callback END_GAME route,
   and time/network loading overlay cancellation.
3. UIFrame init/onDestroy manager cleanup, touch start/move/end/cancel with
   interactivity checks and click/cancel sound priority, repeat/once schedule
   removal, resource path/sound route.
4. Sound enable/volume/replay/effect stop contracts under in-memory audio log.
5. service/skm button lifecycle and visibility-bit matrix.
