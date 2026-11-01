package pl.iceclient.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Properties;

/** Ustawienia IceClient zapisywane w pliku .properties (bez zewnętrznych bibliotek). */
public final class IceConfig {
    public static final List<String> ACCENTS = List.of("ice", "aurora", "frost");

    public String accent = "ice";
    public boolean animations = true;
    public boolean snow = true;
    public boolean transitions = true;

    private static IceConfig current = new IceConfig();
    private static Path file;

    public static IceConfig get() {
        return current;
    }

    public static void init(Path path) {
        file = path;
        current = load(path);
    }

    /** Wczytuje ustawienia; brak pliku, uszkodzona treść lub nieznane wartości dają bezpieczne domyślne. */
    public static IceConfig load(Path path) {
        IceConfig c = new IceConfig();
        if (path == null || !Files.isRegularFile(path)) return c;
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(path)) {
            p.load(in);
        } catch (IOException | IllegalArgumentException e) {
            return new IceConfig();
        }
        String a = p.getProperty("accent", "ice").trim();
        c.accent = ACCENTS.contains(a) ? a : "ice";
        c.animations = bool(p, "animations", true);
        c.snow = bool(p, "snow", true);
        c.transitions = bool(p, "transitions", true);
        return c;
    }

    private static boolean bool(Properties p, String key, boolean def) {
        String v = p.getProperty(key);
        if (v == null) return def;
        return switch (v.trim().toLowerCase()) {
            case "true" -> true;
            case "false" -> false;
            default -> def;
        };
    }

    public void save(Path path) throws IOException {
        Properties p = new Properties();
        p.setProperty("accent", accent);
        p.setProperty("animations", Boolean.toString(animations));
        p.setProperty("snow", Boolean.toString(snow));
        p.setProperty("transitions", Boolean.toString(transitions));
        Path dir = path.toAbsolutePath().getParent();
        if (dir != null) Files.createDirectories(dir);
        Path tmp = path.resolveSibling(path.getFileName() + ".tmp");
        try (OutputStream out = Files.newOutputStream(tmp)) {
            p.store(out, "IceClient");
        }
        try {
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** Zapis do pliku wskazanego w init(); błędy zapisu nie przerywają gry. */
    public void saveCurrent() {
        if (file == null) return;
        try {
            save(file);
        } catch (IOException ignored) {
            // ustawienia pozostają w pamięci do końca sesji
        }
    }

    public String nextAccent() {
        return ACCENTS.get((ACCENTS.indexOf(accent) + 1) % ACCENTS.size());
    }
}
