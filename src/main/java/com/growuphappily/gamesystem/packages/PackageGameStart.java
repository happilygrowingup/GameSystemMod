package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.enums.EnumGameMode;
import com.growuphappily.gamesystem.gui.HUDSelectSkill;
import com.growuphappily.gamesystem.system.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PackageGameStart {
    public boolean message;
    public PackageGameStart(FriendlyByteBuf buf){
        message = buf.readBoolean();
    }

    public PackageGameStart(boolean message){
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(message);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            Game.clientIsStarted = message;
            if(!Game.clientIsStarted){
                HUDSelectSkill.skills = new ArrayList<>();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
