package com.zanon.zanons_arsenal;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.zanon.zanons_arsenal.network.ModPayloads;
import com.zanon.zanons_arsenal.registry.ModBlockEntities;
import com.zanon.zanons_arsenal.registry.ModBlocks;
import com.zanon.zanons_arsenal.registry.ModComponents;
import com.zanon.zanons_arsenal.registry.ModItems;
import com.zanon.zanons_arsenal.registry.ModMenus;
import com.zanon.zanons_arsenal.registry.ModParticles;
import com.zanon.zanons_arsenal.registry.ModStructures;
import com.zanon.zanons_arsenal.registry.ModTabs;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(ZanonsArsenal.MOD_ID)
public class ZanonsArsenal {
    public static final String MOD_ID = "zanons_arsenal";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ZanonsArsenal(IEventBus modBus) {
        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModMenus.MENUS.register(modBus);
        ModComponents.COMPONENTS.register(modBus);
        ModParticles.PARTICLE_TYPES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModTabs.TABS.register(modBus);
        ModStructures.register(modBus);
        modBus.addListener(ModPayloads::register);
        modBus.addListener(ModBlockEntities::registerCapabilities);
    }
}
