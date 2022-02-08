package com.growuphappily.gamesystem.world.item;

import com.growuphappily.gamesystem.entity.EntityShooter;
import com.growuphappily.gamesystem.entity.EntityTypeRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemBlankDice extends Item {
    public ItemBlankDice() {
        super(new Properties().tab(CreativeModeTab.TAB_COMBAT));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(!world.isClientSide) {
            EntityShooter shooter = new EntityShooter(EntityTypeRegistry.shooter.get(), world);
            shooter.setOwner(player);
            shooter.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 5f, 0.5f);
            world.addFreshEntity(shooter);
            shooter.moveTo(player.getEyePosition().add(player.getLookAngle()));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
