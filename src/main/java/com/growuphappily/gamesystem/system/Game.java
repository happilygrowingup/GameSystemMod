package com.growuphappily.gamesystem.system;

import com.growuphappily.gamesystem.enums.EnumGameMode;
import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.models.EvilEternalHunter;
import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageAttribute;
import com.growuphappily.gamesystem.packages.PackageGameStart;
import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;

import java.util.*;

@Mod.EventBusSubscriber
public class Game extends Thread{
    private static final boolean DEBUG = false;
    public EnumGameMode gameMode;
    public ArrayList<GamePlayer> players = new ArrayList<>();
    public static Game instance;
    public static boolean isStarted = false;
    public static MinecraftServer server;
    public GamePlayer evil;
    public ArrayList<GamePlayer> humans = new ArrayList<>();
    private static long lastTime = 0;

    public Game(EnumGameMode gameMode){
        instance = this;
        isStarted = false;
        this.gameMode = gameMode;
        Random rand = new Random();
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            players.add(new GamePlayer(p));
        }
        evil = players.get(rand.nextInt(server.getPlayerCount()));
        evil.isEvil = true;
        for (GamePlayer player : players) {
            if (!player.isEvil) {
                humans.add(player);
            }
        }
        for (GamePlayer human:humans) {
            human.playerInstance.sendMessage(new TextComponent("Your identity is human. Select your Attributes and factions."), ChatType.SYSTEM, Util.NIL_UUID);
        }
        evil.playerInstance.sendMessage(new TextComponent("Your identity is evil. Select distorted rules"), ChatType.SYSTEM, Util.NIL_UUID);
    }

    public void addPlayerAttributes(ServerPlayer p, Attributes attributes){
        searchPlayerByName(p.getDisplayName().getString()).addAttributes(attributes);
        broadcast(p.getDisplayName().getString() + " has finished selecting.");
        for(GamePlayer player : players){
            if(player.attributes == null){
                return;
            }
        }
        broadcast("Game is ready.Run \"/game start\" to start game.");
    }

    public void addEvil(GamePlayer p){
        p.isEvil = true;
        players.set(players.indexOf(evil), p);
        evil = p;
        broadcast(p.playerInstance.getDisplayName().getString() + " has finished selecting.");
        for(GamePlayer player : players){
            if(player.attributes == null){
                return;
            }
        }
        broadcast("Game is ready.Run \"/game start\" to start game.");
    }

    public void start(){
        for(GamePlayer p : players){
            if(p.attributes == null){
                broadcast("Game is not ready!");
                return;
            }
        }
        Game.isStarted = true;
        for(GamePlayer player : players){
            player.blood = player.attributes.getMaxBlood();
            player.attributes.originalSpeed = Objects.requireNonNull(player.playerInstance.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED)).getBaseValue();
            if (!player.isEvil) {
                player.attributes.surgical = player.attributes.getMaxSurgical();
            }
            Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackageGameStart(true));
        }
        evil.attributes.surgical = 0;
        broadcast("Game Started!");
        broadcast(evil.playerInstance.getDisplayName().getString() + " is EVIL!!BE CAREFUL!!");
    }

    @SubscribeEvent
    public static void update(TickEvent.ServerTickEvent event){
        if(Game.isStarted) {
            if (new Date().getTime() >= lastTime + 1000) {  //Exec by seconds
                for (int i = 0; i < Game.instance.players.size(); i++) {
                    GamePlayer player = Game.instance.players.get(i);
                    if (player.isEvil && player.attributes.surgical > 0) {
                        player.attributes.surgical -= player.attributes.getSurgicalRegenSpeed();
                    }
                    if (!player.isEvil) {
                        player.attributes.surgical += player.attributes.getSurgicalRegenSpeed();
                    }
                    if(!player.isEvil && player.attributes.surgical > player.attributes.getMaxSurgical()){
                        player.attributes.surgical = player.attributes.getMaxSurgical();
                    }
                    if(player.isEvil && player.attributes.surgical < 0){
                        player.attributes.surgical = 0;
                    }
                    if (!player.isEvil && player.lastHurt + (long)player.attributes.getRestTime() <= new Date().getTime()) {
                        player.blood += player.attributes.getRegenSpeed();
                        LogManager.getLogger().info("regen: " + player.attributes.getRestTime());
                    }
                    if(player.isEvil){
                        //player.blood += player.attributes.getRegenSpeed();
                    }
                    Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackageAttribute(
                                    player.attributes.speed + "." +
                                    player.attributes.health + "." +
                                    player.attributes.strength + "." +
                                    player.attributes.defence + "." +
                                    player.attributes.mental + "." +
                                    player.attributes.IQ + "." +
                                    player.attributes.knowledge + "." +
                                    player.attributes.surgical + "." +
                                    ((player.lastAttack + 1/player.attributes.getAttackSpeed())*1000 < new Date().getTime())
                    ));
                }
                lastTime = new Date().getTime();
            }
            for (int i = 0; i < Game.instance.players.size(); i++) {
                GamePlayer player = Game.instance.players.get(i);
                AttributeInstance attrMaxHealth = player.playerInstance.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
                assert attrMaxHealth != null;
                attrMaxHealth.setBaseValue(player.attributes.getMaxBlood());
                AttributeInstance attrMoveSpeed = player.playerInstance.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
                assert attrMoveSpeed != null;
                attrMoveSpeed.setBaseValue(player.attributes.originalSpeed + player.attributes.speed*player.attributes.originalSpeed*0.01);
                // AttributeInstance attrAttackSpeed = player.playerInstance.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED);
                // assert attrAttackSpeed != null;
                // attrAttackSpeed.setBaseValue(attrAttackSpeed.getBaseValue() + player.attributes.getAttackSpeed());
                player.playerInstance.setHealth(player.blood);
            }
            if (Game.instance.evil.attributes.surgical >= Game.instance.evil.attributes.maxtire) {
                Game.instance.evil.state = EnumPlayerState.OVERLOADED;
                Game.instance.evil.attributes.speed -= 20;
                Game.instance.evil.attributes.health -= 20;
                Game.instance.evil.attributes.strength -= 20;
                Game.instance.evil.attributes.defence -= 20;
                Game.instance.evil.attributes.mental -= 20;
                Game.instance.evil.attributes.IQ -= 20;
                Game.instance.evil.attributes.knowledge -= 20;
                Game.instance.evil.playerInstance.sendMessage(new TextComponent("[SYSTEM]You have overloaded!"), ChatType.SYSTEM, Util.NIL_UUID);
            }
            if (Game.instance.evil.attributes.surgical <= 0 && Game.instance.evil.state == EnumPlayerState.OVERLOADED) {
                Game.instance.evil.state = null;
                Game.instance.evil.attributes.speed += 20;
                Game.instance.evil.attributes.health += 20;
                Game.instance.evil.attributes.strength += 20;
                Game.instance.evil.attributes.defence += 20;
                Game.instance.evil.attributes.mental += 20;
                Game.instance.evil.attributes.IQ += 20;
                Game.instance.evil.attributes.knowledge += 20;
                Game.instance.evil.playerInstance.sendMessage(new TextComponent("[SYSTEM]You have out of overload."), ChatType.SYSTEM, Util.NIL_UUID);
            }

            //Check death
            for (int i = 0; i < Game.instance.players.size() && !DEBUG; i++) {
                GamePlayer gamePlayer = Game.instance.players.get(i);
                if (gamePlayer.playerInstance != server.getPlayerList().getPlayerByName(gamePlayer.playerInstance.getDisplayName().getString()) || gamePlayer.blood <= 0) {
                    gamePlayer.playerInstance.setGameMode(GameType.SPECTATOR);
                    Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> gamePlayer.playerInstance), new PackageGameStart(false));
                    if (gamePlayer.isEvil) {
                        broadcast("Human win!");
                        gameOver();
                    } else {
                        Game.instance.humans.remove(gamePlayer);
                    }
                }
            }
            //Check winner
            if (Game.instance.humans.size() == 0 && !DEBUG) {
                broadcast("Evil win!");
                gameOver();
            }
        }
    }

    @SubscribeEvent
    public static void updatec(TickEvent.ClientTickEvent event){

    }

    public static void broadcast(String msg){
        server.getPlayerList().broadcastMessage(new TextComponent("[SYSTEM] " + msg), ChatType.SYSTEM, Util.NIL_UUID);
    }

    public GamePlayer searchPlayerByName(String name){
        for (GamePlayer p : players) {
            if (p.playerInstance.getDisplayName().getString().equals(name)) {
                return p;
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onServerStop(ServerStoppingEvent event){
        Game.isStarted = false;
    }

    public static void gameOver(){
        Game.isStarted = false;
        for (GamePlayer player: instance.players) {
            Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackageGameStart(false));
        }
    }

    public static boolean isInGame(Entity e){
        return instance.searchPlayerByName(e.getDisplayName().getString()) != null;
    }
}
