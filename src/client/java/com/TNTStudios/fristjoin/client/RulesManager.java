package com.TNTStudios.fristjoin.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class RulesManager {
    private static final File RULES_FILE = new File("config/fristjoin/rules.json");

    public static List<String> loadRules() {
        if (!RULES_FILE.exists()) {
            createDefaultRules();
        }

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(RULES_FILE))) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return new Gson().fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("Error al cargar las reglas.");
        }
    }

    private static void createDefaultRules() {
        List<String> defaultRules = List.of(
                "Bienvenido al servidor de TNT Studios.",
                "1. Respeta a todos los jugadores.",
                "2. No uses hacks o cheats.",
                "3. No hagas spam en el chat.",
                "4. Prohibido el griefing en zonas protegidas.",
                "5. Usa el sentido común para mantener la convivencia.",
                "6. No se permite lenguaje ofensivo o discriminatorio.",
                "7. La última página requiere aceptar para continuar."
        );

        try {
            // Verifica si el directorio existe, si no, lo crea
            File parentDir = RULES_FILE.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Crea el archivo con las reglas por defecto
            try (FileWriter writer = new FileWriter(RULES_FILE)) {
                new Gson().toJson(defaultRules, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
