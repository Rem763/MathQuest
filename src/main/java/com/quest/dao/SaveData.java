package com.quest.dao;

import com.quest.model.BattleRecord;
import com.quest.model.GameState;

import java.util.ArrayList;
import java.util.List;

public class SaveData {
    public String playerName;
    public int hp;
    public int maxHp;
    public int enemyHp;
    public int enemyMaxHp;
    public int coins;
    public int level;
    public int stars;
    public int round;
    public String difficulty;
    public String currentQuestion;
    public String[] options;
    public int countdown;
    public int correctAnswerIndex;
    public boolean battleFinished;
    public boolean defeated;
    public boolean lastBattleVictory;
    public int lastBattleLevel;
    public int lastBattleRewardCoins;
    public int lastBattleRewardStars;
    public String feedback;
    public List<BattleRecord> battleRecords = new ArrayList<>();

    public static SaveData from(GameState state) {
        SaveData data = new SaveData();
        data.playerName = state.getPlayerName();
        data.hp = state.getHp();
        data.maxHp = state.getMaxHp();
        data.enemyHp = state.getEnemyHp();
        data.enemyMaxHp = state.getEnemyMaxHp();
        data.coins = state.getCoins();
        data.level = state.getLevel();
        data.stars = state.getStars();
        data.round = state.getRound();
        data.difficulty = state.getDifficulty().name();
        data.currentQuestion = state.getCurrentQuestion();
        data.options = state.getOptions();
        data.countdown = state.getCountdown();
        data.correctAnswerIndex = state.getCorrectAnswerIndex();
        data.battleFinished = state.isBattleFinished();
        data.defeated = state.isDefeated();
        data.lastBattleVictory = state.isLastBattleVictory();
        data.lastBattleLevel = state.getLastBattleLevel();
        data.lastBattleRewardCoins = state.getLastBattleRewardCoins();
        data.lastBattleRewardStars = state.getLastBattleRewardStars();
        data.feedback = state.getFeedback();
        data.battleRecords = new ArrayList<>(state.getBattleRecords());
        return data;
    }

    public GameState toGameState() {
        GameState state = new GameState();
        state.setPlayerName(playerName);
        state.setMaxHp(maxHp);
        state.setHp(hp);
        state.setEnemyMaxHp(enemyMaxHp);
        state.setEnemyHp(enemyHp);
        state.setCoins(coins);
        state.setLevel(level);
        state.setStars(stars);
        state.setRound(round);
        state.setDifficulty(GameState.Difficulty.valueOf(difficulty == null ? "NORMAL" : difficulty));
        state.setCurrentQuestion(currentQuestion);
        state.setOptions(options);
        state.setCountdown(countdown);
        state.setCorrectAnswerIndex(correctAnswerIndex);
        state.setBattleFinished(battleFinished);
        state.setDefeated(defeated);
        state.setBattleResult(lastBattleVictory, lastBattleLevel, lastBattleRewardCoins, lastBattleRewardStars);
        state.setFeedback(feedback);
        if (battleRecords != null) {
            for (BattleRecord record : battleRecords) {
                state.addBattleRecord(record);
            }
        }
        return state;
    }
}
