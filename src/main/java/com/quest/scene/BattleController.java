package com.quest.scene;

import com.quest.model.BattleRecord;
import com.quest.model.GameState;
import com.quest.model.Question;
import com.quest.service.QuestionGenerator;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class BattleController extends BaseSceneController {
    private final QuestionGenerator questionGenerator = new QuestionGenerator();
    private Timeline countdownTimeline;
    private Timeline idleFloatTimeline;
    private Timeline playerAttackTimeline;
    private Timeline playerHurtTimeline;
    private Timeline monsterAttackTimeline;
    private Timeline monsterHurtTimeline;

    @FXML private StackPane battleRoot;
    @FXML private ImageView backgroundImage;
    @FXML private Label questionLabel;
    @FXML private Label hpLabel;
    @FXML private Label enemyHpLabel;
    @FXML private Label coinLabel;
    @FXML private Label roundLabel;
    @FXML private Label countdownLabel;
    @FXML private Label feedbackLabel;
    @FXML private Rectangle countdownBar;
    @FXML private Region countdownFill;
    @FXML private ImageView playerSprite;
    @FXML private ImageView monsterSprite;
    @FXML private Label playerNameplate;
    @FXML private Label monsterNameplate;
    @FXML private Button option1Button;
    @FXML private Button option2Button;
    @FXML private Button option3Button;
    @FXML private Button option4Button;
    
    @FXML
    private void initialize() {
        loadSprites();
        prepareBattle();
    }

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        super.setSceneManager(sceneManager);
        loadSprites();
        prepareBattle();
    }

    private void loadSprites() {
        if (backgroundImage != null) {
            backgroundImage.setImage(loadImage("/sprites/battle_bg.png"));
        }
        setPlayerState("idle");
        setMonsterState("idle");
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }


    private void setPlayerState(String state) {
        if (playerSprite == null) {
            return;
        }
        playerSprite.setImage(loadImage("/sprites/player/" + state + ".png"));
        playerSprite.setPreserveRatio(true);
        playerSprite.setFitWidth(200);
        playerSprite.setFitHeight(200);
        playerSprite.setTranslateY(6);
    }

    private void setMonsterState(String state) {
        if (monsterSprite == null) {
            return;
        }
        monsterSprite.setImage(loadImage("/sprites/slime/" + state + ".png"));
        monsterSprite.setPreserveRatio(true);
        monsterSprite.setFitWidth(200);
        monsterSprite.setFitHeight(200);
        monsterSprite.setTranslateY(12);
    }

    private void prepareBattle() {
        if (sceneManager == null) {
            return;
        }
        sceneManager.resetBattleState();
        generateNextQuestion();
        startCountdown();
        startIdleFloatAnimation();
    }

    private void generateNextQuestion() {
        if (sceneManager == null) {
            return;
        }
        GameState state = sceneManager.getGameState();
        Question question = questionGenerator.generate(state.getLevel());
        state.setCurrentQuestion(question.getText());
        state.setOptions(question.getOptions().stream().map(String::valueOf).toArray(String[]::new));
        state.setCorrectAnswerIndex(question.getOptions().indexOf(question.getAnswer()));
        state.setCountdown(state.getBattleCountdownSeconds());
        state.setBattleFinished(false);
        resetAnswerButtons();
        refreshView();
    }

    private void resetAnswerButtons() {
        if (option1Button != null) option1Button.setDisable(false);
        if (option2Button != null) option2Button.setDisable(false);
        if (option3Button != null) option3Button.setDisable(false);
        if (option4Button != null) option4Button.setDisable(false);
    }

    private void startCountdown() {
        stopCountdown();
        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (sceneManager == null) {
                stopCountdown();
                return;
            }
            GameState state = sceneManager.getGameState();
            if (state.isBattleFinished()) {
                stopCountdown();
                return;
            }
            int next = state.getCountdown() - 1;
            state.setCountdown(next);
            if (next <= 0) {
                resolveTurn(false, "超时，判定错误", true);
            }
            refreshView();
        }));
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        countdownTimeline.play();
    }

    private void stopCountdown() {
        if (countdownTimeline != null) {
            countdownTimeline.stop();
            countdownTimeline = null;
        }
    }

    private void startIdleFloatAnimation() {
        if (idleFloatTimeline != null) {
            idleFloatTimeline.stop();
        }
        if (playerSprite != null) {
            playerSprite.setTranslateY(6);
        }
        if (monsterSprite != null) {
            monsterSprite.setTranslateY(12);
        }
        idleFloatTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    if (playerSprite != null) {
                        playerSprite.setTranslateY(6);
                    }
                    if (monsterSprite != null) {
                        monsterSprite.setTranslateY(12);
                    }
                }),
                new KeyFrame(Duration.seconds(1.5), event -> {
                    if (playerSprite != null) {
                        playerSprite.setTranslateY(0);
                    }
                    if (monsterSprite != null) {
                        monsterSprite.setTranslateY(6);
                    }
                })
        );
        idleFloatTimeline.setAutoReverse(true);
        idleFloatTimeline.setCycleCount(Timeline.INDEFINITE);
        idleFloatTimeline.play();
    }

    private void stopIdleFloatAnimation() {
        if (idleFloatTimeline != null) {
            idleFloatTimeline.stop();
            idleFloatTimeline = null;
        }
    }

    private void stopFrameTimelines() {
        if (playerAttackTimeline != null) {
            playerAttackTimeline.stop();
            playerAttackTimeline = null;
        }
        if (playerHurtTimeline != null) {
            playerHurtTimeline.stop();
            playerHurtTimeline = null;
        }
        if (monsterAttackTimeline != null) {
            monsterAttackTimeline.stop();
            monsterAttackTimeline = null;
        }
        if (monsterHurtTimeline != null) {
            monsterHurtTimeline.stop();
            monsterHurtTimeline = null;
        }
    }

    private void playFrameAnimation(ImageView sprite, String folder, int frameCount, double frameSeconds, Runnable onFinished) {
        if (sprite == null) {
            return;
        }
        Timeline timeline = new Timeline();
        for (int i = 0; i < frameCount; i++) {
            final int index = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(frameSeconds * i), event -> sprite.setImage(loadImage("/sprites/" + folder + "/" + index + ".png"))));
        }
        if (onFinished != null) {
            timeline.setOnFinished(e -> onFinished.run());
        }
        timeline.play();
        if (sprite == playerSprite) {
            if (folder.contains("player/attack")) {
                playerAttackTimeline = timeline;
            } else if (folder.contains("player/hurt")) {
                playerHurtTimeline = timeline;
            }
        } else if (sprite == monsterSprite) {
            if (folder.contains("slime/attack")) {
                monsterAttackTimeline = timeline;
            } else if (folder.contains("slime/hurt")) {
                monsterHurtTimeline = timeline;
            }
        }
    }

    private void refreshView() {
        if (sceneManager == null) {
            return;
        }
        GameState state = sceneManager.getGameState();
        if (playerNameplate != null) playerNameplate.setText(state.getPlayerName());
        if (monsterNameplate != null) monsterNameplate.setText("史莱姆");
        if (questionLabel != null) questionLabel.setText("第 " + state.getRound() + " 题：" + state.getCurrentQuestion());
        if (hpLabel != null) hpLabel.setText("玩家 HP: " + state.getHp() + "/" + state.getMaxHp());
        if (enemyHpLabel != null) enemyHpLabel.setText("怪物 HP: " + state.getEnemyHp() + "/" + state.getEnemyMaxHp());
        if (coinLabel != null) coinLabel.setText("金币: " + state.getCoins());
        if (roundLabel != null) roundLabel.setText("回合: " + state.getRound());
        if (countdownLabel != null) {
            countdownLabel.setText("倒计时: " + state.getCountdown() + "s（" + state.getDifficultyText() + "）");
            countdownLabel.setTextFill(state.getCountdown() <= 3 ? Color.RED : Color.web("#333333"));
            countdownLabel.setStyle(state.getCountdown() <= 3 ? "-fx-font-size: 20px; -fx-font-weight: bold;" : "-fx-font-size: 20px;");
        }
        if (feedbackLabel != null) feedbackLabel.setText(state.getFeedback());
        if (option1Button != null) option1Button.setText(state.getOptions()[0]);
        if (option2Button != null) option2Button.setText(state.getOptions()[1]);
        if (option3Button != null) option3Button.setText(state.getOptions()[2]);
        if (option4Button != null) option4Button.setText(state.getOptions()[3]);
        updateCountdownBar(state);
    }

    private void updateCountdownBar(GameState state) {
        if (countdownFill == null || countdownBar == null) {
            return;
        }
        double maxSeconds = state.getBattleCountdownSeconds();
        double ratio = Math.max(0.0, Math.min(1.0, state.getCountdown() / maxSeconds));
        double width = countdownBar.getWidth() > 0 ? countdownBar.getWidth() : 520;
        countdownFill.setPrefWidth(width * ratio);
        countdownFill.setStyle(ratio <= 0.3
                ? "-fx-background-color: linear-gradient(to right, #ff4d4d, #ff9966); -fx-background-radius: 12;"
                : "-fx-background-color: linear-gradient(to right, #4caf50, #9be15d); -fx-background-radius: 12;");
    }

    @FXML public void chooseOption1() { answerSelected(0); }
    @FXML public void chooseOption2() { answerSelected(1); }
    @FXML public void chooseOption3() { answerSelected(2); }
    @FXML public void chooseOption4() { answerSelected(3); }

    private void answerSelected(int index) {
        if (sceneManager == null) {
            return;
        }
        GameState state = sceneManager.getGameState();
        if (state.isBattleFinished()) {
            return;
        }
        boolean correct = index == state.getCorrectAnswerIndex();
        if (correct) {
            playPlayerAttackAnimation();
        } else {
            playMonsterAttackAnimation();
        }
        resolveTurn(correct, correct ? "回答正确！怪物受到伤害" : "回答错误！玩家受到伤害", false);
    }

    private void resolveTurn(boolean playerHit, String feedback, boolean timeout) {
        if (sceneManager == null) {
            return;
        }
        GameState state = sceneManager.getGameState();
        state.setBattleFinished(true);
        stopCountdown();
        state.setFeedback(feedback);

        if (playerHit) {
            int damage = 8;
            state.setEnemyHp(state.getEnemyHp() - damage);
            state.setCoins(state.getCoins() + 10);
        } else {
            int damage = 5;
            state.setHp(state.getHp() - damage);
        }

        refreshView();

        if (state.getHp() <= 0) {
            state.setStars(0);
            state.setBattleResult(false, state.getLevel(), 0, 0);
            state.addBattleRecord(new BattleRecord(state.getLevel(), false, 0, 0, state.getRound(), System.currentTimeMillis()));
            state.setFeedback(timeout ? "时间到！挑战失败" : "闯关失败");
            playTimeoutFailAnimation();
            sceneManager.resetAfterDefeat();
            PauseTransition delay = new PauseTransition(Duration.millis(1400));
            delay.setOnFinished(e -> finishBattle());
            delay.play();
            return;
        }

        if (state.getEnemyHp() <= 0) {
            int rewardCoins = 20;
            int rewardStars = Math.min(3, state.getStars() + 1);
            state.setStars(rewardStars);
            state.setCoins(state.getCoins() + rewardCoins);
            state.setBattleResult(true, state.getLevel(), rewardCoins, rewardStars);
            state.addBattleRecord(new BattleRecord(state.getLevel(), true, rewardCoins, rewardStars, state.getRound(), System.currentTimeMillis()));
            playVictoryAnimation();
            PauseTransition delay = new PauseTransition(Duration.millis(900));
            delay.setOnFinished(e -> finishBattle());
            delay.play();
            return;
        }

        state.setRound(state.getRound() + 1);
        generateNextQuestion();
        startCountdown();
    }

    private void playPlayerAttackAnimation() {
        stopFrameTimelines();
        if (feedbackLabel != null) {
            feedbackLabel.setScaleX(1.0);
            feedbackLabel.setScaleY(1.0);
            ScaleTransition scale = new ScaleTransition(Duration.millis(120), feedbackLabel);
            scale.setFromX(1.0);
            scale.setFromY(1.0);
            scale.setToX(1.15);
            scale.setToY(1.15);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);
            scale.play();
        }
        if (playerSprite != null) {
            setPlayerState("idle");
            playFrameAnimation(playerSprite, "player/attack", 4, 0.08, () -> setPlayerState("idle"));
            PauseTransition delay = new PauseTransition(Duration.millis(220));
            delay.setOnFinished(e -> {
                if (monsterSprite != null) {
                    setMonsterState("hurt");
                    playFrameAnimation(monsterSprite, "slime/hurt", 2, 0.08, () -> setMonsterState("idle"));
                }
            });
            delay.play();
        }
    }

    private void playMonsterAttackAnimation() {
        stopFrameTimelines();
        if (feedbackLabel != null) {
            feedbackLabel.setScaleX(1.0);
            feedbackLabel.setScaleY(1.0);
            ScaleTransition scale = new ScaleTransition(Duration.millis(120), feedbackLabel);
            scale.setFromX(1.0);
            scale.setFromY(1.0);
            scale.setToX(1.15);
            scale.setToY(1.15);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);
            scale.play();
        }
        if (monsterSprite != null) {
            playFrameAnimation(monsterSprite, "slime/attack", 8, 0.06, () -> setMonsterState("idle"));
            PauseTransition delay = new PauseTransition(Duration.millis(220));
            delay.setOnFinished(e -> {
                if (playerSprite != null) {
                    setPlayerState("hurt");
                    playFrameAnimation(playerSprite, "player/hurt", 3, 0.08, () -> setPlayerState("idle"));
                }
            });
            delay.play();
        }
    }

    private void playVictoryAnimation() {
        if (feedbackLabel != null) {
            feedbackLabel.setText("胜利！");
            ScaleTransition scale = new ScaleTransition(Duration.millis(220), feedbackLabel);
            scale.setFromX(1.0);
            scale.setFromY(1.0);
            scale.setToX(1.4);
            scale.setToY(1.4);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);
            scale.play();

            RotateTransition rotate = new RotateTransition(Duration.millis(320), feedbackLabel);
            rotate.setByAngle(360);
            rotate.play();
        }
        if (playerSprite != null) {
            playFrameAnimation(playerSprite, "player/attack", 4, 0.08, () -> setPlayerState("idle"));
        }
        if (monsterSprite != null) {
            setMonsterState("dead");
        }
    }

    private void playTimeoutFailAnimation() {
        if (feedbackLabel != null) {
            feedbackLabel.setText("时间到！闯关失败！");
            ScaleTransition scale = new ScaleTransition(Duration.millis(180), feedbackLabel);
            scale.setFromX(1.0);
            scale.setFromY(1.0);
            scale.setToX(1.5);
            scale.setToY(1.5);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);
            scale.play();

            FadeTransition fade = new FadeTransition(Duration.millis(360), feedbackLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.3);
            fade.setAutoReverse(true);
            fade.setCycleCount(2);
            fade.play();
        }
        if (countdownLabel != null) {
            countdownLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #d32f2f;");
        }
        if (playerSprite != null) {
            playFrameAnimation(playerSprite, "player/dead", 9, 0.10, () -> setPlayerState("dead"));
        }
        if (monsterSprite != null) {
            playFrameAnimation(monsterSprite, "slime/attack", 8, 0.06, () -> setMonsterState("idle"));
        }
    }

    private void finishBattle() {
        stopCountdown();
        stopIdleFloatAnimation();
        stopFrameTimelines();
        if (sceneManager == null) {
            return;
        }
        sceneManager.switchTo(SceneType.RESULT);
    }

    @FXML
    public void flee() {
        stopCountdown();
        if (sceneManager == null) {
            return;
        }
        sceneManager.switchTo(SceneType.MAP);
    }
}
