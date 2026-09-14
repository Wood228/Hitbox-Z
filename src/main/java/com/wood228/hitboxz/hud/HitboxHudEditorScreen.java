package com.wood228.hitboxz.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class HitboxHudEditorScreen extends Screen {
    private enum Element { WATERMARK, STATUS, KEYBINDS, SESSION }

    private Element dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    public HitboxHudEditorScreen(Screen parent) {
        super(Text.literal("Hitbox-Z HUD Editor"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x55000000);
        HitboxHudConfig c = HitboxHudConfig.get();
        HitboxHud.renderWatermark(context, c);
        HitboxHud.renderStatus(context, c);
        HitboxHud.renderKeybinds(context, c);
        HitboxHud.renderSession(context, c);

        context.fill(8, height - 28, 230, height - 8, 0xCC11151F);
        context.drawTextWithShadow(textRenderer, Text.literal("HUD EDITOR  •  Drag elements  •  ESC to save"), 14, height - 22, 0xFFE5E7EB);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            Element hit = hitElement((int) mouseX, (int) mouseY);
            if (hit != null) {
                dragging = hit;
                HitboxHudConfig c = HitboxHudConfig.get();
                dragOffsetX = (int) mouseX - getX(hit, c);
                dragOffsetY = (int) mouseY - getY(hit, c);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging != null && button == 0) {
            HitboxHudConfig c = HitboxHudConfig.get();
            setPosition(dragging, (int) mouseX - dragOffsetX, (int) mouseY - dragOffsetY, c);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging != null) {
            HitboxHudConfig.get().save();
            dragging = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        HitboxHudConfig.get().save();
        super.close();
    }

    private Element hitElement(int x, int y) {
        HitboxHudConfig c = HitboxHudConfig.get();
        if (inside(x, y, c.watermarkX, c.watermarkY, 104, 24)) return Element.WATERMARK;
        if (inside(x, y, c.statusX, c.statusY, 112, 24)) return Element.STATUS;
        if (inside(x, y, c.keybindsX, c.keybindsY, 150, 46)) return Element.KEYBINDS;
        if (inside(x, y, c.sessionX, c.sessionY, 150, 24)) return Element.SESSION;
        return null;
    }

    private static boolean inside(int x, int y, int ex, int ey, int w, int h) {
        return x >= ex && x <= ex + w && y >= ey && y <= ey + h;
    }

    private static int getX(Element e, HitboxHudConfig c) {
        return switch (e) {
            case WATERMARK -> c.watermarkX;
            case STATUS -> c.statusX;
            case KEYBINDS -> c.keybindsX;
            case SESSION -> c.sessionX;
        };
    }

    private static int getY(Element e, HitboxHudConfig c) {
        return switch (e) {
            case WATERMARK -> c.watermarkY;
            case STATUS -> c.statusY;
            case KEYBINDS -> c.keybindsY;
            case SESSION -> c.sessionY;
        };
    }

    private void setPosition(Element e, int x, int y, HitboxHudConfig c) {
        int maxX = Math.max(0, width - 160);
        int maxY = Math.max(0, height - 60);
        x = Math.max(0, Math.min(maxX, x));
        y = Math.max(0, Math.min(maxY, y));
        switch (e) {
            case WATERMARK -> { c.watermarkX = x; c.watermarkY = y; }
            case STATUS -> { c.statusX = x; c.statusY = y; }
            case KEYBINDS -> { c.keybindsX = x; c.keybindsY = y; }
            case SESSION -> { c.sessionX = x; c.sessionY = y; }
        }
    }
}
