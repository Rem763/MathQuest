package com.quest.model;

public class BattleRecord {
    private int level;
    private boolean victory;
    private int rewardCoins;
    private int rewardStars;
    private int roundCount;
    private long timestamp;

    public BattleRecord() {
    }

    public BattleRecord(int level, boolean victory, int rewardCoins, int rewardStars, int roundCount, long timestamp) {
        this.level = level;
        this.victory = victory;
        this.rewardCoins = rewardCoins;
        this.rewardStars = rewardStars;
        this.roundCount = roundCount;
        this.timestamp = timestamp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public int getRewardCoins() {
        return rewardCoins;
    }

    public void setRewardCoins(int rewardCoins) {
        this.rewardCoins = rewardCoins;
    }

    public int getRewardStars() {
        return rewardStars;
    }

    public void setRewardStars(int rewardStars) {
        this.rewardStars = rewardStars;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
