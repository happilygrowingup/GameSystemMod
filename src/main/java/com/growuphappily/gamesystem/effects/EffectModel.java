package com.growuphappily.gamesystem.effects;


import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber
public class EffectModel {
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
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event){
        if(Game.isStarted) {
            for (int i = 0; i < affectedPlayers.size(); i++) {
                GamePlayer player = affectedPlayers.get(i);

            }
        }
    }

    public static void removePlayer(GamePlayer player){

    }
}
