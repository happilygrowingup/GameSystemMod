package com.growuphappily.gamesystem.gui;

import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageAttribute;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GUISelectAttribute extends Screen {
    private final Logger logger = LogManager.getLogger();
    public int points = 250;
    private int speed;
    private int health;
    private int strength;
    private int defence;
    private int mental;
    private int IQ;
    private int knowledge;
    private int surgical;
    private Button Bspeed;
    private Button Bhealth;
    private Button Bstrength;
    private Button Bdefence;
    private Button Bmental;
    private Button BIQ;
    private Button Bknowledge;
    private Button Bsurgical;

    public GUISelectAttribute(Component p_96550_) {
        super(p_96550_);
    }

    @Override
    protected void init() {
        assert minecraft != null;
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
        Bspeed = new Button(width / 2 - 15, height/2 - 35, 10, 10, new TextComponent("+"), (button) -> {
            if(speed >= 60){speed=60;return;}
            speed += 10;
            points -= 10;
        });
        Bhealth = new Button(width / 2 - 15, height/2 - 25, 10, 10, new TextComponent("+"), (button) -> {
            if(health >= 60){health=60;return;}
            health += 10;
            points -= 10;
        });
        Bstrength = new Button(width / 2 - 15, height/2 - 15, 10, 10, new TextComponent("+"), (button) -> {
            if(strength >= 60){strength=60;return;}
            strength += 10;
            points -= 10;
        });
        Bdefence = new Button(width / 2 - 15, height/2 - 5, 10, 10, new TextComponent("+"), (button) -> {
            if(defence >= 60){defence=60;return;}
            defence += 10;
            points -= 10;
        });
        Bmental = new Button(width / 2 - 15, height/2 + 5, 10, 10, new TextComponent("+"), (button) -> {
            if(mental >= 60){mental=60;return;}
            mental += 10;
            points -= 10;
        });
        BIQ = new Button(width / 2 - 15, height/2 + 15, 10, 10, new TextComponent("+"), (button) -> {
            if(IQ >= 60){IQ=60;return;}
            IQ += 10;
            points -= 10;
        });
        Bknowledge = new Button(width / 2 - 15, height/2 + 25, 10, 10, new TextComponent("+"), (button) -> {
            if(knowledge >= 60){knowledge=60;return;}
            knowledge += 10;
            points -= 10;
        });
        super.init();
        addRenderableWidget(Bspeed);
        addRenderableWidget(Bhealth);
        addRenderableWidget(Bstrength);
        addRenderableWidget(Bdefence);
        addRenderableWidget(Bmental);
        addRenderableWidget(BIQ);
        addRenderableWidget(Bknowledge);
    }

    @Override
    public void render(@NotNull PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        this.renderBackground(p_96562_);
        drawString(p_96562_, this.font, "You have " + points + " points left.", width / 2 - 60, 50, 0xFFFFFF);
        drawString(p_96562_, this.font, "speed:" + speed, width / 2 + 20, height/2 - 35, 0xFFFFFF);
        drawString(p_96562_, this.font, "health:" + health, width / 2 + 20, height/2 - 25, 0xFFFFFF);
        drawString(p_96562_, this.font, "strength:" + strength, width / 2 + 20, height/2 - 15, 0xFFFFFF);
        drawString(p_96562_, this.font, "defence:" + defence, width / 2 + 20, height/2 - 5, 0xFFFFFF);
        drawString(p_96562_, this.font, "mental:" + mental, width / 2 + 20, height/2 + 5, 0xFFFFFF);
        drawString(p_96562_, this.font, "IQ:" + IQ, width / 2 + 20, height/2 + 15, 0xFFFFFF);
        drawString(p_96562_, this.font, "knowledge:" + knowledge, width / 2 + 20, height/2 + 25, 0xFFFFFF);
        Bspeed.render(p_96562_, p_96563_, p_96564_, p_96565_);
        Bhealth.render(p_96562_, p_96563_, p_96564_, p_96565_);
        Bstrength.render(p_96562_, p_96563_, p_96564_, p_96565_);
        Bdefence.render(p_96562_, p_96563_, p_96564_, p_96565_);
        Bmental.render(p_96562_, p_96563_, p_96564_, p_96565_);
        BIQ.render(p_96562_, p_96563_, p_96564_, p_96565_);
        Bknowledge.render(p_96562_, p_96563_, p_96564_, p_96565_);
        super.render(p_96562_, p_96563_, p_96564_, p_96565_);
        if(points <= 0){
            endSelecting();
        }
    }

    public void endSelecting(){
        Minecraft.getInstance().setScreen(new GUISelectSkill(new TextComponent("Select your skill..")));
        assert Minecraft.getInstance().player != null;
        Networking.INSTANCE.sendToServer(new PackageAttribute(
                speed + "." +
                health + "." +
                strength + "." +
                defence + "." +
                mental + "." +
                IQ + "." +
                knowledge + "." +
                surgical + "."
        ));
    }
}

