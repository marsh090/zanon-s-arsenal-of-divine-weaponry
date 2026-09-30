package com.zanon.zanons_arsenal.registry;

import java.util.List;
import java.util.Optional;

import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.weapon.MurasamaItem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.item.SwingAnimationType;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.SwingAnimation;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ZanonsArsenal.MOD_ID);

    public static final DeferredItem<MurasamaItem> MURASAMA = ITEMS.registerItem("murasama", MurasamaItem::new, props -> props
            .stacksTo(1)
            .enchantable(15)
            .fireResistant()
            .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
            .component(ModComponents.SHEATHED.get(), true)
            .component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers())
            .component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.NONE, 6))
            .delayedComponent(DataComponents.BLOCKS_ATTACKS, context -> new BlocksAttacks(
                    0.25F,
                    1.0F,
                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                    Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                    Optional.of(SoundEvents.SHIELD_BLOCK),
                    Optional.of(SoundEvents.SHIELD_BREAK)
            ))
    );

    public static final DeferredItem<Item> VAULT_KEY = ITEMS.registerSimpleItem("murasama_vault_key", props -> props.stacksTo(16));

    public static final DeferredItem<BlockItem> DARK_METAL = ITEMS.registerSimpleBlockItem(ModBlocks.DARK_METAL);
    public static final DeferredItem<BlockItem> RUSTED_METAL = ITEMS.registerSimpleBlockItem(ModBlocks.RUSTED_METAL);
    public static final DeferredItem<BlockItem> DARK_METAL_SLAB = ITEMS.registerSimpleBlockItem(ModBlocks.DARK_METAL_SLAB);
    public static final DeferredItem<BlockItem> FACTORY_LIGHT = ITEMS.registerSimpleBlockItem(ModBlocks.FACTORY_LIGHT);
    public static final DeferredItem<BlockItem> MURASAMA_VAULT = ITEMS.registerSimpleBlockItem(ModBlocks.MURASAMA_VAULT);
    public static final DeferredItem<BlockItem> OPENED_MURASAMA_VAULT = ITEMS.registerSimpleBlockItem(ModBlocks.OPENED_MURASAMA_VAULT);

    private ModItems() {
    }

    private static net.minecraft.world.item.component.ItemAttributeModifiers ItemAttributeModifiers() {
        return net.minecraft.world.item.component.ItemAttributeModifiers.builder().build();
    }
}
