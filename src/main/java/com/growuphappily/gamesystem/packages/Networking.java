package com.growuphappily.gamesystem.packages;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int ID(){
        return ID++;
    }

    public static void registerMessage(){
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("gamesystem","network"),
                () -> VERSION,
                (s) -> true,
                (s) -> true
        );
        INSTANCE.registerMessage(
                ID(),
                PackageOpenGUI.class,
                PackageOpenGUI::toBytes,
                PackageOpenGUI::new,
                PackageOpenGUI::handler
        );
    }
}
