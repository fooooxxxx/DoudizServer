package com.clx.doudiz.service.impl;

import com.clx.doudiz.domain.Player;
import com.clx.doudiz.domain.Room;
import com.clx.doudiz.domain.enums.Status;
import com.clx.doudiz.domain.mapper.PlayerMapper;
import com.clx.doudiz.domain.mapper.RoomMapper;
import com.clx.doudiz.service.BattleService;
import com.clx.doudiz.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RoomServiceImpl implements RoomService {


    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private BattleService battleService;


    @Override
    public List<Room> getAllRoom() {
        return roomMapper.selectAllRoom();
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
        //设置player的所在房间
        Player player = playerMapper.findPlayerById(playerId);
        player.setEnterRoomId(newRoomId);
        playerMapper.updatePlayer(player);
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
        //设置player的所在房间
        Player player = playerMapper.findPlayerById(playerId);
        player.setEnterRoomId(enterRoom.getRoomId());
        playerMapper.updatePlayer(player);
        return Status.ENTER_SUCCEED;
    }

    @Override
    public Status leaveRoom(int playerId) {
        Player player = playerMapper.findPlayerById(playerId);
        if(player!=null && player.getEnterRoomId()!=0) {//该用户之前进入过房间,并且没有退出
            //Room的处理
            Room room = roomMapper.selectRoomById(player.getEnterRoomId());
            if (room != null) {//如果所离开的房间已经消失
                if (room.getPlayer1Id() == playerId) room.setPlayer1Id(0);
                else if (room.getPlayer2Id() == playerId) room.setPlayer2Id(0);
                else if (room.getPlayer3Id() == playerId) room.setPlayer3Id(0);
                roomMapper.updateRoom(room);
                checkRoomStatus(room.getRoomId());//离开房间时.检查房间状态
            }
            //Player的处理
            player.setEnterRoomId(0);
            playerMapper.updatePlayer(player);
        }
        return Status.LEAVE_SUCCEED;
    }

    @Override
    public Status checkRoomStatus(int roomId) {
        Room room = roomMapper.selectRoomById(roomId);
        if(room==null) return Status.NOT_EXIST;//房间判空

        Status resStatus = judgeRoomStatus(room);//先判断room的状态
        switch(room.getRoomStatus()){
            case EMPTY://房间为空,进行清理
                roomMapper.deleteRoomById(roomId);
                resStatus = Status.NOT_EXIST;//清理后不在存在
                return resStatus;//提前return
            case FULL_AND_READY://房间已满,开始游戏
                battleService.createBattle(room);//初始化战局
                /*
                *
                * 启动游戏
                *
                * */
                resStatus = Status.PLAYING;
                //模式设置为PLAYING
                break;
            case PLAYING:
                return Status.PLAYING;
            default:
                battleService.createBattle(room);

        }
        room.setRoomStatus(resStatus);
        roomMapper.updateRoom(room);

        return resStatus;
    }

    @Override
    public Status judgeRoomStatus(Room room) {
        int p1 = room.getPlayer1Id(),p2 = room.getPlayer2Id(),p3 = room.getPlayer3Id();
        if(p1 == p2 && p2 == p3){//全部相等,意味着全部为0
            //如果房间里没有玩家
            room.setRoomStatus(Status.EMPTY);
        } else if(p1==0 || p2 == 0 || p3 == 0){
            //如果房间里有空位
            room.setRoomStatus(Status.NOT_FULL);
        } else if(room.getRoomStatus()!=Status.PLAYING){
            //满员而且准备开始状态
            room.setRoomStatus(Status.FULL_AND_READY);
        }
        roomMapper.updateRoom(room);
        return room.getRoomStatus();
    }
}
