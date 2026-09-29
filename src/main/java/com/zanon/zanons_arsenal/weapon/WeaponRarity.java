package com.zanon.zanons_arsenal.weapon;

import net.minecraft.ChatFormatting;

public enum WeaponRarity {
    COMMON(ChatFormatting.WHITE),
    UNCOMMON(ChatFormatting.GREEN),
    RARE(ChatFormatting.BLUE),
    EPIC(ChatFormatting.DARK_PURPLE),
    LEGENDARY(ChatFormatting.GOLD),
    DIVINE(ChatFormatting.LIGHT_PURPLE);

    private final ChatFormatting color;

    WeaponRarity(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting color() {
        return this.color;
    }
}
