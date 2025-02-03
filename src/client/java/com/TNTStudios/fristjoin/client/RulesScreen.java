package com.TNTStudios.fristjoin.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class RulesScreen extends Screen {
    private final List<String> rules;
    private int currentPage = 0;
    // Cantidad de líneas (ya envueltas) que se muestran por página
    private final int maxLinesPerPage = 7;
    private static final File CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("fristjoin/fristjoin_status.json").toFile();

    protected RulesScreen(List<String> rules) {
        super(Text.of("Reglas del Servidor"));
        this.rules = rules;
    }

    @Override
    protected void init() {
        updateButtons();
    }

    /**
     * Actualiza (regenera) los botones de navegación según la página actual.
     */
    private void updateButtons() {
        this.clearChildren();
        int centerX = this.width / 2;
        int buttonY = this.height - 40;

        if (currentPage > 0) {
            this.addDrawableChild(ButtonWidget.builder(Text.of("Anterior"), button -> {
                currentPage--;
                updateButtons();
            }).position(centerX - 100, buttonY).size(80, 20).build());
        }

        if (currentPage < getTotalPages() - 1) {
            this.addDrawableChild(ButtonWidget.builder(Text.of("Siguiente"), button -> {
                currentPage++;
                updateButtons();
            }).position(centerX + 20, buttonY).size(80, 20).build());
        } else {
            this.addDrawableChild(ButtonWidget.builder(Text.of("Aceptar"), button -> {
                markRulesAsRead();
                this.close();
            }).position(centerX + 20, buttonY).size(80, 20).build());
        }
    }

    /**
     * Procesa cada párrafo de reglas para obtener una lista de líneas ya envueltas
     * según el ancho máximo (por ejemplo, el ancho de la pantalla menos márgenes).
     */
    private List<OrderedText> getDisplayLines() {
        List<OrderedText> displayLines = new ArrayList<>();
        // Definimos un margen; por ejemplo, dejamos 20px a cada lado (ancho total - 40)
        int maxWidth = this.width - 40;
        for (String rule : rules) {
            // Envuelve cada párrafo en líneas que no excedan 'maxWidth'
            List<OrderedText> wrapped = this.textRenderer.wrapLines(Text.of(rule), maxWidth);
            displayLines.addAll(wrapped);
        }
        return displayLines;
    }

    /**
     * Calcula la cantidad total de páginas en función de la cantidad de líneas ya envueltas.
     */
    private int getTotalPages() {
        List<OrderedText> displayLines = getDisplayLines();
        return (int) Math.ceil((double) displayLines.size() / maxLinesPerPage);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Dibuja fondo y título
        this.renderBackground(context);
        int startY = this.height / 4;
        int centerX = this.width / 2;

        // Obtiene todas las líneas envueltas
        List<OrderedText> displayLines = getDisplayLines();
        int totalLines = displayLines.size();
        int startLine = currentPage * maxLinesPerPage;
        int endLine = Math.min(startLine + maxLinesPerPage, totalLines);

        int y = startY;
        // Dibuja cada línea centrada horizontalmente
        for (int i = startLine; i < endLine; i++) {
            OrderedText line = displayLines.get(i);
            int lineWidth = this.textRenderer.getWidth(line);
            int x = centerX - (lineWidth / 2);
            context.drawTextWithShadow(this.textRenderer, line, x, y, 0xFFFFFF);
            y += 20;
        }
        super.render(context, mouseX, mouseY, delta);
    }

    /**
     * Guarda en el archivo de configuración que el usuario ya leyó las reglas.
     */
    private void markRulesAsRead() {
        JsonObject json = new JsonObject();
        json.addProperty("fristjoin", true);

        try {
            File parentDir = CONFIG_FILE.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                new Gson().toJson(json, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determina si se deben mostrar las reglas.
     * Si el archivo de configuración existe y contiene el valor "true", ya fueron aceptadas.
     */
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

    /**
     * Permite la navegación mediante las flechas izquierda y derecha.
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // 262 = flecha derecha, 263 = flecha izquierda
        if (keyCode == 262 && currentPage < getTotalPages() - 1) {
            currentPage++;
            updateButtons();
            return true;
        } else if (keyCode == 263 && currentPage > 0) {
            currentPage--;
            updateButtons();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
