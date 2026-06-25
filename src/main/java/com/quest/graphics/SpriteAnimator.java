package com.quest.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * Manages sprite animations for a character.
 * Loads sprite sheets from resources/sprites/ and handles state-driven playback.
 */
public class SpriteAnimator {

    public enum State {
        IDLE, ATTACK, SKILL, DEATH, HIT
    }

    private final Map<State, SpriteAnimation> animations = new EnumMap<>(State.class);
    private State currentState = State.IDLE;
    private boolean flipX;
    private double x, y;

    /** Load all animations for a character from the given sprite folder prefix. */
    public static SpriteAnimator load(String prefix) {
        SpriteAnimator anim = new SpriteAnimator();
        anim.loadAnim(State.IDLE,   prefix + "_idle.png",   4, 0.15, true);
        anim.loadAnim(State.ATTACK, prefix + "_attack.png", 6, 0.08, false);
        if (prefix.contains("adventurer")) {
            anim.loadAnim(State.SKILL,  prefix + "_skill.png",  6, 0.10, false);
        }
        anim.loadAnim(State.DEATH,  prefix + "_death.png",  6, 0.12, false);
        anim.loadAnim(State.HIT,    prefix + "_hit.png",    3, 0.08, false);
        return anim;
    }

    private void loadAnim(State state, String filename, int frameCount,
                          double frameDuration, boolean loop) {
        String path = "/sprites/" + filename;
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.err.println("Sprite not found: " + path);
            return;
        }
        Image sheet = new Image(is);
        animations.put(state, new SpriteAnimation(sheet, frameCount, frameDuration, loop));
    }

    /** Switch to a new animation state. Restarts if different from current. */
    public void play(State state) {
        if (state == currentState) return;
        currentState = state;
        SpriteAnimation anim = animations.get(state);
        if (anim != null) anim.play();
    }

    public State getState() { return currentState; }

    /** Advance animation by delta seconds. Auto-returns to IDLE if one-shot ends. */
    public void update(double deltaSeconds) {
        SpriteAnimation anim = animations.get(currentState);
        if (anim == null) return;
        anim.update(deltaSeconds);
        if (anim.isFinished() && currentState != State.IDLE && currentState != State.DEATH) {
            play(State.IDLE);
        }
    }

    /** Render current frame at stored position. */
    public void render(GraphicsContext gc) {
        SpriteAnimation anim = animations.get(currentState);
        if (anim == null) return;
        Image sheet = anim.getSpriteSheet();
        double fx = flipX ? -1 : 1;
        double rx = flipX ? x + anim.getFrameWidth() : x;
        gc.save();
        if (flipX) {
            gc.translate(rx + anim.getFrameWidth(), y);
            gc.scale(-1, 1);
            gc.drawImage(sheet, anim.getFrameX(), anim.getFrameY(),
                    anim.getFrameWidth(), anim.getFrameHeight(),
                    0, 0, anim.getFrameWidth(), anim.getFrameHeight());
        } else {
            gc.drawImage(sheet, anim.getFrameX(), anim.getFrameY(),
                    anim.getFrameWidth(), anim.getFrameHeight(),
                    x, y, anim.getFrameWidth(), anim.getFrameHeight());
        }
        gc.restore();
    }

    /** Render current frame at specified position. */
    public void render(GraphicsContext gc, double rx, double ry) {
        setPosition(rx, ry);
        render(gc);
    }

    public void setPosition(double x, double y) { this.x = x; this.y = y; }
    public void setFlipX(boolean flip) { this.flipX = flip; }
    public boolean isFinished() {
        SpriteAnimation anim = animations.get(currentState);
        return anim != null && anim.isFinished();
    }
}
