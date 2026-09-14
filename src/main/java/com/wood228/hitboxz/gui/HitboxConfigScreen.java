package com.wood228.hitboxz.gui;

import com.wood228.hitboxz.config.HitboxConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public final class HitboxConfigScreen extends Screen {
    private static final int PANEL_WIDTH = 420;
    private static final int PANEL_HEIGHT = 360;
    private static final int BLUE = 0xFF2F80ED;
    private static final int LIGHT_BLUE = 0xFFEAF4FF;
    private static final int PANEL = 0xFFF8FBFF;
    private static final int TEXT = 0xFF18324B;
    private static final int MUTED = 0xFF6D8295;

    private final Screen parent;
    private final HitboxConfig config = HitboxConfig.get();
    private ButtonWidget enabledButton;

    public HitboxConfigScreen(Screen parent) {
        super(Text.literal("Hitbox-Z Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = (this.width - PANEL_WIDTH) / 2;
        int top = (this.height - PANEL_HEIGHT) / 2;

        enabledButton = ButtonWidget.builder(enabledText(), button -> {
            config.enabled = !config.enabled;
            button.setMessage(enabledText());
            config.save();
        }).dimensions(left + 24, top + 66, 372, 24).build();
        addDrawableChild(enabledButton);

        addDrawableChild(slider("Width", left + 24, top + 112, config.width, 0.25D, 2.0D,
                value -> config.width = value, "%.2fx"));
        addDrawableChild(slider("Height", left + 24, top + 154, config.height, 0.25D, 2.0D,
                value -> config.height = value, "%.2fx"));
        addDrawableChild(slider("Line thickness", left + 24, top + 196, config.lineThickness, 1.0D, 5.0D,
                value -> config.lineThickness = value, "%.1f px"));

        addDrawableChild(slider("Red", left + 24, top + 238, config.red, 0, 255,
                value -> config.red = (int) Math.round(value), "%d"));
        addDrawableChild(slider("Green", left + 24, top + 280, config.green, 0, 255,
                value -> config.green = (int) Math.round(value), "%d"));
        addDrawableChild(slider("Blue", left + 24, top + 322, config.blue, 0, 255,
                value -> config.blue = (int) Math.round(value), "%d"));

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
                .dimensions(left + 288, top + PANEL_HEIGHT - 34, 108, 24).build());
    }

    private SliderWidget slider(String label, int x, int y, double initial, double min, double max,
                                java.util.function.DoubleConsumer setter, String format) {
        return new SliderWidget(x, y, 372, 24, Text.literal(label), normalize(initial, min, max)) {
            @Override
            protected void updateMessage() {
                double value = denormalize(this.value, min, max);
                String formatted = format.equals("%d") ? String.format("%d", Math.round(value)) : String.format(format, value);
                setMessage(Text.literal(label + ": " + formatted));
            }

            @Override
            protected void applyValue() {
                double value = denormalize(this.value, min, max);
                setter.accept(value);
                config.clamp();
            }
        };
    }

    private static double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    private static double denormalize(double value, double min, double max) {
        return min + value * (max - min);
    }

    private Text enabledText() {
        return Text.literal("Hitboxes: " + (config.enabled ? "ON" : "OFF"));
    }

    @Override
    public void close() {
        config.clamp();
        config.save();
        if (client != null) client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;

        context.fill(0, 0, width, height, 0xAA071523);
        context.fill(left - 2, top - 2, left + PANEL_WIDTH + 2, top + PANEL_HEIGHT + 2, BLUE);
        context.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, PANEL);
        context.fill(left, top, left + PANEL_WIDTH, top + 52, BLUE);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("HITBOX-Z"), width / 2, top + 12, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Visual hitbox controls"), width / 2, top + 29, 0xFFDCEEFF);

        context.drawTextWithShadow(textRenderer, Text.literal("Appearance"), left + 24, top + 98, TEXT);
        context.drawTextWithShadow(textRenderer, Text.literal("RGB color"), left + 24, top + 224, MUTED);

        int preview = (config.alpha << 24) | (config.red << 16) | (config.green << 8) | config.blue;
        context.fill(left + 332, top + 224, left + 396, top + 230, preview);

        super.render(context, mouseX, mouseY, delta);
    }
}
