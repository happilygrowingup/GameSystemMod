package com.growuphappily.gamesystem.gui;

import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageRule;
import com.growuphappily.gamesystem.rules.RuleBloodFeast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class GUISelectRule extends Screen {
    public Button bloodFeast;
    public GUISelectRule(Component p_96550_) {
        super(p_96550_);
    }

    @Override
    protected void init() {
        assert minecraft != null;
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
        bloodFeast = new Button(width/2 - 25, height/2 - 10, 50, 20, new TextComponent("Blood Feast"), (button) -> {
            Networking.INSTANCE.sendToServer(new PackageRule(RuleBloodFeast.ID));
            minecraft.setScreen(null);
        });
        addRenderableWidget(bloodFeast);
    }

    @Override
    public void render(PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        renderBackground(p_96562_);
        drawCenteredString(p_96562_, font, "Select game rules..", width/2, 50, 0xFFFFFF);
        super.render(p_96562_, p_96563_, p_96564_, p_96565_);
    }
}
