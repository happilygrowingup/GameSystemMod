package com.growuphappily.gamesystem.effects;

import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;

import java.util.Timer;
import java.util.TimerTask;

public class EffectCanNotSelect {
    public static final int ID = 2;
    public static final String message = "Can Not Select";
    public void addPlayer(GamePlayer player, int time){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removePlayer(player);
            }
        },(long)time*1000);
        player.state.add(EnumPlayerState.CAN_NOT_SELECT);
        player.playerInstance.sendMessage(new TextComponent("You can't be selected for " + time + " seconds!"), ChatType.SYSTEM, Util.NIL_UUID);
    }

    public static void removePlayer(GamePlayer player){
        player.state.remove(EnumPlayerState.CAN_NOT_SELECT);
    }
}
