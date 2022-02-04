package com.growuphappily.gamesystem.eventsystem;

import com.growuphappily.gamesystem.system.GamePlayer;

public class AttackEvent extends Event{
    public boolean isCanceled = false;
    public GamePlayer player;
    public GamePlayer target;

    public AttackEvent(GamePlayer player, GamePlayer beingAttacked) {
        this.player = player;
        this.target = beingAttacked;
    }
}
