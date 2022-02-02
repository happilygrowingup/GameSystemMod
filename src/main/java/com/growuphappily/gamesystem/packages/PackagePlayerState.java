package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.gui.HUDAttributes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public class PackagePlayerState {
    private String message;

    private Logger logger = LogManager.getLogger();

    public PackagePlayerState(FriendlyByteBuf buf){
        message = buf.toString(Charset.defaultCharset());
        //logger.info("Recv:" + message);
    }

    public PackagePlayerState(String message){
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
        if(Objects.equals(message, "")){
            HUDAttributes.specialState = new ArrayList<>();
            return;
        }
        HUDAttributes.specialState = new ArrayList<String>(Arrays.asList(message.split("\\.")));
    }
}
