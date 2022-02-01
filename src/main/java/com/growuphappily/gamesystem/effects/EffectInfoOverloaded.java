package com.growuphappily.gamesystem.effects;

import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;

import java.util.Timer;
import java.util.TimerTask;

public class EffectInfoOverloaded {
    public static final int ID = 4;
    public static final String message = "Info Overloaded";
    public void addPlayer(GamePlayer player, int time){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removePlayer(player);
            }
        },(long)time*1000);
        player.state.add(EnumPlayerState.INFO_OVERLOADED);
        player.playerInstance.sendMessage(new TextComponent("You are info-overloaded for " + time + " seconds!"), ChatType.SYSTEM, Util.NIL_UUID);
    }

    public static void removePlayer(GamePlayer player){
        player.state.remove(EnumPlayerState.INFO_OVERLOADED);
    }
}
