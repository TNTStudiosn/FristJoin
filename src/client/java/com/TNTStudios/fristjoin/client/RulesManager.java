package com.TNTStudios.fristjoin.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class RulesManager {
    private static final String RULES_PATH = "/config/rules.json";

    public static List<String> loadRules() {
        try (InputStreamReader reader = new InputStreamReader(RulesManager.class.getResourceAsStream(RULES_PATH))) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return new Gson().fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("Error al cargar las reglas.");
        }
    }
}