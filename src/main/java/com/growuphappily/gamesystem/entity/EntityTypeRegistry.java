package com.growuphappily.gamesystem.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> entityTypes = DeferredRegister.create(ForgeRegistries.ENTITIES, "gamesystem");
    public static RegistryObject<EntityType<EntityShooter>> shooter = entityTypes.register("shooter", () -> EntityType.Builder.of(EntityShooter::new, MobCategory.MISC).sized(0.125f, 0.125f).build("shooter"));
    public static RegistryObject<EntityType<EntityMoonGas>> moonGas = entityTypes.register("moon_gas", () -> EntityType.Builder.of(EntityMoonGas::new, MobCategory.MISC).sized(2f,0.125f).build("moon_gas"));
}
