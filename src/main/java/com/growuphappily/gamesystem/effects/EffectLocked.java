package com.growuphappily.gamesystem.effects;


import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;

import java.util.Timer;
import java.util.TimerTask;

public class EffectLocked {
    public static final int ID = 5;
    public static final String message = "Locked";
    public void addPlayer(GamePlayer player, int time){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removePlayer(player);
            }
        },(long)time*1000);
        player.state.add(EnumPlayerState.LOCKED);
        player.playerInstance.sendMessage(new TextComponent("You are locked for " + time + " seconds!"), ChatType.SYSTEM, Util.NIL_UUID);
    }

    public static void removePlayer(GamePlayer player){
        player.state.remove(EnumPlayerState.LOCKED);
    }
}
