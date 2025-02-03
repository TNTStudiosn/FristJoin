package com.TNTStudios.fristjoin.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class RulesManager {
    // Se usa el directorio de config de Fabric
    private static final File CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("fristjoin").toFile();
    private static final File RULES_FILE = new File(CONFIG_DIR, "rules.json");

    public static List<String> loadRules() {
        if (!RULES_FILE.exists()) {
            createDefaultRules();
        }

        try (Reader reader = new FileReader(RULES_FILE)) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return new Gson().fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("Error al cargar las reglas.");
        }
    }

    private static void createDefaultRules() {
        List<String> defaultRules = List.of(
                "Bienvenido al servidor de TNTStudios.",
                "1. Respeta a todos los jugadores.",
                "2. No uses hacks o cheats.",
                "3. No hagas spam en el chat.",
                "4. Prohibido el griefing en zonas protegidas.",
                "5. Usa el sentido común para mantener la convivencia.",
                "6. No está permitido el lenguaje ofensivo o discriminatorio.",
                "7. Evita el abuso de bugs o glitches; repórtalos al staff.",
                "8. No está permitido el comercio de cuentas o ítems por dinero real.",
                "9. Mantén el chat limpio: no uses mayúsculas excesivas.",
                "10. Respetar las decisiones del staff; su palabra es final.",
                "11. No se permite la suplantación de identidad de otros jugadores o staff.",
                "12. Las construcciones ofensivas o inapropiadas serán eliminadas.",
                "13. No hagas publicidad de otros servidores sin permiso.",
                "14. La última página requiere aceptar para continuar."
        );

        try {
            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
            }
            try (Writer writer = new FileWriter(RULES_FILE)) {
                new Gson().toJson(defaultRules, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
