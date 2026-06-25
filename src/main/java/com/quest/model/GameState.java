package com.quest.model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public enum Difficulty {
        NORMAL,
        HARD
    }

    private String playerName = "冒险者";
    private int hp = 30;
    private int maxHp = 30;
    private int enemyHp = 20;
    private int enemyMaxHp = 20;
    private int coins = 100;
    private int level = 1;
    private int stars = 0;
    private int round = 1;
    private Difficulty difficulty = Difficulty.NORMAL;
    private String currentQuestion = "1 + 1 = ?";
    private String[] options = {"1", "2", "3", "4"};
    private int countdown = 10;
    private int correctAnswerIndex = 1;
    private boolean battleFinished;
    private boolean defeated;
    private boolean lastBattleVictory;
    private int lastBattleLevel;
    private int lastBattleRewardCoins;
    private int lastBattleRewardStars;
    private String feedback = "请选择答案";
    private final List<BattleRecord> battleRecords = new ArrayList<>();

    public void resetBattle() {
        enemyHp = enemyMaxHp;
        round = 1;
        countdown = getBattleCountdownSeconds();
        battleFinished = false;
        defeated = false;
        lastBattleVictory = false;
        lastBattleLevel = level;
        lastBattleRewardCoins = 0;
        lastBattleRewardStars = 0;
        feedback = "请选择答案";
    }

    public void resetAfterDefeat() {
        hp = maxHp;
        enemyHp = enemyMaxHp;
        round = 1;
        countdown = getBattleCountdownSeconds();
        battleFinished = false;
        defeated = true;
        lastBattleVictory = false;
        lastBattleRewardCoins = 0;
        lastBattleRewardStars = 0;
        feedback = "已复活，可以重新挑战";
    }

    public int getBattleCountdownSeconds() {
        return difficulty == Difficulty.HARD ? 3 : 10;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty == null ? Difficulty.NORMAL : difficulty;
    }

    public String getDifficultyText() {
        return difficulty == Difficulty.HARD ? "困难" : "普通";
    }

    public void setBattleResult(boolean victory, int level, int rewardCoins, int rewardStars) {
        this.lastBattleVictory = victory;
        this.lastBattleLevel = level;
        this.lastBattleRewardCoins = rewardCoins;
        this.lastBattleRewardStars = rewardStars;
    }

    public void addBattleRecord(BattleRecord record) {
        if (record != null) {
            battleRecords.add(0, record);
            while (battleRecords.size() > 20) {
                battleRecords.remove(battleRecords.size() - 1);
            }
        }
    }

    public List<BattleRecord> getBattleRecords() {
        return battleRecords;
    }

    public boolean isLastBattleVictory() {
        return lastBattleVictory;
    }

    public int getLastBattleLevel() {
        return lastBattleLevel;
    }

    public int getLastBattleRewardCoins() {
        return lastBattleRewardCoins;
    }

    public int getLastBattleRewardStars() {
        return lastBattleRewardStars;
    }

    public int consumeAttackBonus() {
        return 0;
    }

    public boolean hasShield() {
        return false;
    }

    public void consumeShield() {
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
        if (this.hp == 0) {
            defeated = true;
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = Math.max(1, maxHp);
        if (hp > this.maxHp) {
            hp = this.maxHp;
        }
    }

    public int getEnemyHp() {
        return enemyHp;
    }

    public void setEnemyHp(int enemyHp) {
        this.enemyHp = Math.max(0, Math.min(enemyHp, enemyMaxHp));
    }

    public int getEnemyMaxHp() {
        return enemyMaxHp;
    }

    public void setEnemyMaxHp(int enemyMaxHp) {
        this.enemyMaxHp = Math.max(1, enemyMaxHp);
        if (enemyHp > this.enemyMaxHp) {
            enemyHp = this.enemyMaxHp;
        }
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = Math.max(0, coins);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = Math.max(0, Math.min(3, stars));
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = Math.max(1, round);
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(String currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = Math.max(0, countdown);
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public boolean isBattleFinished() {
        return battleFinished;
    }

    public void setBattleFinished(boolean battleFinished) {
        this.battleFinished = battleFinished;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
