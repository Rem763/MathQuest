package com.quest.graphics;

import javafx.scene.image.Image;

/**
 * Pixel art animation data for a single action.
 * Each sprite sheet is a horizontal strip of 32x32 frames.
 */
public class SpriteAnimation {

    public enum State {
        IDLE, ATTACK, SKILL, DEATH, HIT
    }

    private final Image spriteSheet;
    private final int frameCount;
    private final int frameWidth;
    private final int frameHeight;
    private final double frameDuration; // seconds per frame
    private final boolean loop;

    private int currentFrame;
    private double elapsed;
    private boolean finished;
    private boolean playing;

    public SpriteAnimation(Image spriteSheet, int frameCount, double frameDuration, boolean loop) {
        this.spriteSheet = spriteSheet;
        this.frameCount = frameCount;
        this.frameWidth = (int) spriteSheet.getWidth() / frameCount;
        this.frameHeight = (int) spriteSheet.getHeight();
        this.frameDuration = frameDuration;
        this.loop = loop;
        this.currentFrame = 0;
        this.elapsed = 0;
        this.finished = false;
        this.playing = true;
    }

    /** Advance animation by delta seconds. Returns true if frame changed. */
    public boolean update(double deltaSeconds) {
        if (!playing || finished) return false;
        elapsed += deltaSeconds;
        if (elapsed >= frameDuration) {
            elapsed -= frameDuration;
            if (currentFrame < frameCount - 1) {
                currentFrame++;
            } else if (loop) {
                currentFrame = 0;
            } else {
                finished = true;
                playing = false;
            }
            return true;
        }
        return false;
    }

    /** Viewport x offset into the sprite sheet for the current frame. */
    public int getFrameX() {
        return currentFrame * frameWidth;
    }

    public int getFrameY() {
        return 0;
    }

    public int getFrameWidth() { return frameWidth; }
    public int getFrameHeight() { return frameHeight; }

    public Image getSpriteSheet() { return spriteSheet; }

    public void play() {
        playing = true;
        finished = false;
        currentFrame = 0;
        elapsed = 0;
    }

    public void stop() {
        playing = false;
    }

    public boolean isFinished() { return finished; }
    public boolean isPlaying() { return playing; }
    public int getCurrentFrame() { return currentFrame; }
}
