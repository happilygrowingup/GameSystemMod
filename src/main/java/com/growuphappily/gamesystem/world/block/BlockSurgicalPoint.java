package com.growuphappily.gamesystem.world.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

public class BlockSurgicalPoint extends Block {
    public BlockSurgicalPoint() {
        super(Properties.of(Material.ICE).destroyTime(5));
    }
}
