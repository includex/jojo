'use strict';

const assert = require('node:assert/strict');
const test = require('node:test');
const {paginateDialogueCalls} = require('./street_body_label_pages.cjs');

test('starts a new page after three authored lines', () => {
  assert.deepEqual(paginateDialogueCalls(['&7\none\ntwo\nthree\nfour']), {
    pages: ['one\ntwo\nthree', 'four'],
    origins: [
      {callIndex: 0, pageInCall: 1, speakerId: '7', authoredLines: [2, 3, 4]},
      {callIndex: 0, pageInCall: 2, speakerId: '7', authoredLines: [5]},
    ],
  });
});

test('speaker change flushes the current page and each call declares its speaker', () => {
  assert.deepEqual(paginateDialogueCalls(['&181<br/>first<br/>&0<br/>second', '&0\nthird']), {
    pages: ['first', 'second', 'third'],
    origins: [
      {callIndex: 0, pageInCall: 1, speakerId: '181', authoredLines: [2]},
      {callIndex: 0, pageInCall: 2, speakerId: '0', authoredLines: [4]},
      {callIndex: 1, pageInCall: 1, speakerId: '0', authoredLines: [2]},
    ],
  });
});

test('discards empty authored lines but preserves their source line numbers', () => {
  assert.deepEqual(paginateDialogueCalls(['\n&3\n\nalpha\n\nbeta\n']), {
    pages: ['alpha\nbeta'],
    origins: [{callIndex: 0, pageInCall: 1, speakerId: '3', authoredLines: [4, 6]}],
  });
});

test('maxPages stops exactly at the requested page boundary', () => {
  assert.deepEqual(paginateDialogueCalls(['&1\na\nb\nc\nd', '&2\ne'], 1), {
    pages: ['a\nb\nc'],
    origins: [{callIndex: 0, pageInCall: 1, speakerId: '1', authoredLines: [2, 3, 4]}],
  });
  assert.throws(() => paginateDialogueCalls(['&1\na'], 0), /maxPages/);
});

test('rejects malformed input and unsupported markup', () => {
  assert.throws(() => paginateDialogueCalls('not-an-array'), /array/);
  assert.throws(() => paginateDialogueCalls([42]), /must be a string/);
  assert.throws(() => paginateDialogueCalls(['&who\ntext']), /malformed speaker marker/);
  assert.throws(() => paginateDialogueCalls(['text without speaker']), /before a speaker marker/);
  assert.throws(() => paginateDialogueCalls(['&1\n<color=#fff>text<\/color>']), /unsupported rich markup/);
  assert.throws(() => paginateDialogueCalls(['&1\ntext'], -1), /maxPages/);
});
