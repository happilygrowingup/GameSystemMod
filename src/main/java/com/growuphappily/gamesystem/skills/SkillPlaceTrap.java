package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.models.EvilEternalHunter;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;

import java.util.Objects;

public class SkillPlaceTrap extends Skill {
    public SkillPlaceTrap() {
        consumption = 60;
    }

    public void run(GamePlayer player) {
        if (Objects.requireNonNull(Game.getGameByPlayerName(player.playerInstance.getDisplayName().getString())).evil instanceof EvilEternalHunter evil) {
            if (!super.preRun(player)) {
                evil.placeTrap();
            }
        }
    }
}