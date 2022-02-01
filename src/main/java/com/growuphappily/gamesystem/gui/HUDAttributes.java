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

import java.util.ArrayList;
import java.util.Date;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class HUDAttributes extends Gui {
    public static Attributes attr = new Attributes(0,0,0,0,0,0,0,0);
    public static boolean isAttackInCold = false;
    public static int color = 0x0000FF;
    public static float r = 0;
    public static float g = 0;
    public static float b = 255;
    public static float deltaTime;
    public static ArrayList<String> specialState = new ArrayList<>();
    private enum Color{
        R(),
        G(),
        B()
    }
    public long lastTime = 0;
    public static Color currentColor = Color.R;
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
        if(!specialState.isEmpty()){
            int h = 80;
            for (int i = 0; i < specialState.size(); i++) {
                h += 10;
                drawString(new PoseStack(),getFont(),specialState.get(i) + attr.knowledge, screenWidth/2, h, color);
            }
        }
        if(currentColor == Color.R){
            b -= 5;
            r += 5;
            if(r>=255){
                b = 0;
                currentColor = Color.G;
            }
        }
        if(currentColor == Color.G){
            r -= 5;
            g += 5;
            if(g>=255){
                r = 0;
                currentColor = Color.B;
            }
        }
        if(currentColor == Color.B){
            g -= 5;
            b += 5;
            if(b>=255){
                g = 0;
                currentColor = Color.R;
            }
        }
        //LogManager.getLogger().info(r);
        //LogManager.getLogger().info(g);
        //LogManager.getLogger().info(b);
        //color = (int) Math.floor((0xFF0000*Math.abs(Math.sin(time/100000.))) + (0xFF00*Math.abs(Math.cos(time/100000.))));
        color = getColor(r,g,b);
        if(isAttackInCold){
            drawCenteredString(new PoseStack(), getFont(), "Colding!", screenWidth/2, screenHeight/2 ,0xFF0000);
        }

        lastTime = new Date().getTime();
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
    public static int getColor(float r, float g, float b){
        return (int)Math.min(r,255)*0x10000 + (int)Math.min(g,255)*0x100 + (int)Math.min(b,255);
    }
}
