package com.clx.doudiz.service;

import com.clx.doudiz.domain.Room;
import com.clx.doudiz.domain.enums.Status;

import java.util.List;


public interface MatchService {
    List<Room> getAllRoom();

    Room findRoomById(int roomId);

    /** 创建房间,并把创建房间者加入到该房间
     * @param playerId 创建房间者的ID
     * @return 创建的RoomId
     */
    int createRoom(int playerId);

    /** 寻找并且尝试进入房间
     * @param roomId 房间ID
     * @param playerId 需要进入的玩家ID
     * @return 房间状态
     */
    Status enterRoomById(int roomId, int playerId);

    /** 离开房间
     * @param roomId 需要离开的房间ID
     * @param playerId 离开的用户id
     * @return 状态
     */
    Status leaveRoom(int roomId, int playerId);




}
