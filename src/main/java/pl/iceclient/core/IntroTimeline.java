package pl.iceclient.core;

/**
 * Oś czasu intra IceClient (czysta matematyka). Czas t to milisekundy od startu zegara IceClock:
 * najpierw kostki zlatują się w kostkę, potem wjeżdżają litery logo i przechodzi błysk,
 * a kostka zaczyna się mieszać i układać (RubikCube.LEAD_MS).
 */
public final class IntroTimeline {
    public static final String TITLE = "ICE CLIENT";
    public static final long ASSEMBLE_START_MS = 150, ASSEMBLE_MS = 1400;
    public static final long LETTER_START = 900, LETTER_STEP = 70, LETTER_MS = 420;
    public static final long SWEEP_START = 1900, SWEEP_MS = 900;

    private IntroTimeline() {}

    /** Postęp składania kostki z rozproszonych kostek (0..1). */
    public static double assemble(long t) {
        return Ease.clamp01((t - ASSEMBLE_START_MS) / (double) ASSEMBLE_MS);
    }

    /** Postęp pojawiania się i-tej litery (0..1). */
    public static double letter(int i, long t) {
        return Ease.outCubic((t - LETTER_START - LETTER_STEP * i) / (double) LETTER_MS);
    }

    public static boolean sweepActive(long t) {
        return t >= SWEEP_START && t <= SWEEP_START + SWEEP_MS;
    }

    /** Położenie błysku 0..1 wzdłuż napisu. */
    public static double sweep(long t) {
        return Ease.clamp01((t - SWEEP_START) / (double) SWEEP_MS);
    }

    /** Delikatne pulsowanie poświaty 0..1. */
    public static double glow(long t) {
        return 0.5 + 0.5 * Math.sin(t * 0.004);
    }

    /** Czas obrotów kostki: ujemny, dopóki kostka się składa (zgodnie z IceClock.cubeTime). */
    public static long cubeTime(long t) {
        return t - RubikCube.LEAD_MS;
    }
}
