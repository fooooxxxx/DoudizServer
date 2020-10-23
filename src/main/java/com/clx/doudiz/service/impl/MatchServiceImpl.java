package com.clx.doudiz.service.impl;

import com.clx.doudiz.domain.Room;
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
    public Room createRoom(int playerId) {
        Random random = new Random();
        int newRoomId;
        do{
            newRoomId = random.nextInt(99998)+1;
        }while(roomMapper.selectRoomById(newRoomId) != null);//如果该roomId没有重复
        Room newRoom = new Room(newRoomId,playerId);
        return roomMapper.insertRoom(newRoom);
    }
}
