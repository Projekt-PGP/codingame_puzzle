package com.codingame.game;

public class Action {
    public final int destination;

    public Action (int n)
    {
        destination=n;
    }
    @Override
    public String toString()
    {
        return String.format("%d",destination);
    }
}
