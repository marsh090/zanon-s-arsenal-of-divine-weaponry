package com.zanon.zanons_arsenal.combat;

import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.weapon.ArsenalItem;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(modid = ZanonsArsenal.MOD_ID)
public final class ArsenalCombatEvents {
    private ArsenalCombatEvents() {
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        ItemStack stack = event.getEntity().getMainHandItem();
        if (stack.getItem() instanceof ArsenalItem weapon && !weapon.allowsMelee(stack)) {
            event.setCanceled(true);
        }
    }
}
