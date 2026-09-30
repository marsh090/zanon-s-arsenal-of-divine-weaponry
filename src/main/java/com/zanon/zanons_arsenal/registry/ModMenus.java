package com.zanon.zanons_arsenal.registry;

import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.vault.OpenedVaultMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ZanonsArsenal.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<OpenedVaultMenu>> OPENED_VAULT = MENUS.register(
            "opened_murasama_vault",
            () -> new MenuType<>(OpenedVaultMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private ModMenus() {
    }
}
