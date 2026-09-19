'use strict';

const SPEAKER = /^&(\d+)$/;
const ANY_TAG = /<[^>]*>/;

/**
 * Convert authored dialogue calls into the visible pages produced by the
 * recovered DialogueLayer: empty lines are discarded, speaker markers start
 * a new page, and at most three authored text lines appear on one page.
 *
 * Only plain text and the source's exact <br/> delimiter are supported. The
 * returned text uses newlines so it can be used as a stable manifest key.
 */
function paginateDialogueCalls(calls, maxPages = Infinity) {
  if (!Array.isArray(calls)) throw new TypeError('calls must be an array of strings');
  if (maxPages !== Infinity && (!Number.isInteger(maxPages) || maxPages <= 0)) {
    throw new RangeError('maxPages must be a positive integer or Infinity');
  }

  const pages = [];
  const origins = [];
  for (let callIndex = 0; callIndex < calls.length && pages.length < maxPages; callIndex++) {
    const call = calls[callIndex];
    if (typeof call !== 'string') throw new TypeError(`calls[${callIndex}] must be a string`);
    const normalized = call.replace(/\r\n?/g, '\n').replaceAll('<br/>', '\n');
    if (ANY_TAG.test(normalized)) {
      throw new Error(`calls[${callIndex}] contains unsupported rich markup`);
    }

    const authored = normalized.split('\n');
    let speakerId = null;
    let pageLines = [];
    let authoredLines = [];
    let pageInCall = 0;

    const flush = () => {
      if (!pageLines.length || pages.length >= maxPages) return;
      pageInCall++;
      pages.push(pageLines.join('\n'));
      origins.push({callIndex, pageInCall, speakerId, authoredLines: authoredLines.slice()});
      pageLines = [];
      authoredLines = [];
    };

    for (let lineIndex = 0; lineIndex < authored.length && pages.length < maxPages; lineIndex++) {
      const line = authored[lineIndex];
      if (line.length === 0) continue;
      const marker = SPEAKER.exec(line);
      if (marker) {
        flush();
        speakerId = marker[1];
        continue;
      }
      if (line.startsWith('&')) {
        throw new Error(`calls[${callIndex}] line ${lineIndex + 1} has malformed speaker marker`);
      }
      if (speakerId === null) {
        throw new Error(`calls[${callIndex}] has visible text before a speaker marker`);
      }
      pageLines.push(line);
      authoredLines.push(lineIndex + 1);
      if (pageLines.length === 3) flush();
    }
    flush();
  }

  return {pages, origins};
}

module.exports = {paginateDialogueCalls};
