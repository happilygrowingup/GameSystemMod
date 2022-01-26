package com.growuphappily.gamesystem.effects;

import com.growuphappily.gamesystem.system.GamePlayer;
import java.lang.Thread;

public class EffectPosioned extends Thread{
    public GamePlayer player;
    public int time;
    public EffectPosioned(GamePlayer player,int time){
        this.player = player;
        this.time = time;
    }

    @Override
    public void run() {

    }
}
