package com.quest.scene;

import com.quest.dao.PlayerDao;
import com.quest.model.GameState;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private final SceneFactory sceneFactory;
    private final PlayerDao playerDao = new PlayerDao();
    private GameState gameState = new GameState();

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.sceneFactory = new SceneFactory(this);
    }

    public void start() {
        stage.setTitle("数字闯关");
        switchTo(SceneType.TITLE);
        stage.show();
    }

    public void saveGame() {
        playerDao.save(gameState);
    }

    public void newGame() {
        gameState = new GameState();
    }

    public void switchTo(SceneType type) {
        Scene scene = sceneFactory.create(type);
        stage.setScene(scene);
        stage.sizeToScene();
    }

    public void resetBattleState() {
        gameState.resetBattle();
    }

    public void resetAfterDefeat() {
        gameState.resetAfterDefeat();
    }

    public GameState getGameState() {
        return gameState;
    }
}
