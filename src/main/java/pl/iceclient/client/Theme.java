package pl.iceclient.client;

import pl.iceclient.core.IceConfig;

/** Paleta IceClient; kolor akcentu zależy od ustawienia gracza. */
public final class Theme {
    private Theme() {}

    public static int accent() {
        return switch (IceConfig.get().accent) {
            case "aurora" -> 0xFF3DDCC0;
            case "frost" -> 0xFF8B9CFF;
            default -> 0xFF4CC3FF;
        };
    }

    public static String accentLabel(String id) {
        return switch (id) {
            case "aurora" -> "Zorza";
            case "frost" -> "Szron";
            default -> "Lód";
        };
    }
}
