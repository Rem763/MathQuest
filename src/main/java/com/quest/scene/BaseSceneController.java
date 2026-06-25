package com.quest.scene;

public abstract class BaseSceneController implements SceneAware {
    protected SceneManager sceneManager;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
