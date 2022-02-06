package com.growuphappily.gamesystem.gui;

import com.growuphappily.gamesystem.keybinds.KeySelectAbility;
import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageSkills;
import com.growuphappily.gamesystem.system.Game;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;

import java.util.ArrayList;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class HUDSelectSkill extends Gui {
    public static ArrayList<String> skills= new ArrayList<>();
    public static Integer selectIndex;

    public HUDSelectSkill(Minecraft p_93005_) {
        super(p_93005_);
    }

    public void render() {
        drawCenteredString(new PoseStack(),getFont(),new TextComponent("Select Skill to cast ..."),Minecraft.getInstance().getWindow().getGuiScaledWidth()/2,40,0xFFFFFF);
        for (int i = 0; i < skills.size(); i++) {
            drawCenteredString(new PoseStack(),getFont(),new TranslatableComponent(skills.get(i)),Minecraft.getInstance().getWindow().getGuiScaledWidth()/2,50 + 10*i, Objects.equals(i, selectIndex) ? 0xFF0000 : 0xFFFFFF);
        }
        drawCenteredString(new PoseStack(),getFont(),new TextComponent("Surgical(Tire): " + HUDAttributes.attr.surgical),Minecraft.getInstance().getWindow().getGuiScaledWidth()/2,50 + 10*skills.size(),0xFFFFFF);
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
        if(!KeySelectAbility.isDown && selectIndex != null){
            try {
                Networking.INSTANCE.sendToServer(new PackageSkills(skills.get(selectIndex)));
            }catch(IndexOutOfBoundsException ignored){}
            selectIndex = null;
            return;
        }
        if(!KeySelectAbility.isDown){
            return;
        }
        new HUDSelectSkill(Minecraft.getInstance()).render();
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollEvent event){
        if(KeySelectAbility.isDown){
            event.setCanceled(true);
            double delta = -event.getScrollDelta();
            LogManager.getLogger().info(delta);
            if(selectIndex == null){
                selectIndex = 0;
            }
            if(selectIndex + delta >= skills.size()){
                selectIndex = skills.size() -1;
            }
            else if(selectIndex < 0){
                selectIndex = 0;
            }else{
                selectIndex += (int)delta;
            }
        }
    }
}
