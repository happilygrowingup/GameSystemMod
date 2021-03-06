package com.growuphappily.gamesystem.keybinds;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.growuphappily.gamesystem.keybinds.KeySelectAbility.KEY;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus= Mod.EventBusSubscriber.Bus.MOD)
public class KeyReg {
    @SubscribeEvent
    public static void onKeyReg(FMLClientSetupEvent event){
        ClientRegistry.registerKeyBinding(KEY);
    }
}
