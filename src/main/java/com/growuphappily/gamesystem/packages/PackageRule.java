package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.system.Game;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class PackageRule {
    public int message;

    public PackageRule(FriendlyByteBuf buf){
        message = buf.readInt();
    }

    public PackageRule(int message){
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(message);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> Objects.requireNonNull(Game.getGameByPlayerName(Objects.requireNonNull(ctx.get().getSender()).getDisplayName().getString())).addRule(message));
        ctx.get().setPacketHandled(true);
    }
}
