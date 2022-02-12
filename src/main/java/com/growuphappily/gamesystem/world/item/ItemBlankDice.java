package com.growuphappily.gamesystem.world.item;

import com.growuphappily.gamesystem.entity.EntityMoonGas;
import com.growuphappily.gamesystem.entity.EntityTypeRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;

public class ItemBlankDice extends Item {
    public ItemBlankDice() {
        super(new Properties().tab(CreativeModeTab.TAB_COMBAT));
    }
}
