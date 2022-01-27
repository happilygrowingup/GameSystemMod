package com.growuphappily.gamesystem.gui;

import com.growuphappily.gamesystem.system.Attributes;
import com.growuphappily.gamesystem.system.Game;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class HUDAttributes extends Gui {
    public Attributes attr;
    public HUDAttributes(Minecraft p_93005_) {
        super(p_93005_);
        assert Minecraft.getInstance().player != null;
        attr = Game.instance.searchPlayerByName(Minecraft.getInstance().player.getDisplayName().getString()).attributes;
    }

    public void render() {
        drawString(new PoseStack(),getFont(),"Tire(Surgical):" + attr.surgical, screenWidth/2, 10, 0xFFFFFF);
    }

    @SubscribeEvent
    public static void onHUDRender(RenderGameOverlayEvent event){
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        if (Minecraft.getInstance().player == null) {
            return;
        }
        if(!Game.isStarted){
            return;
        }
        new HUDAttributes(Minecraft.getInstance()).render();
    }
}
