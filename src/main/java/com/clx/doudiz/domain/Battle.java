package com.clx.doudiz.domain;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class Battle implements Serializable {
    int battleId;
    JSONArray playerList;
    JSONArray playerHandCards;
    JSONArray playerShowCards;
    int countDown;
    int currentPlayer;
    String extraCards;
    JSONArray identity;
    int battleStatus;


    public int getBattleId() {
        return battleId;
    }

    public void setBattleId(int battleId) {
        this.battleId = battleId;
    }

    public JSONArray getPlayerList() {
        return playerList;
    }

    public void setPlayerList(JSONArray playerList) {
        this.playerList = playerList;
    }

    public JSONArray getPlayerHandCards() {
        return playerHandCards;
    }

    public void setPlayerHandCards(JSONArray playerHandCards) {
        this.playerHandCards = playerHandCards;
    }

    public JSONArray getPlayerShowCards() {
        return playerShowCards;
    }

    public void setPlayerShowCards(JSONArray playerShowCards) {
        this.playerShowCards = playerShowCards;
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getExtraCards() {
        return extraCards;
    }

    public void setExtraCards(String extraCards) {
        this.extraCards = extraCards;
    }

    public JSONArray getIdentity() {
        return identity;
    }

    public void setIdentity(JSONArray identity) {
        this.identity = identity;
    }

    public int getBattleStatus() {
        return battleStatus;
    }

    public void setBattleStatus(int battleStatus) {
        this.battleStatus = battleStatus;
    }

    /** 倒计时自减
     * @return 返回自减后的倒计时值
     */
    public int autoReduceCountDown(){
        return --countDown;
    }
}
