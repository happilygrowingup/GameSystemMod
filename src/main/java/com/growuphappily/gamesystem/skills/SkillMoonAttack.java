package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.entity.EntityMoonGas;
import com.growuphappily.gamesystem.entity.EntityTypeRegistry;
import com.growuphappily.gamesystem.enums.EnumFactionCategory;
import com.growuphappily.gamesystem.system.GamePlayer;

public class SkillMoonAttack extends Skill {
    public SkillMoonAttack() {
        consumption = 80;
        category = EnumFactionCategory.FENCING;
    }

    public void run(GamePlayer player) {
        if (super.preRun(player)) {
            return;
        }
        EntityMoonGas shooter = new EntityMoonGas(EntityTypeRegistry.moonGas.get(), player.playerInstance.level);
        shooter.setOwner(player.playerInstance);
        shooter.shootFromRotation(player.playerInstance, player.playerInstance.getXRot(), player.playerInstance.getYRot(), 0f, 5f, 0.5f);
        player.playerInstance.level.addFreshEntity(shooter);
        shooter.moveTo(player.playerInstance.getEyePosition().add(player.playerInstance.getLookAngle()));
    }
}
