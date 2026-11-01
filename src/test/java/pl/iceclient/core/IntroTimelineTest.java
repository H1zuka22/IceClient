package pl.iceclient.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class IntroTimelineTest {
    @Test
    void assemblyGoesFromZeroToOneAndFinishesBeforeTheCubeTurns() {
        assertEquals(0.0, IntroTimeline.assemble(0), 1e-9);
        assertEquals(1.0, IntroTimeline.assemble(IntroTimeline.ASSEMBLE_START_MS + IntroTimeline.ASSEMBLE_MS), 1e-9);
        assertTrue(IntroTimeline.assemble(800) > IntroTimeline.assemble(400));
        assertTrue(IntroTimeline.cubeTime(RubikCube.LEAD_MS - 1) < 0);
        assertEquals(0L, IntroTimeline.cubeTime(RubikCube.LEAD_MS));
    }

    @Test
    void lettersAppearInOrderAndFinish() {
        long t = IntroTimeline.LETTER_START + 100;
        assertTrue(IntroTimeline.letter(0, t) > IntroTimeline.letter(5, t));
        assertEquals(0.0, IntroTimeline.letter(0, 0), 1e-9);
        for (int i = 0; i < IntroTimeline.TITLE.length(); i++) assertEquals(1.0, IntroTimeline.letter(i, 9000), 1e-9);
    }

    @Test
    void sweepRunsOnlyInItsWindowAndMovesForward() {
        assertFalse(IntroTimeline.sweepActive(0));
        assertTrue(IntroTimeline.sweepActive(IntroTimeline.SWEEP_START + 100));
        assertFalse(IntroTimeline.sweepActive(IntroTimeline.SWEEP_START + IntroTimeline.SWEEP_MS + 1));
        assertTrue(IntroTimeline.sweep(IntroTimeline.SWEEP_START + 600) > IntroTimeline.sweep(IntroTimeline.SWEEP_START + 100));
    }

    @Test
    void glowStaysInRange() {
        for (long t = 0; t < 5000; t += 37) {
            double g = IntroTimeline.glow(t);
            assertTrue(g >= 0 && g <= 1);
        }
    }

    @Test
    void outBackEndpoints() {
        assertEquals(0.0, Ease.outBack(0), 1e-9);
        assertEquals(1.0, Ease.outBack(1), 1e-9);
    }
}
