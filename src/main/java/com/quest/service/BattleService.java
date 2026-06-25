package com.quest.service;

import com.quest.model.Player;

public class BattleService {
    public int calculateDamage(Player player) {
        return Math.max(1, player.getAttack());
    }
}
