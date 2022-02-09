package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.gui.HUDSelectSkill;
import com.growuphappily.gamesystem.models.EvilEternalHunter;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.Supplier;

public class PackageSkills {
    public String skill;

    public PackageSkills(FriendlyByteBuf buf){
        skill = buf.toString(Charset.defaultCharset());
    }

    public PackageSkills(String message){
        this.skill = message;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBytes(skill.getBytes());
    }

    public void handler(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getOriginationSide().isClient()){
                try {
                    LogManager.getLogger().info(skill);
                    LogManager.getLogger().info(skill.lastIndexOf("."));
                    Class<?> skillClass = Class.forName(skill);
                    skillClass.getMethod("run", GamePlayer.class).invoke(skillClass.getConstructors()[0].newInstance(), Objects.requireNonNull(Game.getGameByPlayerName(Objects.requireNonNull(ctx.get().getSender()).getDisplayName().getString())).searchPlayerByName(Objects.requireNonNull(ctx.get().getSender()).getDisplayName().getString()));
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
