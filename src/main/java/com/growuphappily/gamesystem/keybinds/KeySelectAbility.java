package com.growuphappily.gamesystem.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeySelectAbility {
    public static final KeyMapping KEY = new KeyMapping("key.message", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.category.ability");
    public static boolean isDown = false;
    @SubscribeEvent
    public static void onKeyReg(FMLClientSetupEvent event){
        ClientRegistry.registerKeyBinding(KEY);
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event){
        isDown = KEY.isDown();
    }
}
