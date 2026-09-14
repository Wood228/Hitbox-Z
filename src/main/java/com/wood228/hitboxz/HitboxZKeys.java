package com.wood228.hitboxz;

import com.wood228.hitboxz.config.HitboxConfig;
import com.wood228.hitboxz.gui.HitboxConfigScreen;
import com.wood228.hitboxz.hud.HitboxHudConfig;
import com.wood228.hitboxz.hud.HitboxHudEditorScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class HitboxZKeys {
    private static final String CATEGORY = "key.category.hitboxz";
    private static KeyBinding toggleKey;
    private static KeyBinding settingsKey;
    private static KeyBinding hudKey;

    private HitboxZKeys() {}

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.hitboxz.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F6, CATEGORY));
        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.hitboxz.settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, CATEGORY));
        hudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.hitboxz.hud", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                HitboxConfig config = HitboxConfig.get();
                config.enabled = !config.enabled;
                config.save();
                if (client.player != null) client.player.sendMessage(Text.literal("Hitbox-Z: " + (config.enabled ? "ON" : "OFF")), true);
            }
            while (settingsKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new HitboxConfigScreen(null));
            }
            while (hudKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new HitboxHudEditorScreen(null));
            }
        });
    }
}
