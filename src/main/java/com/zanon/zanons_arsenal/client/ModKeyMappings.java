package com.zanon.zanons_arsenal.client;

import org.lwjgl.glfw.GLFW;

import com.zanon.zanons_arsenal.ZanonsArsenal;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;

public final class ModKeyMappings {
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(ZanonsArsenal.MOD_ID, "arsenal"));

    public static final Lazy<KeyMapping> SKILL = Lazy.of(() -> new KeyMapping(
            "key.zanons_arsenal.skill",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            CATEGORY
    ));

    public static final Lazy<KeyMapping> MODE = Lazy.of(() -> new KeyMapping(
            "key.zanons_arsenal.mode",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            CATEGORY
    ));

    private ModKeyMappings() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        event.registerCategory(CATEGORY);
        event.register(SKILL.get());
        event.register(MODE.get());
    }
}
