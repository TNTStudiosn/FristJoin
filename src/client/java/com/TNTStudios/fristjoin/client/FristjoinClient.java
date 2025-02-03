package com.TNTStudios.fristjoin.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FristjoinClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null
                    && RulesScreen.shouldShowRules()
                    && !(client.currentScreen instanceof RulesScreen)) {
                client.execute(() -> client.setScreen(new RulesScreen(RulesManager.loadRules())));
            }
        });

    }
}
