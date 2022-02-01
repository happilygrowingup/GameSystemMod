package com.growuphappily.gamesystem.effects;

import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class EffectBrokenArmor {
    public static final int ID = 0;
    public static final String message = "Broken Armor";
    public static ArrayList<GamePlayer> affectedPlayers = new ArrayList<>();
    public void addPlayer(GamePlayer player, int time){
        Timer timer = new Timer();
        int lastDefence = player.attributes.defence;
        affectedPlayers.add(player);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removePlayer(player, lastDefence);
            }
        },(long)time*1000);
        player.attributes.defence = (int)(0.5*player.attributes.defence);
        player.playerInstance.sendMessage(new TextComponent("Your armor have broken for " + time + " seconds!"), ChatType.SYSTEM, Util.NIL_UUID);
    }

    public static void removePlayer(GamePlayer player, int lastDefence){
        player.attributes.defence = lastDefence;
        affectedPlayers.remove(player);
    }
}
