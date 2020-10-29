package com.clx.doudiz.domain;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public class Player implements Serializable {
    private int playerId;
    private String playerName;
    private int enterRoomId;

    public int getEnterRoomId() {
        return enterRoomId;
    }

    public void setEnterRoomId(int enterRoomId) {
        this.enterRoomId = enterRoomId;
    }



    public Player(){}

    public Player(String name){
        this.playerName = name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
