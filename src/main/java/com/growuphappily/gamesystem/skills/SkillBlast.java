package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.enums.EnumFactionCategory;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class SkillBlast extends Skill{
    public static ArrayList<GamePlayer> openedPlayers = new ArrayList<>();

    public SkillBlast(){
        consumption = 100;
        category = EnumFactionCategory.GUN;
    }

    public void run(GamePlayer player){
        if(super.preRun(player)){
            return;
        }
        openedPlayers.add(player);
    }
}
