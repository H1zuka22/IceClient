package pl.iceclient.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GfxMathTest {
    @Test
    void cornerInsetShrinksTowardTheMiddle() {
        for (int r = 1; r <= 8; r++) {
            int prev = Integer.MAX_VALUE;
            for (int row = 0; row < r; row++) {
                int in = GfxMath.cornerInset(r, row);
                assertTrue(in >= 0 && in <= r);
                assertTrue(in <= prev);
                prev = in;
            }
            assertEquals(0, GfxMath.cornerInset(r, r - 1));
        }
    }

    @Test
    void mulAlphaKeepsRgbAndScalesAlpha() {
        assertEquals(0x80112233, GfxMath.mulAlpha(0xFF112233, 0.5f));
        assertEquals(0x00112233, GfxMath.mulAlpha(0xFF112233, 0f));
        assertEquals(0xFF112233, GfxMath.mulAlpha(0xFF112233, 2f));
    }

    @Test
    void lerpColorEndpointsAndMiddle() {
        assertEquals(0xFF000000, GfxMath.lerpColor(0xFF000000, 0xFFFFFFFF, 0f));
        assertEquals(0xFFFFFFFF, GfxMath.lerpColor(0xFF000000, 0xFFFFFFFF, 1f));
        assertEquals(0xFF808080, GfxMath.lerpColor(0xFF000000, 0xFFFFFFFF, 0.5f));
    }
}
