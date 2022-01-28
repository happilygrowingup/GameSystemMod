package com.growuphappily.gamesystem.gui;

import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageEvil;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import com.growuphappily.gamesystem.util.Util;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class GUISelectEvil extends Screen {
    Button buttonEternalHunter;
    protected GUISelectEvil(Component p_96550_) {
        super(p_96550_);
    }

    @Override
    protected void init() {
        buttonEternalHunter = new Button(width/2 - 10, height/2 - 5, 20, 10,new TextComponent("EternalHunter"), (button) -> {
            Game.instance.addEvil(Objects.requireNonNull(GamePlayer.getEvilWithID(0, Util.getServerPlayerByNameAtClient())));
            Networking.INSTANCE.sendToServer(new PackageEvil(0));
            Minecraft.getInstance().setScreen(null);
        });
    }

    @Override
    public void render(@NotNull PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        renderBackground(p_96562_);
        drawCenteredString(p_96562_, font, "Select your evil..", width/2, 50, 0xFFFFFF);
        buttonEternalHunter.render(p_96562_, p_96563_, p_96564_, p_96565_);
    }
}
