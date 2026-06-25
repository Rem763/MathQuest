package com.quest.graphics;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Generates 32x32 pixel-art sprite sheets for adventurer and slime monster.
 * Run main() to produce PNGs under src/main/resources/sprites/.
 */
public class PixelArtGenerator {

    // Palette
    static final int TR = 0x00000000;
    static final int SK = 0xFFFFD89C;
    static final int HR = 0xFF6B3A2E;
    static final int TU = 0xFF4A90D9;
    static final int PT = 0xFF3D3D3D;
    static final int SW = 0xFFC0C0C0;
    static final int BT = 0xFF5C3A1E;
    static final int EY = 0xFF000000;
    static final int BL = 0xFF8B6914;
    static final int CP = 0xFFCC3333;
    static final int GR = 0xFF5CB85C;
    static final int DG = 0xFF3D8D3D;
    static final int LG = 0xFF8ED88E;
    static final int WH = 0xFFFFFFFF;
    static final int BK = 0xFF000000;
    static final int SKY = 0xFF4A7DB8;
    static final int GRASS = 0xFF4B8E4A;
    static final int TREE = 0xFF2E6B2D;
    static final int DIRT = 0xFF8B5A2B;
    static final int ROCK = 0xFF6B6B6B;

    static final int N = 32;

    public static void main(String[] args) throws Exception {
        new File("src/main/resources/sprites").mkdirs();
        save("src/main/resources/sprites/adventurer_idle.png",   adventurerIdle());
        save("src/main/resources/sprites/adventurer_attack.png", adventurerAttack());
        save("src/main/resources/sprites/adventurer_skill.png",  adventurerSkill());
        save("src/main/resources/sprites/adventurer_death.png",  adventurerDeath());
        save("src/main/resources/sprites/adventurer_hit.png",    adventurerHit());
        save("src/main/resources/sprites/monster_idle.png",      monsterIdle());
        save("src/main/resources/sprites/monster_attack.png",    monsterAttack());
        save("src/main/resources/sprites/monster_death.png",     monsterDeath());
        save("src/main/resources/sprites/monster_hit.png",       monsterHit());
        saveSingle("src/main/resources/sprites/battle_bg.png",    battleBackground());
        System.out.println("Done - 10 sprite sheets in src/main/resources/sprites/");
    }

    static void save(String path, int[][][] sheet) throws Exception {
        int frames = sheet.length;
        BufferedImage img = new BufferedImage(N * frames, N, BufferedImage.TYPE_INT_ARGB);
        for (int f = 0; f < frames; f++) {
            for (int y = 0; y < N; y++) {
                for (int x = 0; x < N; x++) {
                    img.setRGB(f * N + x, y, sheet[f][y][x]);
                }
            }
        }
        ImageIO.write(img, "png", new File(path));
    }

    static void saveSingle(String path, int[][] frame) throws Exception {
        BufferedImage img = new BufferedImage(N, N, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                img.setRGB(x, y, frame[y][x]);
            }
        }
        ImageIO.write(img, "png", new File(path));
    }

    static void rect(int[][] m, int x, int y, int w, int h, int c) {
        for (int dy = 0; dy < h; dy++)
            for (int dx = 0; dx < w; dx++)
                if (x + dx >= 0 && x + dx < N && y + dy >= 0 && y + dy < N)
                    m[y + dy][x + dx] = c;
    }

    static int[][] fresh() {
        int[][] m = new int[N][N];
        for (int y = 0; y < N; y++) for (int x = 0; x < N; x++) m[y][x] = TR;
        return m;
    }

    static int[][][] sheet(int count) {
        int[][][] s = new int[count][][];
        for (int i = 0; i < count; i++) s[i] = fresh();
        return s;
    }

    // ================================================================
    //  BATTLE BACKGROUND
    // ================================================================

    static int[][] battleBackground() {
        int[][] m = fresh();
        // sky
        rect(m, 0, 0, N, 14, SKY);
        // clouds
        rect(m, 3, 2, 6, 2, 0x66FFFFFF);
        rect(m, 15, 3, 7, 2, 0x55FFFFFF);
        rect(m, 22, 1, 5, 2, 0x44FFFFFF);
        // mountains
        rect(m, 0, 10, 11, 6, 0xFF355C4D);
        rect(m, 8, 8, 12, 8, 0xFF2E4D40);
        rect(m, 17, 9, 8, 7, 0xFF264238);
        // ground
        rect(m, 0, 18, N, 14, GRASS);
        rect(m, 0, 24, N, 8, DIRT);
        // path
        rect(m, 11, 18, 10, 14, 0xFF6C4A2D);
        rect(m, 12, 20, 8, 10, 0xFF84542E);
        // bushes and rocks
        rect(m, 2, 19, 3, 2, TREE);
        rect(m, 25, 20, 4, 2, TREE);
        rect(m, 6, 26, 2, 2, ROCK);
        rect(m, 22, 27, 3, 2, ROCK);
        // little sparkles
        rect(m, 4, 6, 1, 1, WH);
        rect(m, 20, 5, 1, 1, WH);
        return m;
    }

    // ================================================================
    //  ADVENTURER
    // ================================================================

    static void drawAdventurer(int[][] m, int tx, int ty, int sword) {
        rect(m, tx + 12, ty + 0, 8, 7, SK);
        rect(m, tx + 13, ty + 1, 2, 2, EY);
        rect(m, tx + 17, ty + 1, 2, 2, EY);
        rect(m, tx + 11, ty - 1, 10, 3, HR);
        rect(m, tx + 10, ty + 0, 2, 3, HR);
        rect(m, tx + 20, ty + 0, 2, 3, HR);
        rect(m, tx + 12, ty + 7, 8, 9, TU);
        rect(m, tx + 13, ty + 14, 2, 2, BL);
        rect(m, tx + 17, ty + 14, 2, 2, BL);
        rect(m, tx + 13, ty + 16, 2, 4, PT);
        rect(m, tx + 17, ty + 16, 2, 4, PT);
        rect(m, tx + 12, ty + 20, 3, 3, BT);
        rect(m, tx + 17, ty + 20, 3, 3, BT);
        rect(m, tx + 8, ty + 9, 4, 2, SK);
        rect(m, tx + 7, ty + 11, 2, 3, SK);
        rect(m, tx + 20, ty + 9, 4, 2, SK);
        rect(m, tx + 23, ty + 11, 2, 3, SK);
        rect(m, tx + 9, ty + 14, 4, 6, CP);
        switch (sword) {
            case 0 -> { rect(m, tx + 24, ty + 7, 2, 8, SW); rect(m, tx + 22, ty + 12, 5, 2, SW); }
            case 1 -> { rect(m, tx + 22, ty + 8, 9, 2, SW); rect(m, tx + 21, ty + 6, 4, 6, SW); }
            case 2 -> { rect(m, tx + 12, ty - 5, 2, 9, SW); rect(m, tx + 9, ty - 5, 6, 2, SW); }
            case 3 -> { rect(m, tx + 23, ty + 6, 9, 2, SW); rect(m, tx + 22, ty + 5, 2, 4, SW); }
            case 4 -> {
                for (int i = 0; i < 9; i++) rect(m, tx + 22 + i, ty + 9 - i, 2, 2, SW);
                rect(m, tx + 21, ty + 10, 3, 2, SW);
            }
        }
    }

    static int[][][] adventurerIdle() { /* unchanged */
        int[][][] s = sheet(4);
        int[] by = {0, -1, 0, -1};
        for (int i = 0; i < 4; i++) {
            drawAdventurer(s[i], 0, by[i], 0);
            if (i == 2) { rect(s[i], 13, 2, 2, 1, SK); rect(s[i], 17, 2, 2, 1, SK); }
        }
        return s;
    }

    static int[][][] adventurerAttack() { int[][][] s = sheet(6); drawAdventurer(s[0], 0, 0, 2); drawAdventurer(s[1], 0, -1, 2); drawAdventurer(s[2], 2, 0, 4); drawAdventurer(s[3], 2, 0, 1); drawAdventurer(s[4], 1, 0, 0); drawAdventurer(s[5], 0, 0, 0); return s; }
    static int[][][] adventurerSkill() { int[][][] s = sheet(6); int[] by = {0, 0, -1, -1, 0, 0}; for (int i = 0; i < 6; i++) { drawAdventurer(s[i], 0, by[i], 2); if (i >= 1) { rect(s[i], 19, 8, 6, 6, 0x44FFFF00); rect(s[i], 18, 7, 8, 8, 0x22FFFF00); } if (i >= 3) { rect(s[i], 14, -2, 5, 5, 0x88FFFF00); rect(s[i], 12, -4, 9, 9, 0x44FFFF00); rect(s[i], 7, 2, 11, 3, SW); } } return s; }
    static int[][][] adventurerDeath() { int[][][] s = sheet(6); drawAdventurer(s[0], 0, 0, 0); drawAdventurer(s[1], 1, 1, 0); drawAdventurer(s[2], 2, 4, 0); rect(s[3], 0, 16, 14, 6, TU); rect(s[3], 11, 13, 6, 6, SK); rect(s[3], 12, 14, 1, 1, EY); rect(s[3], 16, 14, 1, 1, EY); rect(s[3], 10, 12, 8, 2, HR); rect(s[3], 2, 17, 3, 4, PT); rect(s[3], 1, 20, 3, 2, BT); rect(s[3], 8, 14, 4, 3, CP); for (int f = 4; f < 6; f++) { int a = f == 4 ? 0xAA : 0x66; rect(s[f], 0, 16, 14, 6, (a << 24) | (TU & 0xFFFFFF)); rect(s[f], 11, 13, 6, 6, (a << 24) | (SK & 0xFFFFFF)); rect(s[f], 10, 12, 8, 2, (a << 24) | (HR & 0xFFFFFF)); } return s; }
    static int[][][] adventurerHit() { int[][][] s = sheet(3); drawAdventurer(s[0], -3, -1, 0); rect(s[0], 14, 2, 2, 2, EY); rect(s[0], 19, 2, 2, 2, EY); drawAdventurer(s[1], -1, 0, 0); rect(s[1], 14, 2, 1, 1, 0xFFCC3333); drawAdventurer(s[2], 0, 0, 0); return s; }

    // slime section unchanged
    static void drawSlime(int[][] m, int tx, int ty, int squish, boolean eyesClosed) { /* unchanged */
        int topW = 16 - squish * 2;
        int topH = 8 - squish;
        int botW = 18 - squish;
        int botH = 8 - squish;
        int midW = 20 - squish * 2;
        rect(m, tx + 5, ty + botH + topH + 1, botW + 2, 2, DG);
        rect(m, tx + 4, ty + topH + 2, botW, botH, GR);
        rect(m, tx + 3, ty + topH + 3, 2, botH - 1, DG);
        rect(m, tx + 4 + botW - 1, ty + topH + 3, 2, botH - 1, DG);
        rect(m, tx + 2, ty + topH + botH + 1, botW + 4, 2, DG);
        rect(m, tx + 4, ty + topH, midW, 3, LG);
        rect(m, tx + 5, ty + 1, topW, topH, LG);
        rect(m, tx + 5, ty + 0, topW, 2, WH);
        rect(m, tx + 8, ty + 0, 4, 1, WH);
        if (eyesClosed) { rect(m, tx + 9, ty + topH, 3, 1, BK); rect(m, tx + 16, ty + topH, 3, 1, BK); }
        else { rect(m, tx + 8, ty + topH - 1, 4, 4, WH); rect(m, tx + 15, ty + topH - 1, 4, 4, WH); rect(m, tx + 9, ty + topH, 2, 2, BK); rect(m, tx + 16, ty + topH, 2, 2, BK); }
    }
    static int[][][] monsterIdle() { int[][][] s = sheet(4); int[] sq = {0, 2, 0, 1}; for (int i = 0; i < 4; i++) drawSlime(s[i], 5, 10, sq[i], false); return s; }
    static int[][][] monsterAttack() { int[][][] s = sheet(6); int[] sx = {0, 2, 5, 4, 1, 0}; int[] sq = {0, 1, 2, 1, 0, 0}; for (int i = 0; i < 6; i++) { drawSlime(s[i], 5 + sx[i], 10, sq[i], i == 2); if (i == 2) { rect(s[i], 1, 6, 3, 3, DG); rect(s[i], 0, 10, 2, 4, GR); rect(s[i], 28, 6, 3, 3, DG); } } return s; }
    static int[][][] monsterDeath() { int[][][] s = sheet(6); for (int i = 0; i < 6; i++) { int alpha; switch (i) { case 0: alpha = 0xFF; break; case 1: alpha = 0xFF; break; case 2: alpha = 0xDD; break; case 3: alpha = 0xAA; break; case 4: alpha = 0x77; break; default: alpha = 0x44; break; } int spread = i * 2; rect(s[i], 5 - spread, 18 + i, 22 + spread * 2, 5 - i, (alpha << 24) | (GR & 0xFFFFFF)); rect(s[i], 6 - spread / 2, 15 + i, 18 + spread, 4, (alpha << 24) | (LG & 0xFFFFFF)); if (i >= 1) { rect(s[i], 13, 15, 1, 1, EY); rect(s[i], 19, 15, 1, 1, EY); } } return s; }
    static int[][][] monsterHit() { int[][][] s = sheet(3); drawSlime(s[0], 2, 12, 3, true); rect(s[0], 13, 5, 3, 3, EY); rect(s[0], 21, 5, 3, 3, EY); drawSlime(s[1], 5, 9, 1, false); drawSlime(s[2], 5, 10, 0, false); return s; }
}
