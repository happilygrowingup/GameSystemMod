package com.growuphappily.gamesystem.effects;

import com.growuphappily.gamesystem.system.Dice;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Date;

public class EffectAbyss {
    public static final int ID = 3;
    public static final String message = "Deep Vally";
    public static ArrayList<GamePlayer> affectedPlayers = new ArrayList<>();
    public static ArrayList<Long> startTimes = new ArrayList<>();
    public static ArrayList<Integer> times = new ArrayList<>();
    public static long lastTime = 0;
    public void addPlayer(GamePlayer player, int time){
        affectedPlayers.add(player);
        player.attributes.health -= 5;
        player.attributes.speed -= 5;
        startTimes.add(new Date().getTime());
        player.playerInstance.sendMessage(new TextComponent("You are infected by the Deep Vally..."), ChatType.SYSTEM, Util.NIL_UUID);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event){
        if(Game.isStarted) {
            if(lastTime + (long)1000 <= new Date().getTime()) {
                for (GamePlayer player : affectedPlayers) {
                    int dice = Dice.onedX(6);
                    int index = affectedPlayers.indexOf(player);
                    if((double)dice > player.attributes.mental*0.1){
                        times.set(index, times.get(index) + 2);
                    }else if((double)dice == player.attributes.mental*0.1){
                        player.attributes.mental -= 3;
                    }
                    if(startTimes.get(index) + (long) times.get(index) <= new Date().getTime()){
                        removePlayer(player);
                    }
                }
            }
        }
    }

    public static void removePlayer(GamePlayer player){
        int index = affectedPlayers.indexOf(player);
        affectedPlayers.remove(player);
        times.remove(index);
        startTimes.remove(index);
        player.attributes.speed += 5;
        player.attributes.health += 5;
    }
}
