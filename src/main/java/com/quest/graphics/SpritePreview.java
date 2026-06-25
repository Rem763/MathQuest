package com.quest.graphics;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Preview tool - displays all sprite animations at 4x scale.
 * Keys: 1=idle 2=attack 3=skill 4=death 5=hit  A=adventurer M=monster
 */
public class SpritePreview extends Application {

    private SpriteAnimator adventurer;
    private SpriteAnimator monster;
    private SpriteAnimator current;
    private Label status;

    @Override
    public void start(Stage stage) {
        adventurer = SpriteAnimator.load("adventurer");
        monster = SpriteAnimator.load("monster");
        current = adventurer;

        Canvas canvas = new Canvas(512, 256);
        BorderPane root = new BorderPane(canvas);
        root.setStyle("-fx-background-color: #2a2a3e;");
        status = new Label("Adventurer | Press: 1=Idle 2=Attack 3=Skill 4=Death 5=Hit  A/M=Switch");
        status.setTextFill(Color.WHITE);
        root.setBottom(status);

        Scene scene = new Scene(root);
        stage.setTitle("Sprite Preview (4x scale)");
        stage.setScene(scene);
        stage.show();

        // Scale canvas rendering to 4x
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false); // keep pixel-art crisp

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double dt = 1.0 / 60.0; // assume ~60fps
                current.update(dt);
                gc.clearRect(0, 0, 512, 256);
                gc.save();
                gc.scale(4.0, 4.0);
                current.render(gc, 32, 16);
                gc.restore();
            }
        }.start();

        scene.setOnKeyPressed(e -> handleKey(e.getCode()));
    }

    private void handleKey(KeyCode k) {
        switch (k) {
            case DIGIT1 -> play(SpriteAnimator.State.IDLE,   "Idle");
            case DIGIT2 -> play(SpriteAnimator.State.ATTACK, "Attack");
            case DIGIT3 -> play(SpriteAnimator.State.SKILL,  "Skill");
            case DIGIT4 -> play(SpriteAnimator.State.DEATH,  "Death");
            case DIGIT5 -> play(SpriteAnimator.State.HIT,    "Hit");
            case A -> switchChar(adventurer, "Adventurer");
            case M -> switchChar(monster, "Monster");
            default -> {}
        }
    }

    private void play(SpriteAnimator.State state, String name) {
        current.play(state);
        updateStatus(name);
    }

    private void switchChar(SpriteAnimator a, String name) {
        current = a;
        current.play(SpriteAnimator.State.IDLE);
        updateStatus(name + " | 1=Idle 2=Attack 3=Skill 4=Death 5=Hit  A/M=Switch");
    }

    private void updateStatus(String text) {
        status.setText(current == adventurer ? "Adventurer - " + text : "Monster - " + text);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
