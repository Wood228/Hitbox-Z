package com.wood228.hitboxz.hud;

import com.wood228.hitboxz.config.HitboxConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public final class HitboxHud {
    private static final int PANEL = 0xB9151822;
    private static final int PANEL_SOFT = 0xA510131C;
    private static final int ACCENT = 0xFF8B5CF6;
    private static final int TEXT = 0xFFF3F4F6;
    private static final int MUTED = 0xFF9CA3AF;
    private static long sessionStart;

    private HitboxHud() {}

    public static void register() {
        sessionStart = System.currentTimeMillis();
        HudRenderCallback.EVENT.register(HitboxHud::render);
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        HitboxHudConfig c = HitboxHudConfig.get();
        MinecraftClient client = MinecraftClient.getInstance();
        if (!c.enabled || client.player == null) return;
        renderWatermark(context, c);
        renderStatus(context, c);
        renderKeybinds(context, c);
        renderSession(context, c);
    }

    public static void renderWatermark(DrawContext context, HitboxHudConfig c) {
        panel(context, c.watermarkX, c.watermarkY, 104, 24, PANEL);
        context.fill(c.watermarkX, c.watermarkY, c.watermarkX + 3, c.watermarkY + 24, ACCENT);
        text(context, "HITBOX-Z", c.watermarkX + 10, c.watermarkY + 7, TEXT);
    }

    public static void renderStatus(DrawContext context, HitboxHudConfig c) {
        boolean enabled = HitboxConfig.get().enabled;
        panel(context, c.statusX, c.statusY, 112, 24, PANEL);
        text(context, "HITBOX", c.statusX + 8, c.statusY + 7, MUTED);
        text(context, enabled ? "ENABLED" : "DISABLED", c.statusX + 58, c.statusY + 7, enabled ? ACCENT : MUTED);
    }

    public static void renderKeybinds(DrawContext context, HitboxHudConfig c) {
        panel(context, c.keybindsX, c.keybindsY, 150, 46, PANEL);
        text(context, "KEYBINDS", c.keybindsX + 8, c.keybindsY + 6, TEXT);
        badge(context, c.keybindsX + 8, c.keybindsY + 23, "F6", "Toggle");
        badge(context, c.keybindsX + 76, c.keybindsY + 23, "RSHIFT", "Settings");
    }

    public static void renderSession(DrawContext context, HitboxHudConfig c) {
        long seconds = Math.max(0L, (System.currentTimeMillis() - sessionStart) / 1000L);
        panel(context, c.sessionX, c.sessionY, 150, 24, PANEL_SOFT);
        text(context, "SESSION", c.sessionX + 8, c.sessionY + 7, MUTED);
        text(context, String.format("%02d:%02d", seconds / 60L, seconds % 60L), c.sessionX + 96, c.sessionY + 7, TEXT);
    }

    private static void badge(DrawContext context, int x, int y, String key, String label) {
        context.fill(x, y, x + 60, y + 16, 0xFF202533);
        text(context, key, x + 4, y + 4, TEXT);
        text(context, label, x + 64, y + 4, MUTED);
    }

    private static void panel(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
        context.fill(x, y, x + width, y + 1, 0xFF2B3140);
    }

    private static void text(DrawContext context, String value, int x, int y, int color) {
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal(value), x, y, color);
    }
}
