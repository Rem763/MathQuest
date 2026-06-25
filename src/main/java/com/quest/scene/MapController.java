package com.quest.scene;

import com.quest.model.GameState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class MapController extends BaseSceneController {
    @FXML private Label statusLabel;
    @FXML private Button stage1Button;
    @FXML private Button stage2Button;
    @FXML private Button stage3Button;
    @FXML private Button bossButton;
    @FXML private Button normalButton;
    @FXML private Button hardButton;

    @FXML
    private void initialize() {
        refreshStatus();
        refreshStageLocks();
    }

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        super.setSceneManager(sceneManager);
        refreshStatus();
        refreshStageLocks();
    }

    private void refreshStatus() {
        if (sceneManager == null || statusLabel == null) {
            return;
        }
        GameState state = sceneManager.getGameState();
        statusLabel.setText("玩家：" + state.getPlayerName() + "  金币：" + state.getCoins() + "  HP：" + state.getHp() + "/" + state.getMaxHp() + "  当前关卡：" + state.getLevel() + "  难度：" + state.getDifficultyText());
    }

    private void refreshStageLocks() {
        if (sceneManager == null) {
            return;
        }
        GameState state = sceneManager.getGameState();
        int level = state.getLevel();
        if (stage1Button != null) stage1Button.setDisable(level < 1);
        if (stage2Button != null) stage2Button.setDisable(level < 2);
        if (stage3Button != null) stage3Button.setDisable(level < 3);
        if (bossButton != null) bossButton.setDisable(level < 4);
        if (normalButton != null) normalButton.setDisable(state.getDifficulty() == GameState.Difficulty.NORMAL);
        if (hardButton != null) hardButton.setDisable(state.getDifficulty() == GameState.Difficulty.HARD);
    }

    @FXML
    public void setNormalDifficulty() {
        if (sceneManager == null) {
            return;
        }
        sceneManager.getGameState().setDifficulty(GameState.Difficulty.NORMAL);
        refreshStatus();
        refreshStageLocks();
    }

    @FXML
    public void setHardDifficulty() {
        if (sceneManager == null) {
            return;
        }
        sceneManager.getGameState().setDifficulty(GameState.Difficulty.HARD);
        refreshStatus();
        refreshStageLocks();
    }

    @FXML public void goStage1() { goToLevel(1); }
    @FXML public void goStage2() { goToLevel(2); }
    @FXML public void goStage3() { goToLevel(3); }
    @FXML public void goBoss() { goToLevel(4); }

    private void goToLevel(int level) {
        if (sceneManager == null) {
            return;
        }
        sceneManager.getGameState().setLevel(level);
        sceneManager.switchTo(SceneType.BATTLE);
    }

    @FXML
    public void backToTitle() {
        if (sceneManager == null) {
            return;
        }
        sceneManager.switchTo(SceneType.TITLE);
    }
}
