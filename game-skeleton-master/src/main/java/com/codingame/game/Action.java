package com.codingame.game;

public class Action {
    private int destination;
    public Action(int actualRoom,int tagetRoom,Graph g)
    {
        if (checkMove(actualRoom,tagetRoom,g))
        {
            destination = tagetRoom;
        }
    }

    public boolean checkMove(int actualRoom,int targetRoom, Graph g)
    {
        if (g.list[actualRoom-1].contains(targetRoom))
        {
            return true;
        }
        return false;
    }
}
