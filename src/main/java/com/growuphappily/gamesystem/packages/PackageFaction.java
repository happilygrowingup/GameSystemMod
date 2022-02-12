package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.enums.EnumFactionCategory;
import com.growuphappily.gamesystem.enums.EnumGameMode;
import com.growuphappily.gamesystem.gui.HUDSelectSkill;
import com.growuphappily.gamesystem.system.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PackageFaction {
    public EnumFactionCategory message;
    public PackageFaction(FriendlyByteBuf buf){
        message = buf.readEnum(EnumFactionCategory.class);
    }

    public PackageFaction(EnumFactionCategory message){
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeEnum(message);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            Game.getGameByPlayerName(ctx.get().getSender().getDisplayName().getString()).searchPlayerByName(ctx.get().getSender().getDisplayName().getString()).factions.add(message);
        });
        ctx.get().setPacketHandled(true);
    }
}
