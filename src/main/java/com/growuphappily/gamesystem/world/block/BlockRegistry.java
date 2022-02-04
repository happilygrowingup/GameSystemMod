package com.growuphappily.gamesystem.world.block;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "gamesystem");
    public static RegistryObject<Block> surgicalPoint = BLOCKS.register("surgical_point", BlockSurgicalPoint::new);
}
