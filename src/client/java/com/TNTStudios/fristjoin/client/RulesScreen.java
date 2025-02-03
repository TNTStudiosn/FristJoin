package com.TNTStudios.fristjoin.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class RulesScreen extends Screen {
    private final List<String> rules;
    private int currentPage = 0;
    private final int maxLinesPerPage = 5;
    private static final File CONFIG_FILE = new File("config/fristjoin/fristjoin_status.json");

    protected RulesScreen(List<String> rules) {
        super(Text.of("Reglas del Servidor"));
        this.rules = rules;
    }

    @Override
    protected void init() {
        updateButtons();
    }

    private void updateButtons() {
        this.clearChildren();
        int centerX = this.width / 2;
        int centerY = this.height - 40;

        if (currentPage > 0) {
            this.addDrawableChild(ButtonWidget.builder(Text.of("Anterior"), btn -> {
                currentPage--;
                updateButtons();
            }).position(centerX - 100, centerY).size(80, 20).build());
        }

        if (currentPage < getTotalPages() - 1) {
            this.addDrawableChild(ButtonWidget.builder(Text.of("Siguiente"), btn -> {
                currentPage++;
                updateButtons();
            }).position(centerX + 20, centerY).size(80, 20).build());
        } else {
            this.addDrawableChild(ButtonWidget.builder(Text.of("Aceptar"), btn -> {
                markRulesAsRead();
                this.close();
            }).position(centerX - 40, centerY).size(80, 20).build());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        int startY = this.height / 4;
        int centerX = this.width / 2;
        int startLine = currentPage * maxLinesPerPage;
        int endLine = Math.min(startLine + maxLinesPerPage, rules.size());

        for (int i = startLine; i < endLine; i++) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.of(rules.get(i)), centerX, startY + (i - startLine) * 20, 0xFFFFFF);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) rules.size() / maxLinesPerPage);
    }

    private void markRulesAsRead() {
        JsonObject json = new JsonObject();
        json.addProperty("fristjoin", true);

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            new Gson().toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean shouldShowRules() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                return !json.get("fristjoin").getAsBoolean();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
