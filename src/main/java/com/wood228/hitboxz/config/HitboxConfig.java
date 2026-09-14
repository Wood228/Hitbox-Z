package com.wood228.hitboxz.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class HitboxConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Path.of("config", "hitbox-z.json");
    private static final HitboxConfig INSTANCE = new HitboxConfig();

    public boolean enabled = true;
    public double width = 1.0D;
    public double height = 1.0D;
    public double lineThickness = 2.0D;
    public int red = 80;
    public int green = 180;
    public int blue = 255;
    public int alpha = 255;

    private HitboxConfig() {}

    public static HitboxConfig get() { return INSTANCE; }

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                HitboxConfig loaded = GSON.fromJson(Files.readString(PATH), HitboxConfig.class);
                if (loaded != null) INSTANCE.copyFrom(loaded);
            }
        } catch (IOException | RuntimeException ignored) {
        }
        INSTANCE.clamp();
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(this));
        } catch (IOException ignored) {
        }
    }

    public void clamp() {
        width = clamp(width, 0.25D, 2.0D);
        height = clamp(height, 0.25D, 2.0D);
        lineThickness = clamp(lineThickness, 1.0D, 5.0D);
        red = clamp(red, 0, 255);
        green = clamp(green, 0, 255);
        blue = clamp(blue, 0, 255);
        alpha = clamp(alpha, 25, 255);
    }

    private void copyFrom(HitboxConfig other) {
        enabled = other.enabled;
        width = other.width;
        height = other.height;
        lineThickness = other.lineThickness;
        red = other.red;
        green = other.green;
        blue = other.blue;
        alpha = other.alpha;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
