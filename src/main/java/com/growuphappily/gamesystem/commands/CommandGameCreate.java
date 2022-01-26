package com.growuphappily.gamesystem.commands;

import com.growuphappily.gamesystem.enums.EnumGameMode;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class CommandGameCreate implements Command<CommandSourceStack> {
    public static CommandGameCreate instance = new CommandGameCreate();
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        context.getSource().sendSuccess(new TextComponent("Test"),false);
        return 0;
    }
}
