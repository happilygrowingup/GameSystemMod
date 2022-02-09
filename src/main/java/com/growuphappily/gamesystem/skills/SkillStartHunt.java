package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.models.EvilEternalHunter;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import org.apache.logging.log4j.LogManager;

import java.util.Objects;

public class SkillStartHunt extends Skill {
    public SkillStartHunt() {
        consumption = 50;
    }

    public void run(GamePlayer player) {
        if (Objects.requireNonNull(Game.getGameByPlayerName(player.playerInstance.getDisplayName().getString())).evil instanceof EvilEternalHunter evil) {
            boolean b = super.preRun(player);
            LogManager.getLogger().info(b);
            if (!b) {
                evil.openHuntMode();
            }
        }
    }
}
