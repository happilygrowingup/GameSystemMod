package com.growuphappily.gamesystem.gui;

import com.google.common.graph.Network;
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

import java.util.ArrayList;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class GUISelectAttribute extends Screen {
    private final Logger logger = LogManager.getLogger();
    public int points = 250;
    public ArrayList<Integer> attrs = new ArrayList<>();
    public static final String[] names = {"speed: ", "health: ", "strength: ", "defence: ", "mental: ", "IQ: ", "knowledge: "};
    public ArrayList<Button> buttons = new ArrayList<>();
    public GUISelectAttribute(Component p_96550_) {
        super(p_96550_);
    }
    public ArrayList<Integer> locks = new ArrayList<>();
    public boolean canLock = true;
    public int leftTime = 5;

    @Override
    protected void init() {
        for (int i = 0; i < 7; i++) {
            attrs.add(0);
            int finalI = i;
            Button b = new Button(width / 2 - 25, height/2 - 35 + 10*i, 40, 10, new TextComponent("Lock"), (button)->{
                if(!locks.contains(finalI) && canLock) {
                    locks.add(finalI);
                    button.setMessage(new TextComponent("Locked"));
                }
            });
            buttons.add(b);
            addRenderableWidget(b);
        }
        addRenderableWidget(new Button(width/2 - 50, height - 25, 100, 20, new TextComponent("Save"), (button) -> {
            endSelecting();
        }));
        addRenderableWidget(new Button(width/2 - 50, height - 50, 100, 20, new TextComponent("Re-generate"), (button) -> {
            genAttributes();
            canLock = false;
            for (Button b : buttons) {
                if(b.getMessage().getString().equals("Lock")){
                    b.visible = false;
                }
            }
            --leftTime;
            if(leftTime == 0){
                button.visible = false;
            }
        }));
        genAttributes();
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float p_96565_) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, p_96565_);
        drawString(stack, this.font, "Generated Attributes:(Left random times: " + leftTime + ")", width / 2 - 60, 50, 0xFFFFFF);
        for (int i = 0; i < 7; i++) {
            drawString(stack, this.font, names[i] + attrs.get(i), width / 2 + 20, height/2 - 35 + i*10, 0xFFFFFF);
        }
    }

    public void endSelecting(){
        Minecraft.getInstance().setScreen(new GUISelectSkill(new TextComponent("Select your skill..")));
        assert Minecraft.getInstance().player != null;
        StringBuilder msg = new StringBuilder();
        for (int i : attrs) {
            msg.append(i);
            msg.append(".");
        }
        Networking.INSTANCE.sendToServer(new PackageAttribute(msg.toString()));
    }

    public void genAttributes(){
        if(attrs.size() < 7){
            return;
        }
        int points = 250;
        for (int i : locks) {
            points -= attrs.get(i);
        }
        for (int i = 0; i < attrs.size(); i++) {
            if(locks.contains(i)){
                continue;
            }
            int r = new Random().nextInt(10, 60);
            points -= r;
            attrs.set(i, r);
            if(points < 0){
                genAttributes();
                return;
            }
        }
    }
}

