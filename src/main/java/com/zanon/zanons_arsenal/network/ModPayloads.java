package com.zanon.zanons_arsenal.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModPayloads {
    private ModPayloads() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SkillPayload.TYPE, SkillPayload.STREAM_CODEC, SkillPayload::handle);
        registrar.playToServer(ModePayload.TYPE, ModePayload.STREAM_CODEC, ModePayload::handle);
    }
}
