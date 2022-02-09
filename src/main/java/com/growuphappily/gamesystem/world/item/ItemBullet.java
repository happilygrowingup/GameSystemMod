package com.growuphappily.gamesystem.world.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ItemBullet extends Item {
    public ItemBullet(){
        super(new Properties().tab(CreativeModeTab.TAB_COMBAT));
    }
}
