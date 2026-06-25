package com.quest.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneFactory {
    private final SceneManager sceneManager;

    public SceneFactory(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene create(SceneType type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getFxmlPath(type)));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof SceneAware aware) {
                aware.setSceneManager(sceneManager);
            }
            return new Scene(root, 1280, 720);
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalStateException("Failed to load scene: " + type, e);
        }
    }

    private String getFxmlPath(SceneType type) {
        return switch (type) {
            case TITLE -> "/fxml/title.fxml";
            case MAP -> "/fxml/map.fxml";
            case BATTLE -> "/fxml/battle.fxml";
            case RESULT -> "/fxml/result.fxml";
        };
    }
}
