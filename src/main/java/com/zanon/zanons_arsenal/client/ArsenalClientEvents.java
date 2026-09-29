package com.zanon.zanons_arsenal.client;

import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.network.ModePayload;
import com.zanon.zanons_arsenal.network.SkillPayload;
import com.zanon.zanons_arsenal.weapon.ArsenalItem;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = ZanonsArsenal.MOD_ID, value = net.neoforged.api.distmarker.Dist.CLIENT)
public final class ArsenalClientEvents {
    private ArsenalClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }
        ItemStack stack = minecraft.player.getMainHandItem();
        if (!(stack.getItem() instanceof ArsenalItem weapon)) {
            return;
        }
        while (ModKeyMappings.SKILL.get().consumeClick()) {
            if (weapon.hasSkill(stack)) {
                ClientPacketDistributor.sendToServer(new SkillPayload());
            }
        }
        while (ModKeyMappings.MODE.get().consumeClick()) {
            if (weapon.hasModes(stack)) {
                ClientPacketDistributor.sendToServer(new ModePayload());
            }
        }
    }
}
