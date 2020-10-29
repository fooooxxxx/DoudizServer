package com.clx.doudiz.service;

import com.clx.doudiz.domain.Room;
import com.clx.doudiz.domain.enums.Status;

import java.util.List;


public interface RoomService {
    List<Room> getAllRoom();


    /** 创建房间,并把创建房间者加入到该房间
     * @param playerId 创建房间者的id
     * @return 创建的roomId
     */
    int createRoom(int playerId);

    /** 寻找并且尝试进入房间
     * @param roomId 房间id
     * @param playerId 需要进入的玩家ID
     * @return 房间状态
     */
    Status enterRoomById(int roomId, int playerId);

    /** 通过playerId查询到他所在房间,并且从该房间中删去自己
     * @param playerId 离开的用户id
     * @return 只会返回 LEAVE_SUCCEED
     */
    Status leaveRoom(int playerId);

    /** 检查Room的status,并且对room进行删除或开始游戏等操作
     * @param roomId 需要检查的roomId
     * @return 检查后的Room状态
     */
    Status checkRoomStatus(int roomId);


    /** 判断该房间当前状况,并且更新状况
     * @param room 需要判断的Room
     * @return 当前房间状态
     */
    Status judgeRoomStatus(Room room);


}
