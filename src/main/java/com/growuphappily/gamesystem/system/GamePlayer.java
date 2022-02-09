package com.growuphappily.gamesystem.system;


import com.growuphappily.gamesystem.enums.EnumAttackResult;
import com.growuphappily.gamesystem.enums.EnumCastResult;
import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.eventsystem.AttackEvent;
import com.growuphappily.gamesystem.eventsystem.AttackSucceedEvent;
import com.growuphappily.gamesystem.eventsystem.EventHandler;
import com.growuphappily.gamesystem.models.EvilEternalHunter;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
    public boolean isSuperRegened = false;
    public boolean canSuperRegen = false;
    public boolean isLocked;

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
                AttackSucceedEvent event = new AttackSucceedEvent(this, beingAttacked, (float) ((attributes.strength*0.15) + 5 - (beingAttacked.attributes.defence * 0.1)));
                if(EventHandler.post(event)){return EnumAttackResult.CRITICAL;}
                beingAttacked.hurt(DamageSource.playerAttack(playerInstance), event.damage);
                return EnumAttackResult.CRITICAL;
            }else {
                AttackSucceedEvent event = new AttackSucceedEvent(this, beingAttacked, (float) ((attributes.strength*0.1) + 5 - (beingAttacked.attributes.defence * 0.1)));
                if(EventHandler.post(event)){return EnumAttackResult.NORMAL;}
                beingAttacked.hurt(DamageSource.playerAttack(playerInstance), event.damage);
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
        if(Game.instances == null){
            return;
        }
        for (int k = 0; k <Game.instances.size(); k++) {
            Game game = Game.instances.get(k);
            if (game.isStarted) {
                GamePlayer player = game.searchPlayerByName(event.getPlayer().getDisplayName().getString());
                GamePlayer beingAttacked = game.searchPlayerByName(event.getTarget().getDisplayName().getString());
                if (beingAttacked != null) {
                    event.setCanceled(true);
                    if (EventHandler.post(new AttackEvent(player, beingAttacked))) {
                        return;
                    }
                    if (player.lastAttack + (long) (1 / player.attributes.getAttackSpeed()) * 1000 > new Date().getTime()) {
                        LogManager.getLogger().info(player.playerInstance.getDisplayName().getString() + "'s attack is Cold!");
                        player.playerInstance.sendMessage(new TextComponent("Colding!"), ChatType.SYSTEM, Util.NIL_UUID);
                        return;
                    }
                    EnumAttackResult result = player.Attack(beingAttacked);
                    player.lastAttack = new Date().getTime();
                    beingAttacked.lastHurt = player.lastAttack;
                    if (result == EnumAttackResult.CRITICAL) {
                        player.playerInstance.sendMessage(new TextComponent("CRITICAL!"), ChatType.SYSTEM, Util.NIL_UUID);
                    } else if (result == EnumAttackResult.MISSED) {
                        player.playerInstance.sendMessage(new TextComponent("MISSED!"), ChatType.SYSTEM, Util.NIL_UUID);
                    }
                    if (player.state.contains(EnumPlayerState.HUNTING)) {
                        int dice = Dice.onedX(100);
                        if ((double) dice >= (double) 5 + (double) beingAttacked.attributes.mental * 0.1 + (double) beingAttacked.attributes.defence) {
                            beingAttacked.blood = 0;
                            player.playerInstance.sendMessage(new TextComponent("HUNT KILL!"), ChatType.SYSTEM, Util.NIL_UUID);
                            game.broadcast("Evil killed " + beingAttacked.playerInstance.getDisplayName().getString() + " using hunting mode!!");
                        } else {
                            beingAttacked.playerInstance.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 1));
                        }
                    }
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
        isSuperRegened = true;
    }

    public void sendMessage(String msg){
        playerInstance.sendMessage(new TextComponent(msg), ChatType.SYSTEM, Util.NIL_UUID);
    }
}