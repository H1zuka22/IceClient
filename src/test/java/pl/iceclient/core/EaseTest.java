package pl.iceclient.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EaseTest {
    @Test
    void endpointsAreExact() {
        assertEquals(0.0, Ease.outCubic(0), 1e-9);
        assertEquals(1.0, Ease.outCubic(1), 1e-9);
        assertEquals(0.0, Ease.inOutCubic(0), 1e-9);
        assertEquals(1.0, Ease.inOutCubic(1), 1e-9);
    }

    @Test
    void clampsOutOfRangeInput() {
        assertEquals(0.0, Ease.outCubic(-5), 1e-9);
        assertEquals(1.0, Ease.inOutCubic(7), 1e-9);
    }

    @Test
    void isMonotonic() {
        double prevA = -1, prevB = -1;
        for (int i = 0; i <= 100; i++) {
            double t = i / 100.0;
            assertTrue(Ease.outCubic(t) >= prevA);
            assertTrue(Ease.inOutCubic(t) >= prevB);
            prevA = Ease.outCubic(t);
            prevB = Ease.inOutCubic(t);
        }
    }
}
