package com.growuphappily.gamesystem.packages;

import com.growuphappily.gamesystem.gui.HUDSelectSkill;
import com.growuphappily.gamesystem.models.EvilEternalHunter;
import com.growuphappily.gamesystem.system.Game;
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
            if (Game.instance != null){
                try {
                    LogManager.getLogger().info(skill);
                    LogManager.getLogger().info(skill.lastIndexOf("."));
                    Class<?> skillClass = Class.forName(skill);
                    skillClass.getMethod("run").invoke(skillClass.getConstructors()[0].newInstance((Object) null));
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
