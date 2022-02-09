package com.growuphappily.gamesystem.world.item;

import com.growuphappily.gamesystem.world.block.BlockRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "gamesystem");
    public static RegistryObject<Item> surgicalPoint = ITEMS.register("surgical_point", () -> new BlockItem(BlockRegistry.surgicalPoint.get(), new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static RegistryObject<Item> blankDice = ITEMS.register("blank_dice", ItemBlankDice::new);
    public static RegistryObject<Item> motionDice = ITEMS.register("motion_dice", ItemMotionDice::new);
    public static RegistryObject<Item> skillDice = ITEMS.register("skill_dice", ItemSkillDice::new);
    public static RegistryObject<Item> converseDice = ITEMS.register("converse_dice", ItemConverseDice::new);
    public static RegistryObject<Item> bullet = ITEMS.register("bullet", ItemBullet::new);
}
