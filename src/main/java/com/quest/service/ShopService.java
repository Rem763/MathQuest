package com.quest.service;

public class ShopService {
    public boolean canBuy(int coins, int price) {
        return coins >= price;
    }
}
