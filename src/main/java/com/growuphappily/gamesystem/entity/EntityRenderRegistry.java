package com.growuphappily.gamesystem.entity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRenderRegistry {
    @SubscribeEvent
    public static void onRenderersRegister(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityTypeRegistry.shooter.get(), ShooterRender::new);
        event.registerEntityRenderer(EntityTypeRegistry.moonGas.get(), MoonGasRender::new);
    }
    @SubscribeEvent
    public static void onyerRegister(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModelShooter.LAYER_LOCATION, ModelShooter::createBodyLayer);
        event.registerLayerDefinition(ModelMoonGas.LAYER_LOCATION, ModelMoonGas::createBodyLayer);
    }
}
