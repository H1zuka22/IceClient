package pl.iceclient.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class IceConfigTest {
    @Test
    void saveAndLoadRoundTrip() throws IOException {
        Path dir = Files.createTempDirectory("icecfg");
        Path f = dir.resolve("sub").resolve("iceclient.properties");
        IceConfig c = new IceConfig();
        c.accent = "aurora";
        c.snow = false;
        c.animations = false;
        c.save(f);
        IceConfig l = IceConfig.load(f);
        assertEquals("aurora", l.accent);
        assertFalse(l.snow);
        assertFalse(l.animations);
        assertTrue(l.transitions);
    }

    @Test
    void missingFileGivesDefaults() throws IOException {
        IceConfig l = IceConfig.load(Files.createTempDirectory("icecfg").resolve("nie-ma.properties"));
        assertEquals("ice", l.accent);
        assertTrue(l.snow && l.animations && l.transitions);
    }

    @Test
    void invalidValuesFallBackToDefaults() throws IOException {
        Path f = Files.createTempFile("icecfg", ".properties");
        Files.writeString(f, "accent=neon\nsnow=maybe\nanimations=FALSE\n");
        IceConfig l = IceConfig.load(f);
        assertEquals("ice", l.accent);
        assertTrue(l.snow);
        assertFalse(l.animations);
    }

    @Test
    void corruptFileGivesDefaults() throws IOException {
        Path f = Files.createTempFile("icecfg", ".properties");
        Files.writeString(f, "accent=\\uZZZZ\n");
        assertEquals("ice", IceConfig.load(f).accent);
    }

    @Test
    void nextAccentCyclesThroughAllAccents() {
        IceConfig c = new IceConfig();
        assertEquals("aurora", c.nextAccent());
        c.accent = "frost";
        assertEquals("ice", c.nextAccent());
    }
}
