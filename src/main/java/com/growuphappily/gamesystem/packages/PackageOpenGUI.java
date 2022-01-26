package com.growuphappily.gamesystem.packages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketDecoder;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

public class PackageOpenGUI {
    private String message;

    public PackageOpenGUI(FriendlyByteBuf buf){
        message = Arrays.toString(buf.readByteArray());
    }

    public PackageOpenGUI(String message){
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBytes(message.getBytes());
    }

    public void handler(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            //Code for open client GUI
        });
    }
}
