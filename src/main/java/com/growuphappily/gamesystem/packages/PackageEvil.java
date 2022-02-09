package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class PackageEvil {
    public int message;

    public PackageEvil(FriendlyByteBuf buf) {
        message = buf.readInt();
    }

    public PackageEvil(int message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(message);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Objects.requireNonNull(Game.getGameByPlayerName(Objects.requireNonNull(ctx.get().getSender()).getDisplayName().getString())).addEvil(Objects.requireNonNull(GamePlayer.getEvilWithID(message, ctx.get().getSender()))));
        ctx.get().setPacketHandled(true);
    }
}
