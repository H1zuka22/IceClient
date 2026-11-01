package pl.iceclient.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CubeLayoutTest {
    @Test
    void lerpEndpointsMatchBothLayouts() {
        CubeLayout.Layout a = CubeLayout.loading(854, 480), b = CubeLayout.title(854, 480);
        assertEquals(a, CubeLayout.lerp(a, b, 0));
        assertEquals(b, CubeLayout.lerp(a, b, 1));
        assertEquals(a, CubeLayout.lerp(a, b, -3));
        assertEquals(b, CubeLayout.lerp(a, b, 9));
    }

    @Test
    void layoutsAreOnScreenForCommonSizes() {
        int[][] sizes = {{320, 180}, {427, 240}, {854, 480}, {1280, 720}, {1920, 1080}};
        for (int[] s : sizes) {
            for (CubeLayout.Layout l : new CubeLayout.Layout[] {CubeLayout.loading(s[0], s[1]), CubeLayout.title(s[0], s[1])}) {
                assertTrue(l.cx() > 0 && l.cx() < s[0] && l.cy() > 0 && l.cy() < s[1] && l.scale() >= 14f);
            }
        }
    }

    @Test
    void titleCubeHiddenOnNarrowScreens() {
        assertFalse(CubeLayout.titleVisible(359));
        assertTrue(CubeLayout.titleVisible(360));
    }
}
