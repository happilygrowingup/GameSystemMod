package com.growuphappily.gamesystem.system;

import com.growuphappily.gamesystem.enums.EnumGameMode;
import net.minecraft.server.MinecraftServer;

public class Game extends Thread{
    public EnumGameMode gameMode;
    public GamePlayer[] players;
    public static Game instance;
    public Game(EnumGameMode gameMode, GamePlayer[] players){
        this.gameMode = gameMode;
        this.players = players;
        instance = this;
    }
}
