package com.growuphappily.gamesystem.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ShooterRender extends EntityRenderer<EntityShooter> {
    public EntityModel<EntityShooter> modelShooter;
    protected ShooterRender(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        modelShooter = new ModelShooter(p_174008_.bakeLayer(ModelShooter.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityShooter p_114482_) {
        return new ResourceLocation("gamesystem", "textures/entity/shooter.png");
    }

    @Override
    public void render(@NotNull EntityShooter p_114485_, float p_114486_, float p_114487_, @NotNull PoseStack p_114488_, @NotNull MultiBufferSource p_114489_, int p_114490_) {
        super.render(p_114485_, p_114486_, p_114487_, p_114488_, p_114489_, p_114490_);
        modelShooter.renderToBuffer(p_114488_, p_114489_.getBuffer(modelShooter.renderType(getTextureLocation(p_114485_))), p_114490_, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
    }
}
