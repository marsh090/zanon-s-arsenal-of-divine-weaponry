package com.zanon.zanons_arsenal.weapon;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Shared controls for arsenal weapons. Weapon Skill and Weapon Mode are the mod's default keys.
 */
public interface ArsenalItem {
    WeaponDefinition definition();

    default boolean hasSkill(ItemStack stack) {
        return false;
    }

    default boolean hasModes(ItemStack stack) {
        return false;
    }

    default boolean allowsMelee(ItemStack stack) {
        return true;
    }

    /** Tooltip attack damage, including the player's base 1. */
    default double attackDamage(ItemStack stack) {
        return 0.0D;
    }

    /** Attacks per second. */
    default double attackSpeed(ItemStack stack) {
        return 0.0D;
    }

    default void useSkill(ServerPlayer player, ItemStack stack) {
    }

    default void toggleMode(ServerPlayer player, ItemStack stack) {
    }
}
