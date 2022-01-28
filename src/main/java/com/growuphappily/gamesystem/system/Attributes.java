package com.growuphappily.gamesystem.system;

public class Attributes {
    public int speed;
    public int health;
    public int strength;
    public int defence;
    public int mental;
    public int IQ;
    public int knowledge;
    public float surgical;
    public float maxtire = 200;  //TODO:temp

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

    public Attributes(int speed, int health, int strength, int defence, int mental, int IQ, int knowledge, int surgical){
        this.speed = speed;
        this.health = health;
        this.strength = strength;
        this.defence = defence;
        this.mental = mental;
        this.IQ = IQ;
        this.knowledge = knowledge;
        this.surgical = surgical;
    }
}
