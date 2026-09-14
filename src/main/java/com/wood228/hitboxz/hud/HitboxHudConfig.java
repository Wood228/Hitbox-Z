package com.wood228.hitboxz.hud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class HitboxHudConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Path.of("config", "hitbox-z-hud.json");
    private static final HitboxHudConfig INSTANCE = new HitboxHudConfig();

    public boolean enabled = true;
    public int watermarkX = 10;
    public int watermarkY = 10;
    public int statusX = 10;
    public int statusY = 38;
    public int keybindsX = 10;
    public int keybindsY = 66;
    public int sessionX = 10;
    public int sessionY = 116;

    private HitboxHudConfig() {}

    public static HitboxHudConfig get() {
        return INSTANCE;
    }

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                HitboxHudConfig loaded = GSON.fromJson(Files.readString(PATH), HitboxHudConfig.class);
                if (loaded != null) INSTANCE.copyFrom(loaded);
            }
        } catch (IOException | RuntimeException ignored) {
        }
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(this));
        } catch (IOException ignored) {
        }
    }

    private void copyFrom(HitboxHudConfig other) {
        enabled = other.enabled;
        watermarkX = other.watermarkX;
        watermarkY = other.watermarkY;
        statusX = other.statusX;
        statusY = other.statusY;
        keybindsX = other.keybindsX;
        keybindsY = other.keybindsY;
        sessionX = other.sessionX;
        sessionY = other.sessionY;
    }
}
