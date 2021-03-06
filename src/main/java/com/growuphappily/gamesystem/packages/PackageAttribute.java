package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.gui.HUDAttributes;
import com.growuphappily.gamesystem.system.Attributes;
import com.growuphappily.gamesystem.system.Game;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public class PackageAttribute {
    private String message;

    private Logger logger = LogManager.getLogger();
    public PackageAttribute(FriendlyByteBuf buf){
        message = buf.toString(Charset.defaultCharset());
        //logger.info("Recv:" + message);
    }

    public PackageAttribute(String message){
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBytes(message.getBytes());
    }

    public void handler(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection().getOriginationSide().isServer()){
                String[] a = message.split("\\.");
                HUDAttributes.attr = new Attributes(
                        Integer.parseInt(a[0]),
                        Integer.parseInt(a[1]),
                        Integer.parseInt(a[2]),
                        Integer.parseInt(a[3]),
                        Integer.parseInt(a[4]),
                        Integer.parseInt(a[5]),
                        Integer.parseInt(a[6]),
                        Integer.parseInt(a[7])
                );
                HUDAttributes.isAttackInCold = Boolean.getBoolean(a[8]);
            }else {
                String[] a = message.split("\\.");
                try {
                    Objects.requireNonNull(Game.getGameByPlayerName(Objects.requireNonNull(ctx.get().getSender()).getDisplayName().getString())).addPlayerAttributes(
                            Objects.requireNonNull(ctx.get().getSender()),
                            new Attributes(
                                    Integer.parseInt(a[0]),
                                    Integer.parseInt(a[1]),
                                    Integer.parseInt(a[2]),
                                    Integer.parseInt(a[3]),
                                    Integer.parseInt(a[4]),
                                    Integer.parseInt(a[5]),
                                    Integer.parseInt(a[6]),
                                    0
                            )
                    );
                }catch(NullPointerException ignored){}
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
