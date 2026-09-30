package com.zanon.zanons_arsenal.client;

import com.zanon.zanons_arsenal.ZanonsArsenal;
import com.zanon.zanons_arsenal.client.particle.RedSlashParticle;
import com.zanon.zanons_arsenal.registry.ModMenus;
import com.zanon.zanons_arsenal.registry.ModParticles;

import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = ZanonsArsenal.MOD_ID, value = Dist.CLIENT)
public final class ArsenalClient {
    private ArsenalClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.OPENED_VAULT.get(), OpenedVaultScreen::new);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        ModKeyMappings.register(event);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.RED_SLASH.get(), RedSlashParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerModelProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(SheathedProperty.ID, SheathedProperty.MAP_CODEC);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerModelType skin : event.getSkins()) {
            addSheath(event.getPlayerRenderer(skin));
            addSheath(event.getMannequinRenderer(skin));
        }
    }

    private static void addSheath(AvatarRenderer<?> renderer) {
        if (renderer != null) {
            renderer.addLayer(new SheathLayer(renderer));
        }
    }
}
