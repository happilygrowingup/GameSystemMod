package com.growuphappily.gamesystem.system;


import com.growuphappily.gamesystem.enums.EnumAttackResult;
import com.growuphappily.gamesystem.enums.EnumCastResult;
import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.models.EvilEternalHunter;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

import java.util.Date;

@Mod.EventBusSubscriber
public class GamePlayer {
    public Attributes attributes;
    public ServerPlayer playerInstance;
    public EnumPlayerState state;
    public float blood;
    public boolean isEvil = false;
    public long lastHurt = 0;
    public long lastAttack = 0;

    public GamePlayer(ServerPlayer playerInstance){
        this.playerInstance = playerInstance;
    }

    public EnumAttackResult Attack(GamePlayer beingAttacked){
        if(((Dice.onedX(100) + this.attributes.speed)*0.2) >= (20 + beingAttacked.attributes.speed*0.4)){
            beingAttacked.lastHurt = new Date().getTime();
            if(Dice.onedX(100) >= (Dice.onedX(100) + beingAttacked.attributes.defence - (attributes.strength * 0.1))){
                beingAttacked.blood -= (attributes.strength*0.15) + 5 - (beingAttacked.attributes.defence * 0.1);
                return EnumAttackResult.CRITICAL;
            }else {
                beingAttacked.blood -= (attributes.strength*0.1) + 5 - (beingAttacked.attributes.defence * 0.1);
                return EnumAttackResult.NORMAL;
            }
        }else{
            return EnumAttackResult.MISSED;
        }
    }

    public EnumCastResult tryCast(){
        if(state == EnumPlayerState.OVERLOADED){
            return EnumCastResult.FAIL;
        }
        int d = Dice.onedX(100);
        if(d <= 60 + (attributes.IQ * 0.5)){
            if(d == 1){
                return EnumCastResult.BIG_SUCCESS;
            }
            return EnumCastResult.SUCCESS;
        }else{
            if(d == 100){
                return EnumCastResult.BIG_FAIL;
            }
            return EnumCastResult.FAIL;
        }
    }

    public void addAttributes(Attributes attr){
        attributes = attr;
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event){
        LogManager.getLogger().info("Attack!");
        if(Game.isStarted) {
            GamePlayer player = Game.instance.searchPlayerByName(event.getPlayer().getDisplayName().getString());
            GamePlayer beingAttacked = Game.instance.searchPlayerByName(event.getTarget().getDisplayName().getString());
            if(beingAttacked != null){
                event.setCanceled(true);
                EnumAttackResult result = player.Attack(beingAttacked);
                if(result == EnumAttackResult.CRITICAL){
                    player.playerInstance.sendMessage(new TextComponent("CRITICAL!"), ChatType.SYSTEM, Util.NIL_UUID);
                }else{
                    player.playerInstance.sendMessage(new TextComponent("MISSED!"), ChatType.SYSTEM, Util.NIL_UUID);
                }
            }
        }
    }

    public static GamePlayer getEvilWithID(int ID, ServerPlayer playerInstance){
        if (ID == 0) {return new EvilEternalHunter(playerInstance);}
        return null;
    }
}