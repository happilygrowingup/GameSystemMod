package com.growuphappily.gamesystem.packages;

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
        logger.info("Recv:" + message);
    }

    public PackageAttribute(String message){
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBytes(message.getBytes());
    }

    public void handler(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            String[] a = message.split("\\.");
            Game.instance.addPlayerAttributes(
                    Objects.requireNonNull(ctx.get().getSender()),
                    new Attributes(
                            Integer.parseInt(a[0]),
                            Integer.parseInt(a[1]),
                            Integer.parseInt(a[2]),
                            Integer.parseInt(a[3]),
                            Integer.parseInt(a[4]),
                            Integer.parseInt(a[5]),
                            Integer.parseInt(a[6]),
                            Integer.parseInt(a[7])
                    )
            );
        });
        ctx.get().setPacketHandled(true);
    }
}
