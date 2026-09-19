'use strict';

// Keep --verify-desktop's diagnostic capture pending while the independent
// CDP client waits for the naturally scheduled opening dialogue.
const {app} = require('electron');
if (!app) throw new Error('hold_source_verification_exit must run as the Electron main entry');

app.on('browser-window-created', (_event, window) => {
  window.webContents.capturePage = () => new Promise(() => {});
});
require(process.env.JOJO_SOURCE_MAIN);
