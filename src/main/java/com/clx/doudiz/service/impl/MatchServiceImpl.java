package com.clx.doudiz.service.impl;

import com.clx.doudiz.domain.Room;
import com.clx.doudiz.domain.enums.Status;
import com.clx.doudiz.domain.mapper.PlayerMapper;
import com.clx.doudiz.domain.mapper.RoomMapper;
import com.clx.doudiz.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class MatchServiceImpl implements MatchService {


    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private RoomMapper roomMapper;


    @Override
    public List<Room> getAllRoom() {
        return roomMapper.selectAllRoom();
    }

    @Override
    public Room findRoomById(int roomId) {
        return roomMapper.selectRoomById(roomId);
    }

    @Override
    public int createRoom(int playerId) {
        Random random = new Random();
        int newRoomId;
        do {
            newRoomId = random.nextInt(99998) + 1;
        } while (roomMapper.selectRoomById(newRoomId) != null);//如果该roomId没有重复
        Room newRoom = new Room(newRoomId, playerId);
        roomMapper.insertRoom(newRoom);
        return newRoomId;
    }

    @Override
    public Status enterRoomById(int roomId, int playerId) {
        Room enterRoom = roomMapper.selectRoomById(roomId);
        if (enterRoom.getRoomId() == 0) {//该roomId没有对应的Room,即房间不存在
            return Status.NOT_EXIST;
        } else if (enterRoom.getRoomStatus() != Status.NOT_FULL) {//该房间已经满了,或者已经开始了
            return Status.PLAYING;
        }
        if(enterRoom.getPlayer1Id()==0){ enterRoom.setPlayer1Id(playerId);}
        else if(enterRoom.getPlayer2Id()==0){ enterRoom.setPlayer2Id(playerId);}
        else if(enterRoom.getPlayer3Id()==0){ enterRoom.setPlayer3Id(playerId);}
        else{ return Status.FULL;}
        roomMapper.updateRoom(enterRoom);
        return Status.ENTER_SUCCEED;
    }

    @Override
    public Status leaveRoom(int roomId, int playerId) {
        Room room = roomMapper.selectRoomById(roomId);
        if(room == null){ return Status.NOT_EXIST; }
        if(room.getPlayer1Id()==playerId) room.setPlayer1Id(0);
        else if(room.getPlayer2Id()==playerId) room.setPlayer2Id(0);
        else if(room.getPlayer3Id()==playerId) room.setPlayer3Id(0);
        roomMapper.updateRoom(room);
        return Status.LEAVE_SUCCEED;

    }


}
