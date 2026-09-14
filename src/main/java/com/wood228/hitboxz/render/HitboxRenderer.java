package com.wood228.hitboxz.render;

import com.wood228.hitboxz.config.HitboxConfig;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public final class HitboxRenderer {
    private static final double RENDER_DISTANCE = 128.0D;

    private HitboxRenderer() {}

    public static void register() {
        WorldRenderEvents.AFTER_ENTITIES.register(HitboxRenderer::render);
    }

    private static void render(WorldRenderContext context) {
        HitboxConfig config = HitboxConfig.get();
        if (!config.enabled) return;

        MatrixStack matrices = context.matrixStack();
        VertexConsumerProvider consumers = context.consumers();
        if (matrices == null || consumers == null || context.world() == null) return;

        Vec3d cameraPos = context.camera().getPos();
        Box searchBox = new Box(cameraPos, cameraPos).expand(RENDER_DISTANCE);
        List<Entity> entities = context.world().getEntitiesByClass(
                Entity.class,
                searchBox,
                entity -> !entity.isSpectator()
        );
        VertexConsumer vertices = consumers.getBuffer(RenderLayer.LINES);

        float red = config.red / 255.0F;
        float green = config.green / 255.0F;
        float blue = config.blue / 255.0F;
        float alpha = config.alpha / 255.0F;

        for (Entity entity : entities) {
            drawEntityHitbox(matrices, vertices, entity, cameraPos, config.width, config.height,
                    red, green, blue, alpha);
        }
    }

    private static void drawEntityHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity,
                                         Vec3d cameraPos, double widthScale, double heightScale,
                                         float red, float green, float blue, float alpha) {
        Box box = entity.getBoundingBox();
        double centerX = (box.minX + box.maxX) * 0.5D;
        double centerZ = (box.minZ + box.maxZ) * 0.5D;
        double scaledHalfX = (box.maxX - box.minX) * widthScale * 0.5D;
        double scaledHalfZ = (box.maxZ - box.minZ) * widthScale * 0.5D;
        double scaledHeight = (box.maxY - box.minY) * heightScale;

        Box relativeBox = new Box(
                centerX - scaledHalfX - cameraPos.x,
                box.minY - cameraPos.y,
                centerZ - scaledHalfZ - cameraPos.z,
                centerX + scaledHalfX - cameraPos.x,
                box.minY + scaledHeight - cameraPos.y,
                centerZ + scaledHalfZ - cameraPos.z
        );

        net.minecraft.client.render.WorldRenderer.drawBox(
                matrices,
                vertices,
                relativeBox,
                red,
                green,
                blue,
                alpha
        );
    }
}
