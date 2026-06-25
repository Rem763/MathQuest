package com.quest.scene;

import com.quest.model.BattleRecord;
import com.quest.model.GameState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ResultController extends BaseSceneController {
    @FXML
    private Label resultLabel;
    @FXML
    private TextFlow recordFlow;

    private static final DateTimeFormatter RECORD_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    @FXML
    private void initialize() {
        refreshView();
    }

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        super.setSceneManager(sceneManager);
        refreshView();
    }

    private void refreshView() {
        if (sceneManager == null || resultLabel == null) {
            return;
        }
        GameState state = sceneManager.getGameState();
        if (state.isDefeated()) {
            resultLabel.setText("失败结算：第 " + state.getLastBattleLevel() + " 关挑战失败\n本次奖励：0 金币，0 星");
        } else if (state.isLastBattleVictory()) {
            resultLabel.setText("胜利结算：第 " + state.getLastBattleLevel() + " 关通关成功\n获得金币：" + state.getLastBattleRewardCoins() + "，星级：" + state.getLastBattleRewardStars());
        } else {
            resultLabel.setText("结算：第 " + state.getLevel() + " 关，获得 " + state.getStars() + " 星");
        }
        refreshRecords(state);
    }

    private void refreshRecords(GameState state) {
        if (recordFlow == null) {
            return;
        }
        recordFlow.getChildren().clear();
        if (state.getBattleRecords().isEmpty()) {
            recordFlow.getChildren().add(new Text("暂无闯关记录"));
            return;
        }
        int limit = Math.min(5, state.getBattleRecords().size());
        for (int i = 0; i < limit; i++) {
            BattleRecord record = state.getBattleRecords().get(i);
            String text = String.format("第 %d 关 | %s | 回合 %d | 金币 %+d | 星级 %+d | %s\n",
                    record.getLevel(),
                    record.isVictory() ? "胜利" : "失败",
                    record.getRoundCount(),
                    record.getRewardCoins(),
                    record.getRewardStars(),
                    RECORD_TIME.format(Instant.ofEpochMilli(record.getTimestamp())));
            recordFlow.getChildren().add(new Text(text));
        }
    }

    @FXML
    public void nextLevel() {
        if (sceneManager == null) {
            return;
        }
        GameState state = sceneManager.getGameState();
        state.setDefeated(false);
        state.setLevel(state.getLevel() + 1);
        sceneManager.switchTo(SceneType.BATTLE);
    }

    @FXML
    public void revive() {
        if (sceneManager == null) {
            return;
        }
        GameState state = sceneManager.getGameState();
        state.resetAfterDefeat();
        sceneManager.switchTo(SceneType.BATTLE);
    }

    @FXML
    public void backToMap() {
        if (sceneManager == null) {
            return;
        }
        sceneManager.getGameState().setDefeated(false);
        sceneManager.switchTo(SceneType.MAP);
    }
}
