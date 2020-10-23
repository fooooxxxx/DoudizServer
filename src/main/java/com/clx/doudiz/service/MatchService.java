package com.clx.doudiz.service;

import com.clx.doudiz.domain.Player;
import com.clx.doudiz.domain.Room;

import java.util.List;


public interface MatchService {
    List<Room> getAllRoom();

    Room findRoomById(int roomId);

    /** 创建房间,并把创建房间者加入到该房间
     * @param playerId 创建房间者的ID
     * @return 创建的Room
     */
    Room createRoom(int playerId);


}
