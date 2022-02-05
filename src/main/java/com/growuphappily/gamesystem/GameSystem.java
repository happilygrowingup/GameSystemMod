package com.growuphappily.gamesystem;

import com.growuphappily.gamesystem.world.block.BlockRegistry;
import com.growuphappily.gamesystem.world.item.ItemRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("gamesystem")
public class GameSystem {
    public GameSystem(){
        ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
