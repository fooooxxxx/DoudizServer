package com.clx.doudiz.domain;

import com.clx.doudiz.domain.enums.Status;

public class Room {
    int roomId;
    int player1Id;
    int player2Id;
    int player3Id;
    String notReadyPlayer;
    int battleId;
    Status roomStatus;

    public Room(){
        this.roomId = 0;
        this.roomStatus = Status.UNKNOWN;
    }

    public Room(int roomId,int player1Id){
        this.roomId = roomId;
        this.player1Id = player1Id;
        this.roomStatus = Status.NOT_FULL;
    }

    public Room(int roomId, int player1Id, int player2Id, int player3Id, String notReadyPlayer, int battleId, Status roomStatus) {
        this.roomId = roomId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player3Id = player3Id;
        this.notReadyPlayer = notReadyPlayer;
        this.battleId = battleId;
        this.roomStatus = roomStatus;
    }

    public Room(int roomId, int player1Id, Status roomStatus){
        this.roomId = roomId;
        this.player1Id = player1Id;
        this.roomStatus = roomStatus;
    }

    public Status getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Status roomStatus) {
        this.roomStatus = roomStatus;
    }


    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(int player2Id) {
        this.player2Id = player2Id;
    }

    public int getPlayer3Id() {
        return player3Id;
    }

    public void setPlayer3Id(int player3Id) {
        this.player3Id = player3Id;
    }

    public String getNotReadyPlayer() {
        return notReadyPlayer;
    }

    public void setNotReadyPlayer(String notReadyPlayer) {
        this.notReadyPlayer = notReadyPlayer;
    }

    public int getBattleId() {
        return battleId;
    }

    public void setBattleId(int battleId) {
        this.battleId = battleId;
    }


}
