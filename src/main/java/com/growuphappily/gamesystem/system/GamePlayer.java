package com.growuphappily.gamesystem.system;


import com.growuphappily.gamesystem.enums.EnumAttackResult;
import com.growuphappily.gamesystem.enums.EnumCastResult;
import com.growuphappily.gamesystem.enums.EnumPlayerState;
import net.minecraft.world.entity.player.Player;

public class GamePlayer {
    public Attributes attributes;
    public Player playerInstance;
    public EnumPlayerState state;
    public int blood;
    public GamePlayer(Attributes attributes, Player playerInstance){
        this.attributes = attributes;
        this.playerInstance = playerInstance;
        this.blood = attributes.getMaxBlood();
    }

    public EnumAttackResult Attack(GamePlayer beingAttacked){
        if(((Dice.onedX(100) + this.attributes.speed)*0.2) >= (20 + beingAttacked.attributes.speed*0.4)){
            if(Dice.onedX(100) >= (Dice.onedX(100) + beingAttacked.attributes.defence - (attributes.strength * 0.1))){
                beingAttacked.blood -= (int)((attributes.strength*0.15) + 5 - (beingAttacked.attributes.defence * 0.1));
                return EnumAttackResult.CRITICAL;
            }else {
                beingAttacked.blood -= (int)((attributes.strength*0.1) + 5 - (beingAttacked.attributes.defence * 0.1));
                return EnumAttackResult.NORMAL;
            }
        }else{
            return EnumAttackResult.MISSED;
        }
    }

    public EnumCastResult tryCast(){
        int d = Dice.onedX(100);
        if(d <= 60 + (attributes.IQ * 0.5)){
            if(d == 1){
                return EnumCastResult.BIG_SUCCESS;
            }
            return EnumCastResult.SUCCESS;
        }else{
            if(d == 100){
                return EnumCastResult.BIG_FAIL;
            }
            return EnumCastResult.FAIL;
        }
    }
}