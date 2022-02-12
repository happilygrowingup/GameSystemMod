package com.growuphappily.gamesystem.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

public class MoonGasRender extends EntityRenderer<EntityMoonGas> {
    public EntityModel<EntityMoonGas> modelMoonGas;
    protected MoonGasRender(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        modelMoonGas = new ModelMoonGas<>(p_174008_.bakeLayer(ModelMoonGas.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityMoonGas p_114482_) {
        return new ResourceLocation("gamesystem", "textures/entity/moon_gas.png");
    }

    @Override
    public void render(EntityMoonGas entityMoonGas, float p_114486_, float p_114487_, PoseStack stack, MultiBufferSource p_114489_, int p_114490_) {
        super.render(entityMoonGas, p_114486_, p_114487_, stack, p_114489_, p_114490_);
        stack.pushPose();
        try {
            stack.mulPose(Vector3f.YN.rotationDegrees(entityMoonGas.getOwner().getYRot()));
        }catch(NullPointerException ignored){}
        //LogManager.getLogger().info(entityMoonGas.getYRot());
        modelMoonGas.renderToBuffer(stack, p_114489_.getBuffer(modelMoonGas.renderType(getTextureLocation(entityMoonGas))), p_114490_, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        stack.popPose();
    }
}
