package com.growuphappily.gamesystem;

import com.growuphappily.gamesystem.world.block.BlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("gamesystem")
public class GameSystem {
    public GameSystem(){
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
