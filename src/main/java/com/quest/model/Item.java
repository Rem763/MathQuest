package com.quest.model;

public class Item {
    private String name;
    private String type;
    private int effect;
    private int price;
    private int count;

    public Item() {
    }

    public Item(String name, String type, int effect, int price) {
        this.name = name;
        this.type = type;
        this.effect = effect;
        this.price = price;
        this.count = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = Math.max(0, count);
    }

    public void addOne() {
        count++;
    }

    public boolean consumeOne() {
        if (count <= 0) {
            return false;
        }
        count--;
        return true;
    }
}
