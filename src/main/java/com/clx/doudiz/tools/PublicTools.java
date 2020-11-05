package com.clx.doudiz.tools;

public class PublicTools {
    /** 获得下一个玩家的顺位.
     * @param currentPlayer 当前玩家顺位
     * @return 下一个玩家的顺位(0,1,2)
     */
    public static int playerCyclic(int currentPlayer){
        if(3 == ++currentPlayer){//轮流出牌
            currentPlayer = 0;
        }
        return currentPlayer;
    }


}
