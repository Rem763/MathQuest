package com.quest;

import com.quest.scene.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
