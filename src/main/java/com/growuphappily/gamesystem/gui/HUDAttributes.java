package com.growuphappily.gamesystem.gui;

import com.growuphappily.gamesystem.system.Attributes;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Date;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class HUDAttributes extends Gui {
    public static Attributes attr = new Attributes(0,0,0,0,0,0,0,0);
    public static boolean isAttackInCold = false;
    public static int color = 0x0000FF;
    public static int delta = 0X000001;
    public long startTime;
    public HUDAttributes(Minecraft p_93005_) {
        super(p_93005_);
        assert Minecraft.getInstance().player != null;
    }

    public void render() {
        drawString(new PoseStack(),getFont(),"Tire(Surgical):" + attr.surgical, screenWidth/2, 10, color);
        drawString(new PoseStack(),getFont(),"speed:" + attr.speed, screenWidth/2, 20, color);
        drawString(new PoseStack(),getFont(),"health:" + attr.health, screenWidth/2, 30, color);
        drawString(new PoseStack(),getFont(),"strength:" + attr.strength, screenWidth/2, 40, color);
        drawString(new PoseStack(),getFont(),"defence:" + attr.defence, screenWidth/2, 50, color);
        drawString(new PoseStack(),getFont(),"mental:" + attr.mental, screenWidth/2, 60, color);
        drawString(new PoseStack(),getFont(),"IQ:" + attr.IQ, screenWidth/2, 70, color);
        drawString(new PoseStack(),getFont(),"knowledge:" + attr.knowledge, screenWidth/2, 80, color);
        color = (int) (0xFFFFFF/4*Math.sin(new Date().getTime()/10000.0)) + 0xFFFFFF/2;
        if(isAttackInCold){
            drawCenteredString(new PoseStack(), getFont(), "Colding!", screenWidth/2, screenHeight/2 ,0xFF0000);
        }
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
