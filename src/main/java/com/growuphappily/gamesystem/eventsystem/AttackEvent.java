package com.growuphappily.gamesystem.eventsystem;

import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraftforge.eventbus.api.Event;

public class AttackEvent extends Event {
    public GamePlayer player;
    public GamePlayer target;

    public AttackEvent(GamePlayer player, GamePlayer beingAttacked) {
        this.player = player;
        this.target = beingAttacked;
    }
}
