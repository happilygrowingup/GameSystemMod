package com.growuphappily.gamesystem.models;

import com.growuphappily.gamesystem.effects.EffectPoisoned;
import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.system.Attributes;
import com.growuphappily.gamesystem.system.Dice;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class EvilEternalHunter extends GamePlayer {
    public static final int ID = 0;
    public GamePlayer prey;
    public long lastPreyChange = 0;
    public int preyDefence;
    public long lastFull;
    public boolean isFull = false;
    public int lastSpeed;
    public String optionalSkill;
    public static ArrayList<String> skills = new ArrayList<>();
    static{
        skills.add("com.growuphappily.gamesystem.skills.SkillStartHunt");
        skills.add("com.growuphappily.gamesystem.skills.SkillPlaceTrap");
    }
    public ArrayList<Vec3> trapPos = new ArrayList<>() ;
    public EvilEternalHunter(ServerPlayer playerInstance){
        super(playerInstance);
        attributes = new Attributes(30, 260, 90, 30, 100, 50, 80, 0);
        attributes.maxtire = 200;
    }

    @SubscribeEvent
    public static void onTick(TickEvent event){
        if(Game.isStarted && Game.instance != null){
            if(Game.instance.evil instanceof EvilEternalHunter evil){
                if (evil.lastPreyChange + 120000 <= new Date().getTime()){  //Every 120s
                    if (evil.prey != null){
                        evil.prey.attributes.defence = evil.preyDefence;
                        evil.prey.attributes.speed *= 2;
                    }
                    evil.prey = Game.instance.humans.get(new Random().nextInt(Game.instance.humans.size()));
                    evil.preyDefence = evil.prey.attributes.defence;
                    evil.prey.attributes.defence = 0;
                    evil.prey.attributes.speed /= 2;
                    Game.broadcast(evil.prey.playerInstance.getDisplayName().getString() + " is selected as evil's prey!!!");
                    evil.lastPreyChange = new Date().getTime();
                }
                if (!Game.isInGame(evil.prey.playerInstance)){
                    evil.lastSpeed = evil.attributes.speed;
                    evil.attributes.speed = 0;
                    evil.attributes.IQ -= 20;
                    evil.lastFull = new Date().getTime();
                    evil.isFull = true;
                    Game.broadcast("Prey died! Evil is full!");
                }
                if (evil.isEvil && evil.lastFull + 60000 >= new Date().getTime()){
                    evil.attributes.speed = evil.lastSpeed;
                    evil.attributes.IQ += 20;
                    evil.isFull = false;
                    Game.broadcast("Evil no longer full...");
                }
                for (int i = 0; i < evil.trapPos.size(); i++) {
                    Vec3 trap = evil.trapPos.get(i);
                    if(trap == null){
                        continue;
                    }
                    boolean trapped = false;
                    for (int j = 0; j < Game.instance.humans.size(); j++) {
                        GamePlayer human = Game.instance.humans.get(j);
                        if(trap.distanceTo(human.playerInstance.position()) <= 3.){
                            trapped = true;
                            if((double)Dice.onedX(10) >= human.attributes.IQ*0.03 + human.attributes.knowledge * 0.05){
                                EffectPoisoned.addPlayer(human, 5);
                                human.playerInstance.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,0x100,1));
                                human.playerInstance.addEffect(new MobEffectInstance(MobEffects.GLOWING,0x30, 0));
                            }else{
                                human.playerInstance.addEffect(new MobEffectInstance(MobEffects.GLOWING,200, 0));
                            }
                        }
                    }
                    if(trapped){
                        try {
                            evil.trapPos.set(i, null);
                        }catch (IndexOutOfBoundsException ignored){}
                    }
                }
                evil.trapPos.remove(null);
            }
        }
    }

    public void openHuntMode(){
        if(!state.contains(EnumPlayerState.HUNTING)) {
            playerInstance.sendMessage(new TextComponent("Successfully opened hunt mode."), ChatType.SYSTEM, Util.NIL_UUID);
            state.add(EnumPlayerState.HUNTING);
        }else{
            playerInstance.sendMessage(new TextComponent("Failed to open hunt mode: Already opened!"), ChatType.SYSTEM, Util.NIL_UUID);
            attributes.surgical -= 50;
        }
    }

    public void placeTrap(){
        playerInstance.sendMessage(new TextComponent("Trapping..."), ChatType.SYSTEM, Util.NIL_UUID);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                playerInstance.sendMessage(new TextComponent("Trapped!"), ChatType.SYSTEM, Util.NIL_UUID);
                trapPos.add(playerInstance.position());
            }
        }, 2000);
    }
}
