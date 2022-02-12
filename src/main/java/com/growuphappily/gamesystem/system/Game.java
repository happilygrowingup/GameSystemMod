package com.growuphappily.gamesystem.system;

import com.growuphappily.gamesystem.effects.*;
import com.growuphappily.gamesystem.enums.*;
import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageAttribute;
import com.growuphappily.gamesystem.packages.PackageGameStart;
import com.growuphappily.gamesystem.packages.PackagePlayerState;
import com.growuphappily.gamesystem.rules.RuleBloodFeast;
import com.growuphappily.gamesystem.world.block.BlockSurgicalPoint;
import com.growuphappily.gamesystem.world.item.ItemRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Mod.EventBusSubscriber
public class Game {
    private static final boolean DEBUG = false;
    public EnumGameMode gameMode;
    public ArrayList<GamePlayer> players = new ArrayList<>();
    public static ArrayList<Game> instances = new ArrayList<>();
    public boolean isStarted;
    public static boolean clientIsStarted = false;
    public static MinecraftServer server;
    public GamePlayer evil;
    public ArrayList<GamePlayer> humans = new ArrayList<>();
    private long lastTime = 0;
    public EnumGameStage stage;
    public EnumRules rule;
    public long lastThree = 0;
    public ArrayList<BlockPos> points = new ArrayList<>();
    public ServerLevel world;
    public long stage3StartTime;
    public String name;

    public Game(EnumGameMode gameMode, String name, ArrayList<ServerPlayer> serverPlayers) {
        instances.add(this);
        this.name = name;
        isStarted = false;
        this.gameMode = gameMode;
        Random rand = new Random();
        for (ServerPlayer p : serverPlayers) {
            players.add(new GamePlayer(p));
        }
        evil = players.get(rand.nextInt(server.getPlayerCount()));
        evil.isEvil = true;
        for (GamePlayer player : players) {
            if (!player.isEvil) {
                humans.add(player);
            }
        }
        for (GamePlayer human : humans) {
            human.playerInstance.sendMessage(new TextComponent("Your identity is human. Select your Attributes and factions."), ChatType.SYSTEM, Util.NIL_UUID);
        }
        evil.playerInstance.sendMessage(new TextComponent("Your identity is evil. Select distorted rules, place surgical points."), ChatType.SYSTEM, Util.NIL_UUID);
        evil.playerInstance.getInventory().add(new ItemStack(ItemRegistry.surgicalPoint.get(), 4, null));
        points = new ArrayList<>();
    }

    public void addPlayerAttributes(ServerPlayer p, Attributes attributes) {
        searchPlayerByName(p.getDisplayName().getString()).addAttributes(attributes);
        broadcast(p.getDisplayName().getString() + " has finished selecting.");
        if (isReady()) {
            broadcast("Game is ready.Run \"/game start\" to start game.");
        }
    }

    public void addEvil(GamePlayer p) {
        p.isEvil = true;
        players.set(players.indexOf(evil), p);
        evil = p;
        broadcast(p.playerInstance.getDisplayName().getString() + " has finished selecting.");
        if (isReady()) {
            broadcast("Game is ready.Run \"/game start\" to start game.");
        }
    }

    public void start() {
        if (!isReady()) {
            broadcast("Game is not ready!");
            return;
        }
        isStarted = true;
        for (GamePlayer player : players) {
            player.blood = player.attributes.getMaxBlood();
            player.attributes.originalSpeed = Objects.requireNonNull(player.playerInstance.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED)).getBaseValue();
            if (!player.isEvil) {
                player.attributes.surgical = player.attributes.getMaxSurgical();
            }
            Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackageGameStart(true));
        }
        evil.attributes.surgical = 0;
        broadcast("Game Started with stage 1!");
        broadcast(evil.playerInstance.getDisplayName().getString() + " is EVIL!!BE CAREFUL!!");
        evil.attributes.speed -= 4;
        evil.attributes.health += 40;
        evil.attributes.strength += 5;
        evil.attributes.defence += 10;
        evil.attributes.maxtire += 40;
        stage = EnumGameStage.STAGE_ONE;
        for (GamePlayer player : humans) {
            player.playerInstance.getInventory().add(new ItemStack(ItemRegistry.blankDice.get(), 3));
            if(player.factions.contains(EnumFactionCategory.FENCING)){
                player.attributes.speed += 10;
                player.attributes.defence += 8;
                player.playerInstance.getInventory().add(new ItemStack(Items.DIAMOND_SWORD));
            }
            if(player.factions.contains(EnumFactionCategory.GUN)){
                player.playerInstance.getInventory().add(new ItemStack(ItemRegistry.gun.get()));
                player.playerInstance.getInventory().add(new ItemStack(ItemRegistry.bullet.get(), 10));
            }
        }
        evil.playerInstance.getInventory().add(new ItemStack(ItemRegistry.blankDice.get(), 5));
        for(GamePlayer player : players){
            player.playerInstance.getInventory().add(new ItemStack(ItemRegistry.motionDice.get(), (int)Math.floor(player.attributes.strength/20.)));
            player.playerInstance.getInventory().add(new ItemStack(ItemRegistry.skillDice.get(), (int)Math.floor(player.attributes.IQ/20.)));
            player.playerInstance.getInventory().add(new ItemStack(ItemRegistry.converseDice.get(), (int)Math.floor(player.attributes.knowledge/10.)));
        }
    }

    @SubscribeEvent
    public static void update(TickEvent.ServerTickEvent event) {
        for (int i = 0; i < Game.instances.size(); i++) {
            Game game = Game.instances.get(i);
            if (game.isStarted) {
                if (new Date().getTime() >= game.lastTime + 1000) {  //Exec by seconds
                    for (int j = 0; j < game.players.size(); j++) {
                        GamePlayer player = game.players.get(j);
                        if (player.isEvil && player.attributes.surgical > 0 && !player.state.contains(EnumPlayerState.HUNTING)) {
                            if (game.rule == EnumRules.BLOOD_FEAST) {
                                player.attributes.surgical -= player.attributes.getSurgicalRegenSpeed() * 0.8f;
                            } else {
                                player.attributes.surgical -= player.attributes.getSurgicalRegenSpeed();
                            }
                        }
                        if (player.state.contains(EnumPlayerState.HUNTING)) {
                            player.attributes.surgical += 2;
                        }
                        if (!player.isEvil && !player.state.contains(EnumPlayerState.SOUL_BROKEN)) {
                            if (player.attributes.surgical <= player.attributes.getMaxSurgical() * 0.25 && player.attributes.surgical > 0.) {
                                player.attributes.surgical += 1.2 * (game.rule == EnumRules.BLOOD_FEAST ? 0.5 : 1.0);
                            } else if ((double) player.attributes.surgical > (double) player.attributes.getMaxSurgical() * 0.25 && (double) player.attributes.surgical <= (double) player.attributes.getMaxSurgical() * 0.7) {
                                player.attributes.surgical += 1.5 * (game.rule == EnumRules.BLOOD_FEAST ? 0.5 : 1.0);
                            } else {
                                player.attributes.surgical += 1.7 * (game.rule == EnumRules.BLOOD_FEAST ? 0.5 : 1.0);
                            }
                        }
                        if (player.attributes.surgical == 0 && !player.state.contains(EnumPlayerState.SOUL_BROKEN) && !player.isEvil) {
                            EffectBrokenSoul.addPlayer(player, 4);
                            player.attributes.surgical = 0.1f;
                        }
                        if (!player.isEvil && player.attributes.surgical > player.attributes.getMaxSurgical()) {
                            player.attributes.surgical = player.attributes.getMaxSurgical();
                        }
                        if (player.isEvil && player.attributes.surgical < 0) {
                            player.attributes.surgical = 0;
                        }
                        if (!player.isEvil && player.lastHurt + (long) player.attributes.getRestTime() <= new Date().getTime()) {
                            if (player.canSuperRegen && !player.isSuperRegened) {
                                player.blood += player.attributes.getMaxBlood() * 0.02;
                            } else {
                                player.blood += player.attributes.getMaxBlood() * 0.01;
                            }
                        }
                        if (player.blood < player.attributes.getMaxBlood() * 0.1f && !player.state.contains(EnumPlayerState.SOUL_BROKEN)) {
                            player.state.add(EnumPlayerState.SOUL_BROKEN);
                        }
                        if (player.blood >= player.attributes.getMaxBlood() * 0.1f && !EffectBrokenSoul.affectedPlayers.contains(player)) {
                            player.state.remove(EnumPlayerState.SOUL_BROKEN);
                        }
                        // Update client attributes display
                        if (player.state.contains(EnumPlayerState.INFO_OVERLOADED)) {
                            Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackageAttribute(
                                    player.attributes.speed + "." +
                                            new Random().nextInt(0, 60) + "." +
                                            new Random().nextInt(0, 60) + "." +
                                            new Random().nextInt(0, 60) + "." +
                                            new Random().nextInt(0, 60) + "." +
                                            new Random().nextInt(0, 60) + "." +
                                            new Random().nextInt(0, 60) + "." +
                                            new Random().nextInt(0, 60) + "." +
                                            true
                            ));
                        } else {
                            Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackageAttribute(
                                    player.attributes.speed + "." +
                                            player.attributes.health + "." +
                                            player.attributes.strength + "." +
                                            player.attributes.defence + "." +
                                            player.attributes.mental + "." +
                                            player.attributes.IQ + "." +
                                            player.attributes.knowledge + "." +
                                            player.attributes.surgical + "." +
                                            ((player.lastAttack + (long) (1 / player.attributes.getAttackSpeed())) * 1000 < new Date().getTime())
                            ));
                        }
                        // Update client effects display
                        ArrayList<String> msg = new ArrayList<>();
                        if (player.state.contains(EnumPlayerState.LOCKED)) {
                            msg.add(EffectLocked.message);
                        }
                        if (player.state.contains(EnumPlayerState.SOUL_BROKEN)) {
                            msg.add(EffectBrokenSoul.message);
                        }
                        if (player.state.contains(EnumPlayerState.CAN_NOT_SELECT)) {
                            msg.add(EffectCanNotSelect.message);
                        }
                        if (player.state.contains(EnumPlayerState.INFO_OVERLOADED)) {
                            msg.add(EffectInfoOverloaded.message);
                        }
                        if (player.state.contains(EnumPlayerState.OVERLOADED)) {
                            msg.add("Overloaded");
                        }
                        if (EffectAbyss.affectedPlayers.contains(player)) {
                            msg.add(EffectAbyss.message);
                        }
                        if (EffectPoisoned.affectedPlayers.contains(player)) {
                            msg.add(EffectBrokenSoul.message);
                        }
                        if (player.state.contains(EnumPlayerState.HUNTING)) {
                            msg.add("Hunting");
                        }
                        StringBuilder str = new StringBuilder();
                        for (String s : msg) {
                            if (msg.indexOf(s) == msg.size() - 1) {
                                str.append(s);
                            } else {
                                str.append(s).append(".");
                            }
                        }
                        Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackagePlayerState(str.toString()));
                    }
                    game.lastTime = new Date().getTime();
                }
                if (game.lastThree + 3000 <= new Date().getTime()) {
                    for (int k = 0; k < game.players.size(); k++) {
                        GamePlayer player = game.players.get(k);
                        if (!player.isEvil && player.attributes.surgical != 0) {
                            if (player.attributes.surgical <= player.attributes.getMaxSurgical() * 0.25f) {
                                player.attributes.surgical += player.attributes.mental * 0.06f;
                            } else if (player.attributes.surgical > player.attributes.getMaxSurgical() * 0.25f && player.attributes.surgical <= player.attributes.getMaxSurgical() * 0.7) {
                                player.attributes.surgical += player.attributes.mental * 0.08f;
                            } else {
                                player.attributes.surgical += player.attributes.mental * 0.1f;
                            }
                        }
                    }
                    game.lastThree = new Date().getTime();
                }
                for (int k = 0; k < game.humans.size(); k++) {
                    GamePlayer human = game.humans.get(k);
                    if (!human.isSuperRegened && human.blood > (float) human.attributes.getMaxBlood() * 0.1 && human.blood < (float) human.attributes.getMaxBlood() * 0.5) {
                        human.canSuperRegen = true;
                        human.playerInstance.sendMessage(new TextComponent("[SYSTEM] You have gained a super regen"), ChatType.SYSTEM, Util.NIL_UUID);
                    }
                }
                // Set base value
                for (int j = 0; j < game.players.size(); j++) {
                    GamePlayer player = game.players.get(j);
                    AttributeInstance attrMaxHealth = player.playerInstance.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
                    assert attrMaxHealth != null;
                    attrMaxHealth.setBaseValue(player.attributes.getMaxBlood());
                    AttributeInstance attrMoveSpeed = player.playerInstance.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
                    assert attrMoveSpeed != null;
                    attrMoveSpeed.setBaseValue(player.attributes.originalSpeed + player.attributes.speed * player.attributes.originalSpeed * 0.01);
                    // AttributeInstance attrAttackSpeed = player.playerInstance.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED);
                    // assert attrAttackSpeed != null;
                    // attrAttackSpeed.setBaseValue(attrAttackSpeed.getBaseValue() + player.attributes.getAttackSpeed());
                    player.playerInstance.setHealth(player.blood);
                }
                // Check overload
                if (game.evil.attributes.surgical >= game.evil.attributes.maxtire && !game.evil.state.contains(EnumPlayerState.OVERLOADED)) {
                    game.evil.state.remove(EnumPlayerState.HUNTING);
                    game.evil.state.add(EnumPlayerState.OVERLOADED);
                    game.evil.attributes.speed -= 20;
                    game.evil.attributes.health -= 20;
                    game.evil.attributes.strength -= 20;
                    game.evil.attributes.defence -= 20;
                    game.evil.attributes.mental -= 20;
                    game.evil.attributes.IQ -= 20;
                    game.evil.attributes.knowledge -= 20;
                    game.evil.playerInstance.sendMessage(new TextComponent("[SYSTEM]You have overloaded!"), ChatType.SYSTEM, Util.NIL_UUID);
                }
                if (game.evil.attributes.surgical <= 0 && game.evil.state.contains(EnumPlayerState.OVERLOADED)) {
                    game.evil.state.remove(EnumPlayerState.OVERLOADED);
                    game.evil.attributes.speed += 20;
                    game.evil.attributes.health += 20;
                    game.evil.attributes.strength += 20;
                    game.evil.attributes.defence += 20;
                    game.evil.attributes.mental += 20;
                    game.evil.attributes.IQ += 20;
                    game.evil.attributes.knowledge += 20;
                    game.evil.playerInstance.sendMessage(new TextComponent("[SYSTEM]You have out of overload."), ChatType.SYSTEM, Util.NIL_UUID);
                }
                //Check death
                for (int j = 0; j < game.players.size() && !DEBUG; j++) {
                    GamePlayer gamePlayer = game.players.get(j);
                    if (gamePlayer.playerInstance != server.getPlayerList().getPlayerByName(gamePlayer.playerInstance.getDisplayName().getString()) || gamePlayer.blood <= 0) {
                        gamePlayer.playerInstance.setGameMode(GameType.SPECTATOR);
                        Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> gamePlayer.playerInstance), new PackageGameStart(false));
                        if (gamePlayer.isEvil) {
                            game.broadcast("Human win!");
                            game.gameOver();
                        } else {
                            game.humans.remove(gamePlayer);
                        }
                    }
                }
                //Check winner
                if (game.humans.size() == 0 && !DEBUG) {
                    game.broadcast("Evil win!");
                    game.gameOver();
                }
                if (game.stage == EnumGameStage.STAGE_THREE) {
                    if (game.stage3StartTime + 240000 <= new Date().getTime()) {
                        game.broadcast("Human win!");
                        game.gameOver();
                    }
                }
                //Check surgical point
                if (game.stage == EnumGameStage.STAGE_ONE) {
                    for (int j = 0; j < game.points.size(); j++) {
                        if (game.world.getBlockState(game.points.get(j)).isAir()) {
                            game.broadcast("The surgical point at [" + game.points.get(j).toString() + "] have been destroyed!");
                            game.evil.attributes.surgical += 50;
                            if (game.points.size() == 1) {
                                game.broadcast("Game Stage 2");
                                game.stage = EnumGameStage.STAGE_TWO;
                                game.evil.attributes.speed += 4;
                                game.evil.attributes.health -= 40;
                                game.evil.attributes.strength -= 5;
                                game.evil.attributes.defence -= 10;
                                game.evil.attributes.maxtire -= 40;
                            }
                            game.points.remove(j);
                            break;
                        }
                    }
                }
                //Check stage 3
                if (game.stage == EnumGameStage.STAGE_TWO && game.humans.size() == 1) {
                    game.stage3StartTime = new Date().getTime();
                    game.broadcast("Game stage 3");
                    game.stage = EnumGameStage.STAGE_THREE;
                    game.evil.attributes.speed += 5;
                    game.evil.attributes.health += 40;
                    game.evil.attributes.strength += 5;
                    game.evil.attributes.defence += 10;
                }
            }
        }
    }

    public void broadcast(String msg) {
        for (GamePlayer player : players) {
            player.playerInstance.sendMessage(new TextComponent("[SYSTEM] " + msg), ChatType.SYSTEM, Util.NIL_UUID);
        }
    }

    public GamePlayer searchPlayerByName(String name) {
        for (GamePlayer p : players) {
            if (p.playerInstance.getDisplayName().getString().equals(name)) {
                return p;
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onServerStop(ServerStoppingEvent event) {
        for (Game game : Game.instances) {
            game.isStarted = false;
        }
    }

    public void gameOver() {
        isStarted = false;
        for (GamePlayer player : players) {
            Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.playerInstance), new PackageGameStart(false));
        }
        stage = null;
        rule = null;
        Game game = this;
        instances.remove(this);
    }

    public void addRule(int ID) {
        if (ID == RuleBloodFeast.ID) {
            rule = EnumRules.BLOOD_FEAST;
        }
    }

    public boolean isReady() {
        for (GamePlayer player : players) {
            if (player.attributes == null) {
                return false;
            }
        }
        if (evil.attributes == null) {
            return false;
        }
        if (points.size() < 4) {
            return false;
        }
        return rule != null;
    }

    public static Game getGameByPlayerName(String name) {
        for (Game game : Game.instances) {
            if (game.searchPlayerByName(name) != null) {
                return game;
            }
        }
        return null;
    }

    @Nullable
    public static Game getGameByName(@NotNull String name) {
        for (Game game : Game.instances) {
            if (Objects.equals(game.name, name)) {
                return game;
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        for (int i = 0; i < Game.instances.size(); i++) {
            Game game = Game.instances.get(i);
            if (!game.isStarted && event.getEntity() instanceof ServerPlayer player) {
                if (player == game.evil.playerInstance && event.getPlacedBlock().getBlock() instanceof BlockSurgicalPoint && game.points.size() < 4) {
                    game.points.add(event.getPos());
                }
            }
        }
    }
}
