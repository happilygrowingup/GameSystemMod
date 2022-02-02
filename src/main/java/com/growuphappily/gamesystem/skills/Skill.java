package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;

public class Skill {
    public int consumption;
    public void run(GamePlayer player){
        if(player.isEvil){
            if(player.attributes.surgical + consumption > player.attributes.maxtire){
                player.playerInstance.sendMessage(new TextComponent("Failed to cast: you are too tired!"), ChatType.SYSTEM, Util.NIL_UUID);
                return;
            }
        }else{
            if(player.attributes.surgical < consumption){
                player.playerInstance.sendMessage(new TextComponent("Failed to cast: your surgical is not enough!"), ChatType.SYSTEM, Util.NIL_UUID);
                return;
            }
        }
        switch (player.tryCast()){
            case FAIL -> {
                player.playerInstance.sendMessage(new TextComponent("Cast FAILED!"), ChatType.SYSTEM, Util.NIL_UUID);
                player.attributes.surgical += consumption * (player.isEvil ? -1 : 1);
            }
            case SUCCESS -> {
                player.playerInstance.sendMessage(new TextComponent("Cast SUCCEED!"), ChatType.SYSTEM, Util.NIL_UUID);
                player.attributes.surgical += consumption * (player.isEvil ? -1 : 1);
            }
            case BIG_FAIL -> {
                player.playerInstance.sendMessage(new TextComponent("Casting .. Shit, A BIG FAIL!"), ChatType.SYSTEM, Util.NIL_UUID);
                player.attributes.surgical += consumption * 2 * (player.isEvil ? -1 : 1);
            }
            case BIG_SUCCESS -> {
                player.playerInstance.sendMessage(new TextComponent("Casting .. A BIG SUCCESS!"), ChatType.SYSTEM, Util.NIL_UUID);
            }
        }
    }
}
