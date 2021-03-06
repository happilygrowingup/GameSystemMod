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
        INSTANCE.registerMessage(
                ID(),
                PackageAttribute.class,
                PackageAttribute::toBytes,
                PackageAttribute::new,
                PackageAttribute::handler
        );
        INSTANCE.registerMessage(
                ID(),
                PackageEvil.class,
                PackageEvil::toBytes,
                PackageEvil::new,
                PackageEvil::handler
        );
        INSTANCE.registerMessage(
                ID(),
                PackageGameStart.class,
                PackageGameStart::toBytes,
                PackageGameStart::new,
                PackageGameStart::handler
        );
        INSTANCE.registerMessage(
                ID(),
                PackageRule.class,
                PackageRule::toBytes,
                PackageRule::new,
                PackageRule::handler
        );
        INSTANCE.registerMessage(
                ID(),
                PackagePlayerState.class,
                PackagePlayerState::toBytes,
                PackagePlayerState::new,
                PackagePlayerState::handler
        );
        INSTANCE.registerMessage(
                ID(),
                PackageSkills.class,
                PackageSkills::toBytes,
                PackageSkills::new,
                PackageSkills::handler
        );
        INSTANCE.registerMessage(
                ID(),
                PackageFaction.class,
                PackageFaction::toBytes,
                PackageFaction::new,
                PackageFaction::handler
        );
    }
}
