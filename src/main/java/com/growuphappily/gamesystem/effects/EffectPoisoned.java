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
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber
public class EffectPoisoned {
    public static final String message = "Poisoned";
    public static final int ID = 6;
    public static ArrayList<GamePlayer> affectedPlayers = new ArrayList<>();
    public static ArrayList<GamePlayer> originalPlayers = new ArrayList<>();
    public static long lastHurt = 0;
    public static void addPlayer(GamePlayer player, int time){
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
        for (int k = 0; k < Game.instances.size(); k++) {
            Game game = Game.instances.get(k);
            if (game.isStarted) {
                if (lastHurt + (long) 1000 <= new Date().getTime()) {
                    for (int i = 0; i < affectedPlayers.size(); i++) {
                        GamePlayer player = affectedPlayers.get(i);
                        if(Game.getGameByPlayerName(player.playerInstance.getDisplayName().getString()) != game){
                            return;
                        }
                        player.hurt(DamageSource.MAGIC, (float) (5 - player.attributes.defence * 0.1));
                        player.blood -= (float) (5 - player.attributes.defence * 0.1);
                    }
                    lastHurt = new Date().getTime();
                }
            }
        }
    }

    public static void removePlayer(GamePlayer player){
        affectedPlayers.remove(player);
    }
}
