package pl.iceclient.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Animacja kostki Rubika 3x3x3: kostka miesza się obrotami warstw, a potem układa odwrotnymi ruchami.
 * Czysta matematyka bez zależności od Minecrafta. Stan jest liczony od zera dla danego czasu,
 * więc animacja jest bezstanowa i deterministyczna.
 */
public final class RubikCube {
    public static final int MOVE_MS = 460, PAUSE_MS = 90, HOLD_MS = 900;
    /** Czas od startu zegara, zanim kostka zacznie się obracać (najpierw się składa). */
    public static final int LEAD_MS = 1700;
    private static final double CAM = 9.0;
    public static final int DARK = 0xFF08121D;
    // kolory ścian: +x, -x, +y, -y, +z, -z (paleta lodowa)
    private static final int[] PALETTE = {0xFFE9F7FF, 0xFF5CC8FF, 0xFF3DDCC0, 0xFF8B9CFF, 0xFFB8C7D9, 0xFF2F6FDB};

    public record Move(int axis, int layer, int dir) {
        public Move inverse() {
            return new Move(axis, layer, -dir);
        }
    }

    public record Quad(float[] x, float[] y, float depth, int color) {}

    private static final class Cubie {
        final int[] p0;
        int[] p;
        int[] r = identity();

        Cubie(int x, int y, int z) {
            p0 = new int[] {x, y, z};
            p = p0.clone();
        }
    }

    private final List<Move> seq = new ArrayList<>();
    private final int scrambleLen;

    public RubikCube(long seed, int scramble) {
        Random rnd = new Random(seed);
        int lastAxis = -1;
        for (int i = 0; i < scramble; i++) {
            int axis;
            do {
                axis = rnd.nextInt(3);
            } while (axis == lastAxis);
            lastAxis = axis;
            seq.add(new Move(axis, rnd.nextInt(3) - 1, rnd.nextBoolean() ? 1 : -1));
        }
        scrambleLen = seq.size();
        for (int i = scrambleLen - 1; i >= 0; i--) seq.add(seq.get(i).inverse());
    }

    public List<Move> moves() {
        return List.copyOf(seq);
    }

    public int scrambleLength() {
        return scrambleLen;
    }

    public int cycleMs() {
        return seq.size() * (MOVE_MS + PAUSE_MS) + HOLD_MS;
    }

    // ---------- logika stanu ----------

    private static int[] identity() {
        return new int[] {1, 0, 0, 0, 1, 0, 0, 0, 1};
    }

    static int[] rotMat(int axis, int dir) {
        int c = 0, s = dir;
        return switch (axis) {
            case 0 -> new int[] {1, 0, 0, 0, c, -s, 0, s, c};
            case 1 -> new int[] {c, 0, s, 0, 1, 0, -s, 0, c};
            default -> new int[] {c, -s, 0, s, c, 0, 0, 0, 1};
        };
    }

    private static int[] mul(int[] a, int[] b) {
        int[] o = new int[9];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++) o[i * 3 + j] += a[i * 3 + k] * b[k * 3 + j];
        return o;
    }

    private static int[] mulV(int[] m, int[] v) {
        int[] o = new int[3];
        for (int i = 0; i < 3; i++) o[i] = m[i * 3] * v[0] + m[i * 3 + 1] * v[1] + m[i * 3 + 2] * v[2];
        return o;
    }

    private static Cubie[] solved() {
        Cubie[] cs = new Cubie[27];
        int n = 0;
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -1; z <= 1; z++) cs[n++] = new Cubie(x, y, z);
        return cs;
    }

    private static void apply(Cubie[] cs, Move m) {
        int[] rot = rotMat(m.axis(), m.dir());
        for (Cubie c : cs) {
            if (c.p[m.axis()] == m.layer()) {
                c.p = mulV(rot, c.p);
                c.r = mul(rot, c.r);
            }
        }
    }

    private static boolean isSolved(Cubie[] cs) {
        for (Cubie c : cs) {
            if (!java.util.Arrays.equals(c.p, c.p0) || !java.util.Arrays.equals(c.r, identity())) return false;
        }
        return true;
    }

    /** Czy kostka jest ułożona po wykonaniu pierwszych n ruchów. */
    public boolean solvedAfter(int n) {
        Cubie[] cs = solved();
        for (int i = 0; i < n && i < seq.size(); i++) apply(cs, seq.get(i));
        return isSolved(cs);
    }

    public boolean sequenceSolves() {
        return solvedAfter(seq.size());
    }

    /** Czterokrotny obrót ćwierć obrotu o tę samą warstwę musi dać stan wyjściowy. */
    public static boolean fourTurnsIdentity(Move m) {
        Cubie[] cs = solved();
        for (int i = 0; i < 4; i++) apply(cs, m);
        return isSolved(cs);
    }

    public static boolean isSticker(Quad q) {
        return (q.color() & 0xFFFFFF) != (DARK & 0xFFFFFF);
    }

    // ---------- geometria ----------

    private static double[] rotAxis(double[] v, int axis, double th) {
        double c = Math.cos(th), s = Math.sin(th), x = v[0], y = v[1], z = v[2];
        return switch (axis) {
            case 0 -> new double[] {x, y * c - z * s, y * s + z * c};
            case 1 -> new double[] {x * c + z * s, y, -x * s + z * c};
            default -> new double[] {x * c - y * s, x * s + y * c, z};
        };
    }

    private static double[] view(double[] v, double yaw, double pitch) {
        return rotAxis(rotAxis(v, 1, yaw), 0, pitch);
    }

    private static double[] mulVd(int[] m, double[] v) {
        return new double[] {
            m[0] * v[0] + m[1] * v[1] + m[2] * v[2],
            m[3] * v[0] + m[4] * v[1] + m[5] * v[2],
            m[6] * v[0] + m[7] * v[1] + m[8] * v[2]
        };
    }

    /** Czworokąty (ściany i naklejki) w jednostkach względnych, posortowane od najdalszych. */
    public List<Quad> build(long timeMs) {
        return build(timeMs, 1.0);
    }

    private static double hash01(int i, double k) {
        double v = Math.sin((i + 1) * k) * 43758.5453;
        return v - Math.floor(v);
    }

    /** Postęp składania pojedynczej kostki (każda zlatuje się w nieco innym momencie). */
    private static double assembleOf(int ci, double assemble) {
        return Ease.outCubic(Ease.clamp01(assemble * 1.7 - hash01(ci, 91.7) * 0.7));
    }

    private static double[] displacement(int ci, Cubie c, double amount) {
        if (amount <= 0) return new double[] {0, 0, 0};
        double x = c.p0[0] + (hash01(ci, 3.1) - 0.5) * 2;
        double y = c.p0[1] + (hash01(ci, 5.7) - 0.5) * 2;
        double z = c.p0[2] + (hash01(ci, 8.3) - 0.5) * 2;
        double k = amount * 5.0 / Math.max(1e-6, Math.sqrt(x * x + y * y + z * z));
        return new double[] {x * k, y * k, z * k};
    }

    /**
     * assemble: 0 = kostki rozproszone i niewidoczne, 1 = kostka złożona; wartości pośrednie
     * to zlatywanie się i pojawianie kostek. Czas ujemny oznacza kostkę ułożoną (przed startem obrotów).
     */
    public List<Quad> build(long timeMs, double assemble) {
        int step = MOVE_MS + PAUSE_MS;
        long t = Math.floorMod(Math.max(0L, timeMs), (long) cycleMs());
        int idx = (int) Math.min(seq.size(), t / step);
        long within = t - (long) idx * step;
        Cubie[] cs = solved();
        for (int i = 0; i < idx; i++) apply(cs, seq.get(i));
        Move act = null;
        double ang = 0;
        if (idx < seq.size()) {
            Move m = seq.get(idx);
            if (within < MOVE_MS) {
                act = m;
                ang = m.dir() * Math.PI / 2 * Ease.inOutCubic(within / (double) MOVE_MS);
            } else {
                apply(cs, m);
            }
        }
        double yaw = 0.6 + timeMs * 0.00042, pitch = -0.52;
        List<Quad> out = new ArrayList<>();
        for (int ci = 0; ci < cs.length; ci++) {
            Cubie c = cs[ci];
            double ai = assembleOf(ci, assemble);
            if (ai < 0.01) continue;
            double[] disp = displacement(ci, c, 1 - ai);
            boolean turning = act != null && c.p[act.axis()] == act.layer();
            for (int f = 0; f < 6; f++) {
                int axis = f / 2, sign = (f % 2 == 0) ? 1 : -1;
                double[] n = new double[3];
                n[axis] = sign;
                double[] nn = mulVd(c.r, n);
                if (turning) nn = rotAxis(nn, act.axis(), ang);
                nn = view(nn, yaw, pitch);
                if (nn[2] <= 0) continue; // ściana odwrócona od kamery
                int t1 = (axis + 1) % 3, t2 = (axis + 2) % 3;
                double[][] pts = new double[4][];
                double cx = 0, cy = 0, cz = 0;
                for (int k = 0; k < 4; k++) {
                    double s1 = (k == 0 || k == 3) ? -0.5 : 0.5, s2 = (k < 2) ? -0.5 : 0.5;
                    double[] local = new double[3];
                    local[axis] = 0.5 * sign;
                    local[t1] = s1;
                    local[t2] = s2;
                    double[] v = mulVd(c.r, local);
                    v[0] += c.p[0];
                    v[1] += c.p[1];
                    v[2] += c.p[2];
                    if (turning) v = rotAxis(v, act.axis(), ang);
                    v[0] += disp[0];
                    v[1] += disp[1];
                    v[2] += disp[2];
                    v = view(v, yaw, pitch);
                    pts[k] = v;
                    cx += v[0] / 4;
                    cy += v[1] / 4;
                    cz += v[2] / 4;
                }
                boolean colored = c.p0[axis] == sign;
                out.add(toQuad(pts, cx, cy, cz, 1.0, GfxMath.mulAlpha(DARK, (float) ai), (float) (cz - (colored ? 0 : 0.3))));
                if (colored) out.add(toQuad(pts, cx, cy, cz, 0.84, GfxMath.mulAlpha(PALETTE[f], (float) ai), (float) (cz + 0.001)));
            }
        }
        out.sort(Comparator.comparingDouble(Quad::depth));
        return out;
    }

    private static Quad toQuad(double[][] pts, double cx, double cy, double cz, double k, int color, float depth) {
        float[] xs = new float[4], ys = new float[4];
        for (int i = 0; i < 4; i++) {
            double x = cx + (pts[i][0] - cx) * k, y = cy + (pts[i][1] - cy) * k, z = cz + (pts[i][2] - cz) * k;
            double d = CAM / (CAM - z);
            xs[i] = (float) (x * d);
            ys[i] = (float) (-y * d);
        }
        return new Quad(xs, ys, depth, color);
    }
}
