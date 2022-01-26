package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.gui.GUISelectAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.chat.TextComponent;
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
        ctx.get().enqueueWork(PackageOpenGUI::execute);
        ctx.get().setPacketHandled(true);
    }

    public static void execute(){
        //Code for open client gui
        Minecraft.getInstance().setScreen(new GUISelectAttribute(new TextComponent("Title")));
    }
}
