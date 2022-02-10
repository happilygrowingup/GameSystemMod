package com.growuphappily.gamesystem.eventsystem;

import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraftforge.eventbus.api.Event;

public class AttackSucceedEvent extends Event {
    public GamePlayer player;
    public GamePlayer target;
    public float damage;
    public AttackSucceedEvent(GamePlayer player, GamePlayer target, float damage){
        this.player = player;
        this.target = target;
        this.damage = damage;
    }

}
