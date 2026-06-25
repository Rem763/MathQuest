package com.quest.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.model.GameState;

import java.io.File;
import java.io.IOException;

public class PlayerDao {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File saveFile = new File(System.getProperty("user.home"), ".quest-save.json");

    public void save(GameState state) {
        try {
            SaveData data = SaveData.from(state);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, data);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save game", e);
        }
    }

    public GameState load() {
        if (!saveFile.exists()) {
            return null;
        }
        try {
            SaveData data = objectMapper.readValue(saveFile, SaveData.class);
            return data.toGameState();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load game", e);
        }
    }

    public boolean hasSave() {
        return saveFile.exists();
    }

    public void deleteSave() {
        if (saveFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            saveFile.delete();
        }
    }
}
