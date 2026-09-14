package com.wood228.hitboxz.render;

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
    private static final float RED = 1.0F;
    private static final float GREEN = 0.25F;
    private static final float BLUE = 0.25F;
    private static final float ALPHA = 1.0F;

    private HitboxRenderer() {
    }

    public static void register() {
        WorldRenderEvents.AFTER_ENTITIES.register(HitboxRenderer::render);
    }

    private static void render(WorldRenderContext context) {
        MatrixStack matrices = context.matrixStack();
        VertexConsumerProvider consumers = context.consumers();

        if (matrices == null || consumers == null || context.world() == null) {
            return;
        }

        Vec3d cameraPos = context.camera().getPos();
        Box searchBox = new Box(cameraPos, cameraPos).expand(RENDER_DISTANCE);
        List<Entity> entities = context.world().getEntitiesByClass(Entity.class, searchBox, entity -> !entity.isSpectator());
        VertexConsumer vertices = consumers.getBuffer(RenderLayer.LINES);

        for (Entity entity : entities) {
            drawEntityHitbox(matrices, vertices, entity, cameraPos);
        }
    }

    private static void drawEntityHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, Vec3d cameraPos) {
        Box box = entity.getBoundingBox();

        double minX = box.minX - cameraPos.x;
        double minY = box.minY - cameraPos.y;
        double minZ = box.minZ - cameraPos.z;
        double maxX = box.maxX - cameraPos.x;
        double maxY = box.maxY - cameraPos.y;
        double maxZ = box.maxZ - cameraPos.z;

        Box relativeBox = new Box(minX, minY, minZ, maxX, maxY, maxZ);
        net.minecraft.client.render.WorldRenderer.drawBox(
                matrices,
                vertices,
                relativeBox,
                RED,
                GREEN,
                BLUE,
                ALPHA
        );
    }
}
