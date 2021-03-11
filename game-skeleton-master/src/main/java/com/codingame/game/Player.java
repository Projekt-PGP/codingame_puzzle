package com.codingame.game;
import com.codingame.gameengine.core.AbstractSoloPlayer;

import java.sql.Time;

// Uncomment the line below and comment the line under it to create a Solo Game
// public class Player extends AbstractSoloPlayer {
public class Player extends AbstractSoloPlayer {
    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player
        return 1;
    }

    public Action getAction() throws TimeoutException
    {
        String output = getOutputs().get(0);
        return new Action(Integer.parseInt(output));
    }
}
