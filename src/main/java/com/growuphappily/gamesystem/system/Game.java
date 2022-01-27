package com.growuphappily.gamesystem.system;

import com.growuphappily.gamesystem.enums.EnumGameMode;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Random;

@Mod.EventBusSubscriber
public class Game extends Thread{
    public EnumGameMode gameMode;
    public ArrayList<GamePlayer> players = new ArrayList<>();
    public static Game instance;
    public static boolean isStarted = false;
    public static MinecraftServer server;
    public GamePlayer evil;
    public ArrayList<GamePlayer> humans;

    public Game(EnumGameMode gameMode){
        isStarted = false;
        this.gameMode = gameMode;
        Random rand = new Random();
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            players.add(new GamePlayer(p));
        }
        evil = players.get(rand.nextInt(server.getPlayerCount()));
        evil.isEvil = true;

        instance = this;
    }

    public void addPlayerAttributes(ServerPlayer p, Attributes attributes){
        searchPlayerByName(p.getDisplayName().getString()).addAttributes(attributes);
        broadcast(p.getDisplayName().getString() + "has finished selecting.");
    }

    public void start(){

    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event){
        if(Game.isStarted){

        }
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
}
