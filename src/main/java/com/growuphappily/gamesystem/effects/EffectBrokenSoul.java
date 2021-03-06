package com.growuphappily.gamesystem.effects;

import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class EffectBrokenSoul {
    public static final int ID = 1;
    public static ArrayList<GamePlayer> affectedPlayers = new ArrayList<>();
    public static final String message = "Soul Broken";
    public static void addPlayer(GamePlayer player, int time){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removePlayer(player);
            }
        },(long)time*1000);
        affectedPlayers.add(player);
        player.state.add(EnumPlayerState.SOUL_BROKEN);
        player.playerInstance.sendMessage(new TextComponent("Your soul are broken for " + time + " seconds!"), ChatType.SYSTEM, Util.NIL_UUID);
    }

    public static void removePlayer(GamePlayer player){
        player.state.remove(EnumPlayerState.SOUL_BROKEN);
        affectedPlayers.remove(player);
    }
}
