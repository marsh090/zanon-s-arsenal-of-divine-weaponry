package com.zanon.zanons_arsenal.weapon;

public record WeaponDefinition(String id, WeaponKind kind, WeaponRarity rarity, Acquisition acquisition) {
    public static final WeaponDefinition MURASAMA = new WeaponDefinition(
            "murasama",
            WeaponKind.KATANA,
            WeaponRarity.LEGENDARY,
            Acquisition.FOUND
    );
}
