import copy
import unittest
from unittest.mock import patch
from verify_opening_unit_ready import ACTORS, game_readiness, source_readiness


class UnitReadinessTest(unittest.TestCase):
    def test_texture_loaded_on_a_later_frame_does_not_count_as_initial_readiness(self):
        game = dict(contract='natural-opening-event-timing-game/v1', dialogueInputs=0, pixelReadback=False, isolation=False,
                    frames=[dict(frame=1, actors=[dict(id=181, visible=True)], loadedHallTextureIds=[363]),
                            dict(frame=2, actors=[dict(id=181, visible=True)], loadedHallTextureIds=[363, 364])])
        required = [dict(actorId=181, textureIds=[363, 364])]
        self.assertEqual([364], game_readiness(game, required)[0]['missingTextureIds'])
        game['frames'][0]['loadedHallTextureIds'].append(364)
        self.assertTrue(game_readiness(game, required)[0]['readyAtFirstVisibleFrame'])

    def source(self):
        events = []
        for actor in ACTORS:
            def add(kind, **fields):
                events.append(dict(kind=kind, id=actor, frame=1, **fields))
            add('HallLayer._showHallUnit.entry')
            add('HallUnit._loadFunitTexture.entry', avatar=actor)
            for texture in (actor * 2 + 1, actor * 2 + 2):
                add('HallLayer.loadByUrl.request', url=f'Game/Pmapobj2/{texture}')
                add('HallLayer.loadByUrl.callback', url=f'Game/Pmapobj2/{texture}', textureReady=True, err='0')
            add('HallUnit._loadFunitTexture.resolved', textureCount=2, texturesReady=[True, True])
            add('HallUnit.onInit.resolved', animeReady=True)
            add('HallLayer._showHallUnit.callback', animeReady=True)
        return dict(unitReadyEvents=events)

    @patch('verify_opening_unit_ready.validate_source')
    def test_source_requires_sequential_pair_then_animation_then_ready(self, _):
        source = self.source()
        self.assertEqual(list(ACTORS), [r['actorId'] for r in source_readiness(source)])
        for mutate in (
            lambda e: e[3].update(textureReady=False),
            lambda e: e[7].update(animeReady=False),
            lambda e: e.__setitem__(slice(3, 5), [e[4], e[3]]),
            lambda e: e.__setitem__(slice(7, 9), [e[8], e[7]]),
        ):
            bad = copy.deepcopy(source)
            mutate(bad['unitReadyEvents'])
            with self.assertRaises(ValueError):
                source_readiness(bad)

    @patch('verify_opening_unit_ready.validate_source')
    def test_source_missing_or_duplicate_completion_fails(self, _):
        for duplicate in (False, True):
            source = self.source()
            if duplicate:
                source['unitReadyEvents'].append(source['unitReadyEvents'][-1])
            else:
                source['unitReadyEvents'].pop()
            with self.assertRaises(ValueError):
                source_readiness(source)


if __name__ == '__main__':
    unittest.main()
