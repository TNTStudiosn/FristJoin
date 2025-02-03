package com.TNTStudios.fristjoin.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.util.prefs.Preferences;

public class FristjoinClient implements ClientModInitializer {
    private static final Preferences PREFS = Preferences.userRoot().node("fristjoin");

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !PREFS.getBoolean("hasSeenRules", false)) {
                client.execute(() -> client.setScreen(new RulesScreen(RulesManager.loadRules())));
                PREFS.putBoolean("hasSeenRules", true);
            }
        });
    }
}