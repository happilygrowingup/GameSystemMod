package com.growuphappily.gamesystem.util;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

public class Util {
    @OnlyIn(Dist.CLIENT)
    public static ServerPlayer getServerPlayerByNameAtClient(){
        assert Minecraft.getInstance().player != null;
        return Objects.requireNonNull(Minecraft.getInstance().player.getServer()).getPlayerList().getPlayerByName(Minecraft.getInstance().player.getDisplayName().getString());
    }
}
