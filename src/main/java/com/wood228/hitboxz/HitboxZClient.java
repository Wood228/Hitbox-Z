package com.wood228.hitboxz;

import com.wood228.hitboxz.config.HitboxConfig;
import com.wood228.hitboxz.render.HitboxRenderer;
import net.fabricmc.api.ClientModInitializer;

public final class HitboxZClient implements ClientModInitializer {
    public static final String MOD_ID = "hitboxz";

    @Override
    public void onInitializeClient() {
        HitboxConfig.load();
        HitboxRenderer.register();
        HitboxZKeys.register();
    }
}
