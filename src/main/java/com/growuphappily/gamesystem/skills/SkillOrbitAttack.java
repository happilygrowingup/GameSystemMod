package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.enums.EnumFactionCategory;
import com.growuphappily.gamesystem.system.GamePlayer;

public class SkillOrbitAttack extends Skill{

    public SkillOrbitAttack(){
        consumption = 60;
        category = EnumFactionCategory.FENCING;
    }

    public void run(GamePlayer player){

    }
}
