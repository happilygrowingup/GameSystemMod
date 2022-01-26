package com.growuphappily.gamesystem.packages;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkingRegistry {
    @SubscribeEvent
    public static void onCommon(FMLCommonSetupEvent event){
        Networking.registerMessage();
    }
}
