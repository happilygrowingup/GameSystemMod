package com.growuphappily.gamesystem.system;

public class Attributes {
    public int speed;
    public int health;
    public int strength;
    public int defence;
    public int mental;
    public int IQ;
    public int knowledge;
    public int surgical;

    public float getMoveSpeed(){
        return (float) (1+(speed*0.01));
    }

    public float getAttackSpeed(){
        return (float) (0.3 + (speed*0.05));
    }

    public int getMaxBlood(){
        return (health*5) + defence;
    }

    public float getRegenSpeed(){
        return (float) (getMaxBlood()*(100+(health*0.02))/100);
    }

    public float getRestTime(){
        return (float) (10 - (health*0.05));
    }

    public float getMaxSurgical(){
        return (float) (mental + (IQ*0.5) + (knowledge*4));
    }

    public float getSurgicalRegenSpeed(){
        return (float) (1 + (mental * 0.02));
    }
}
