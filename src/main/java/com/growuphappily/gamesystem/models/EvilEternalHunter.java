package com.growuphappily.gamesystem.models;

import com.growuphappily.gamesystem.system.Attributes;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Date;
import java.util.Random;

@Mod.EventBusSubscriber
public class EvilEternalHunter extends GamePlayer {
    public static final int ID = 0;
    public GamePlayer prey;
    public long lastPreyChange = 0;
    public int preyDefence;
    public long lastFull;
    public boolean isFull = false;
    public int lastSpeed;

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
                    evil.attributes.speed = evil.lastSpeed;;
                    evil.attributes.IQ += 20;
                    evil.isFull = false;
                    Game.broadcast("Evil no longer full...");
                }
            }
        }
    }
}
