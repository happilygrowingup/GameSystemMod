package com.growuphappily.gamesystem.system;


import com.growuphappily.gamesystem.enums.EnumAttackResult;
import com.growuphappily.gamesystem.enums.EnumCastResult;
import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.models.EvilEternalHunter;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Date;

@Mod.EventBusSubscriber
public class GamePlayer {
    public Attributes attributes;
    public ServerPlayer playerInstance;
    public ArrayList<EnumPlayerState> state = new ArrayList<>();
    public float blood;
    public boolean isEvil = false;
    public long lastHurt = 0;
    public long lastAttack = 0;

    public GamePlayer(ServerPlayer playerInstance){
        this.playerInstance = playerInstance;
    }

    public EnumAttackResult Attack(GamePlayer beingAttacked){
        if(beingAttacked.state.contains(EnumPlayerState.CAN_NOT_SELECT) || state.contains(EnumPlayerState.CAN_NOT_SELECT)){
            return EnumAttackResult.MISSED;
        }
        if((Dice.onedX(100) + this.attributes.speed*0.2) >= (20 + beingAttacked.attributes.speed*0.4)){
            beingAttacked.lastHurt = new Date().getTime();
            if(Dice.onedX(100) >= (Dice.onedX(100) + beingAttacked.attributes.defence - (attributes.strength * 0.1))){
                beingAttacked.hurt(DamageSource.playerAttack(playerInstance), (float) ((attributes.strength*0.15) + 5 - (beingAttacked.attributes.defence * 0.1)));
                return EnumAttackResult.CRITICAL;
            }else {
                beingAttacked.hurt(DamageSource.playerAttack(playerInstance), (float) ((attributes.strength*0.1) + 5 - (beingAttacked.attributes.defence * 0.1)));
                return EnumAttackResult.NORMAL;
            }
        }else{
            return EnumAttackResult.MISSED;
        }
    }

    public EnumCastResult tryCast(){
        if(state.contains(EnumPlayerState.OVERLOADED) || state.contains(EnumPlayerState.LOCKED) || state.contains(EnumPlayerState.CAN_NOT_SELECT)){
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
        //LogManager.getLogger().info("Attack!");
        if(Game.isStarted && Game.instance != null) {
            GamePlayer player = Game.instance.searchPlayerByName(event.getPlayer().getDisplayName().getString());
            GamePlayer beingAttacked = Game.instance.searchPlayerByName(event.getTarget().getDisplayName().getString());
            if(beingAttacked != null){
                event.setCanceled(true);
                if(player.lastAttack + (long)(1/player.attributes.getAttackSpeed())*1000 > new Date().getTime()){
                    LogManager.getLogger().info(player.playerInstance.getDisplayName().getString() + "'s attack is Cold!");
                    player.playerInstance.sendMessage(new TextComponent("Colding!"), ChatType.SYSTEM, Util.NIL_UUID);
                    return;
                }
                EnumAttackResult result = player.Attack(beingAttacked);
                player.lastAttack = new Date().getTime();
                beingAttacked.lastHurt = player.lastAttack;
                if(result == EnumAttackResult.CRITICAL){
                    player.playerInstance.sendMessage(new TextComponent("CRITICAL!"), ChatType.SYSTEM, Util.NIL_UUID);
                }else if(result == EnumAttackResult.MISSED){
                    player.playerInstance.sendMessage(new TextComponent("MISSED!"), ChatType.SYSTEM, Util.NIL_UUID);
                }
            }
        }
    }

    public static GamePlayer getEvilWithID(int ID, ServerPlayer playerInstance){
        if (ID == 0) {return new EvilEternalHunter(playerInstance);}
        return null;
    }

    public void hurt(DamageSource source, float amount){
        playerInstance.hurt(source, amount);
        blood -= amount;
    }
}