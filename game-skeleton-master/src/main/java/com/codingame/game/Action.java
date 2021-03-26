package com.codingame.game;

public class Action {
    public int destination;
    public Action(int actualRoom,int tagetRoom,Graph g)
    {
        if (checkMove(actualRoom,tagetRoom,g))
        {
            destination = tagetRoom;
        }
        else
            destination=-1;
    }

<<<<<<< Updated upstream
    public boolean checkMove(int actualRoom,int targetRoom, Graph g)
=======
    private boolean checkMove(int actualRoom,int targetRoom, Graph g)
>>>>>>> Stashed changes
    {
        if (g.list[actualRoom-1].contains(targetRoom))
        {
            return true;
        }
        return false;
    }
}