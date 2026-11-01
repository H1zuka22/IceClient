package pl.iceclient.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SnowFieldTest {
    @Test
    void flakesStayInsideTheScreenAndAreDeterministic() {
        int n = 80, w = 427, h = 240;
        float[] x1 = new float[n], y1 = new float[n], x2 = new float[n], y2 = new float[n];
        int[] s = new int[n];
        for (long t = 0; t < 600000; t += 12345) {
            SnowField.fill(n, w, h, t, x1, y1, s);
            SnowField.fill(n, w, h, t, x2, y2, s);
            for (int i = 0; i < n; i++) {
                assertTrue(x1[i] >= 0 && x1[i] < w + 1);
                assertTrue(y1[i] >= 0 && y1[i] < h + 1);
                assertEquals(x1[i], x2[i]);
                assertEquals(y1[i], y2[i]);
            }
        }
    }
}
