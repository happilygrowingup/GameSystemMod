package com.growuphappily.gamesystem.effects;


import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber
public class EffectPoisoned {
    public static ArrayList<GamePlayer> affectedPlayers = new ArrayList<>();
    public static ArrayList<GamePlayer> originalPlayers = new ArrayList<>();

    public void addPlayer(GamePlayer player, int time){
        originalPlayers.add(player);
        affectedPlayers.add(player);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removePlayer(player);
            }
        },(long)time*1000);
        player.playerInstance.sendMessage(new TextComponent("You are poisoned for " + time + " seconds!"), ChatType.SYSTEM, Util.NIL_UUID);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event){
        if(Game.isStarted) {
            for (int i = 0; i < affectedPlayers.size(); i++) {
                GamePlayer player = affectedPlayers.get(i);
                player.hurt(DamageSource.MAGIC, (float) (5 - player.attributes.defence*0.1));
            }
        }
    }

    public static void removePlayer(GamePlayer player){
        affectedPlayers.remove(player);
    }
}
