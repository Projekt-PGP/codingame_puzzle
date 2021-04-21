package com.codingame.game;

import java.util.HashMap;
import java.util.Map;

import com.codingame.gameengine.module.entities.World;

public class Constants {
    public static final int VIEWER_WIDTH = World.DEFAULT_WIDTH;
    public static final int VIEWER_HEIGHT = World.DEFAULT_HEIGHT;

    public static final int VERTICLE_SIZE = 128;
    public static final int PLAYER_SIZE = 64;
    
    public static final String PLAYER_SPRITE = "player_png.png";
    public static final String VERTICLE_SPRITE[] = {"verticle_png.png", "verticle_png2.png", "verticle_png3.png", "verticle_png4.png"};
    public static final String VERTICLE_ENEMY = "verticle_enemy_png.png";
    public static final String VERTICLE_FUEL = "fuel_png.png";
    public static final String BACKGROUND_SPRITE = "bg_png.png";


    
    public static final String MOVE_ACTION = "MOVE";
}
