package com.codingame.game;

import org.javatuples.Pair;

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

    private boolean checkMove(int actualRoom,int targetRoom, Graph g)
    {
        for (Pair<Integer,Integer> p:g.list[actualRoom]) {
            if (p.getValue0()==targetRoom)
            {
                return true;
            }
        }
        return false;
    }
}