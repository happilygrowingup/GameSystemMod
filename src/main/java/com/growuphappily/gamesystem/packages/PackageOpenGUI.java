package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.gui.GUISelectAttribute;
import com.growuphappily.gamesystem.gui.GUISelectEvil;
import com.growuphappily.gamesystem.gui.HUDAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public class PackageOpenGUI {
    private String message;

    private Logger logger = LogManager.getLogger();

    public PackageOpenGUI(FriendlyByteBuf buf){
        message = buf.toString(Charset.defaultCharset());
        logger.info("Recv:" + message);
    }

    public PackageOpenGUI(String message){
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBytes(message.getBytes());
    }

    public void handler(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(this::execute);
        ctx.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    public void execute(){
        if (Objects.equals(message, "Attr")) {
            HUDAttributes.isEvil = false;
            Minecraft.getInstance().setScreen(new GUISelectAttribute(new TextComponent("Title")));
        }
        if (Objects.equals(message, "Evil")){
            HUDAttributes.isEvil = true;
            Minecraft.getInstance().setScreen(new GUISelectEvil(new TextComponent("Title")));
        }
    }
}
