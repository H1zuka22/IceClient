package pl.iceclient.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class RubikCubeTest {
    @Test
    void fullSequenceSolvesTheCube() {
        assertTrue(new RubikCube(7, 7).sequenceSolves());
        assertTrue(new RubikCube(12345, 11).sequenceSolves());
    }

    @Test
    void scrambleActuallyScrambles() {
        RubikCube c = new RubikCube(7, 7);
        assertTrue(c.solvedAfter(0));
        assertFalse(c.solvedAfter(c.scrambleLength()));
    }

    @Test
    void fourQuarterTurnsAreIdentityForEveryLayer() {
        for (int axis = 0; axis < 3; axis++)
            for (int layer = -1; layer <= 1; layer++)
                for (int dir = -1; dir <= 1; dir += 2)
                    assertTrue(RubikCube.fourTurnsIdentity(new RubikCube.Move(axis, layer, dir)));
    }

    @Test
    void sameSeedGivesSameMoves() {
        assertEquals(new RubikCube(7, 7).moves(), new RubikCube(7, 7).moves());
    }

    @Test
    void quadsAreDepthSortedAndHaveAPlausibleStickerCount() {
        RubikCube c = new RubikCube(7, 7);
        for (long t : new long[] {0, 200, 3000, 6000}) {
            List<RubikCube.Quad> q = c.build(t);
            assertFalse(q.isEmpty());
            float prev = -Float.MAX_VALUE;
            int stickers = 0;
            for (RubikCube.Quad quad : q) {
                assertTrue(quad.depth() >= prev);
                prev = quad.depth();
                if (RubikCube.isSticker(quad)) stickers++;
                assertEquals(4, quad.x().length);
            }
            assertTrue(stickers >= 9 && stickers <= 27);
        }
    }

    @Test
    void assembledEqualsDefaultBuild() {
        RubikCube c = new RubikCube(7, 7);
        List<RubikCube.Quad> a = c.build(2500), b = c.build(2500, 1.0);
        assertEquals(a.size(), b.size());
        for (int i = 0; i < a.size(); i++) {
            assertEquals(a.get(i).color(), b.get(i).color());
            assertEquals(a.get(i).x()[0], b.get(i).x()[0]);
        }
    }

    @Test
    void notAssembledIsInvisibleAndPartialIsInBetween() {
        RubikCube c = new RubikCube(7, 7);
        assertTrue(c.build(0, 0.0).isEmpty());
        int partial = c.build(0, 0.5).size(), full = c.build(0, 1.0).size();
        assertTrue(partial > 0 && partial <= full);
    }

    @Test
    void negativeTimeKeepsTheCubeSolvedAndStillRenders() {
        RubikCube c = new RubikCube(7, 7);
        assertFalse(c.build(-1500, 1.0).isEmpty());
        assertEquals(c.build(0, 1.0).size(), c.build(-1500, 1.0).size());
    }

    @Test
    void cubeStartsTurningOnlyAfterTheAssemblyEnds() {
        assertTrue(RubikCube.LEAD_MS >= IntroTimeline.ASSEMBLE_START_MS + IntroTimeline.ASSEMBLE_MS);
    }
}
