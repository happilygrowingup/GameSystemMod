package com.growuphappily.gamesystem.eventsystem;

public abstract class Event {
    public boolean isCancelled;
    public boolean isCancelled(){ return isCancelled;}

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public static boolean handle(){return true;}
}
