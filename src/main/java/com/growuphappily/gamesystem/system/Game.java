package com.growuphappily.gamesystem.system;

import com.growuphappily.gamesystem.effects.*;
import com.growuphappily.gamesystem.enums.EnumGameMode;
import com.growuphappily.gamesystem.enums.EnumGameStage;
import com.growuphappily.gamesystem.enums.EnumPlayerState;
import com.growuphappily.gamesystem.enums.EnumRules;
import com.growuphappily.gamesystem.models.EvilEternalHunter;
import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageAttribute;
import com.growuphappily.gamesystem.packages.PackageGameStart;
import com.growuphappily.gamesystem.packages.PackagePlayerState;
import com.growuphappily.gamesystem.rules.RuleBloodFeast;
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
    public static EnumGameStage stage;
    public static EnumRules rule;
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
        if (isReady()) {
            broadcast("Game is ready.Run \"/game start\" to start game.");
        }
    }

    public void addEvil(GamePlayer p){
        p.isEvil = true;
        players.set(players.indexOf(evil), p);
        evil = p;
        broadcast(p.playerInstance.getDisplayName().getString() + " has finished selecting.");
        if(isReady()) {
            broadcast("Game is ready.Run \"/game start\" to start game.");
        }
    }

    public void start(){
        if(isReady()) {
            broadcast("Game is not ready!");
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
                    if (player.isEvil && player.attributes.surgical > 0 && !player.state.contains(EnumPlayerState.HUNTING)) {
                        if(rule == EnumRules.BLOOD_FEAST){
                            player.attributes.surgical -= player.attributes.getSurgicalRegenSpeed()*0.8f;
                        }else {
                            player.attributes.surgical -= player.attributes.getSurgicalRegenSpeed();
                        }
                    }
                    if(player.state.contains(EnumPlayerState.HUNTING)){
                        player.attributes.surgical += 2;
                    }
                    if (!player.isEvil && !player.state.contains(EnumPlayerState.SOUL_BROKEN)) {
                        if(player.attributes.surgical <= player.attributes.getMaxSurgical()*0.25 && player.attributes.surgical > 0.){
                            player.attributes.surgical += 1.2 * (rule == EnumRules.BLOOD_FEAST ? 0.5 : 1.0);
                        }else if((double)player.attributes.surgical > (double)player.attributes.getMaxSurgical()*0.25 && (double)player.attributes.surgical <= (double)player.attributes.getMaxSurgical()*0.7){
                            player.attributes.surgical += 1.5 * (rule == EnumRules.BLOOD_FEAST ? 0.5 : 1.0);
                        }else{
                            player.attributes.surgical += 1.7 * (rule == EnumRules.BLOOD_FEAST ? 0.5 : 1.0);
                        }
                    }
                    if(player.attributes.surgical == 0 && !player.state.contains(EnumPlayerState.SOUL_BROKEN) && !player.isEvil){
                        EffectBrokenSoul.addPlayer(player, 4);
                        player.attributes.surgical = 0.1f;
                    }
                    if(!player.isEvil && player.attributes.surgical > player.attributes.getMaxSurgical()){
                        player.attributes.surgical = player.attributes.getMaxSurgical();
                    }
                    if(player.isEvil && player.attributes.surgical < 0){
                        player.attributes.surgical = 0;
                    }
                    if (!player.isEvil && player.lastHurt + (long)player.attributes.getRestTime() <= new Date().getTime()) {
                        if(player.canSuperRegen && !player.isSuperRegened){
                            player.blood += player.attributes.getMaxBlood() * 0.02;
                        }else {
                            player.blood += player.attributes.getMaxBlood() * 0.01;
                        }
                    }
                    // Update client attributes display
                    if(player.state.contains(EnumPlayerState.INFO_OVERLOADED)){
                        Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackageAttribute(
                                player.attributes.speed + "." +
                                        new Random().nextInt(0,60) + "." +
                                        new Random().nextInt(0,60) + "." +
                                        new Random().nextInt(0,60) + "." +
                                        new Random().nextInt(0,60) + "." +
                                        new Random().nextInt(0,60) + "." +
                                        new Random().nextInt(0,60) + "." +
                                        new Random().nextInt(0,60) + "." +
                                        true
                        ));
                    }else {
                        Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackageAttribute(
                                player.attributes.speed + "." +
                                        player.attributes.health + "." +
                                        player.attributes.strength + "." +
                                        player.attributes.defence + "." +
                                        player.attributes.mental + "." +
                                        player.attributes.IQ + "." +
                                        player.attributes.knowledge + "." +
                                        player.attributes.surgical + "." +
                                        ((player.lastAttack + (long)(1 / player.attributes.getAttackSpeed())) * 1000 < new Date().getTime())
                        ));
                    }
                    // Update client effects display
                    ArrayList<String> msg = new ArrayList<>();
                    if(player.state.contains(EnumPlayerState.LOCKED)){msg.add(EffectLocked.message);}
                    if(player.state.contains(EnumPlayerState.SOUL_BROKEN)){msg.add(EffectBrokenSoul.message);}
                    if(player.state.contains(EnumPlayerState.CAN_NOT_SELECT)){msg.add(EffectCanNotSelect.message);}
                    if(player.state.contains(EnumPlayerState.INFO_OVERLOADED)){msg.add(EffectInfoOverloaded.message);}
                    if(player.state.contains(EnumPlayerState.OVERLOADED)){msg.add("Overloaded");}
                    if(EffectDeepVally.affectedPlayers.contains(player)){msg.add(EffectDeepVally.message);}
                    if(EffectPoisoned.affectedPlayers.contains(player)){msg.add(EffectBrokenSoul.message);}
                    if(player.state.contains(EnumPlayerState.HUNTING)){msg.add("Hunting");}
                    StringBuilder str = new StringBuilder();
                    for (String s:msg) {
                        if(msg.indexOf(s) == msg.size()-1){
                            str.append(s);
                        }else {
                            str.append(s).append(".");
                        }
                    }
                    Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackagePlayerState(str.toString()));
                }
                lastTime = new Date().getTime();
            }
            for (int j = 0; j < Game.instance.humans.size(); j++) {
                GamePlayer human = Game.instance.humans.get(j);
                if(!human.isSuperRegened && human.blood > (float)human.attributes.getMaxBlood()*0.1 && human.blood < (float) human.attributes.getMaxBlood()*0.5){
                    human.canSuperRegen = true;
                }
            }
            // Set base value
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
            // Check overload
            if (Game.instance.evil.attributes.surgical >= Game.instance.evil.attributes.maxtire && !Game.instance.evil.state.contains(EnumPlayerState.OVERLOADED)) {
                Game.instance.evil.state.remove(EnumPlayerState.HUNTING);
                Game.instance.evil.state.add(EnumPlayerState.OVERLOADED);
                Game.instance.evil.attributes.speed -= 20;
                Game.instance.evil.attributes.health -= 20;
                Game.instance.evil.attributes.strength -= 20;
                Game.instance.evil.attributes.defence -= 20;
                Game.instance.evil.attributes.mental -= 20;
                Game.instance.evil.attributes.IQ -= 20;
                Game.instance.evil.attributes.knowledge -= 20;
                Game.instance.evil.playerInstance.sendMessage(new TextComponent("[SYSTEM]You have overloaded!"), ChatType.SYSTEM, Util.NIL_UUID);
            }
            if (Game.instance.evil.attributes.surgical <= 0 && Game.instance.evil.state.contains(EnumPlayerState.OVERLOADED)) {
                Game.instance.evil.state.remove(EnumPlayerState.OVERLOADED);
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
        Game.stage = null;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Game.instance = null;
            }
        }, 1000);
    }

    public static boolean isInGame(Entity e){
        return instance.searchPlayerByName(e.getDisplayName().getString()) != null;
    }

    public static void addRule(int ID){
        if(ID == RuleBloodFeast.ID){
            Game.rule = EnumRules.BLOOD_FEAST;
        }
    }

    public static boolean isReady(){
        for (GamePlayer player : Game.instance.players){
            if (player.attributes == null){
                return false;
            }
        }
        return Game.rule != null;
    }
}
