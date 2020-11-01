package com.clx.doudiz.tools;

public class PublicTools {
    public static int playerCyclic(int currentPlayer){
        if(3 == ++currentPlayer){//轮流出牌
            currentPlayer = 0;
        }
        return currentPlayer;
    }
}
