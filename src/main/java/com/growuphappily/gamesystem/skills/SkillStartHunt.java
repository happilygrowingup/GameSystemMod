package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.models.EvilEternalHunter;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;

public class SkillStartHunt extends Skill{
    public SkillStartHunt(){
        consumption = 50;
    }
    @Override
    public void run(GamePlayer player){
        if(Game.instance.evil instanceof EvilEternalHunter evil){
            super.run(player);
            evil.openHuntMode();
        }
    }
}
