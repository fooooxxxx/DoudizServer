package com.clx.doudiz.domain.mapper;


import com.clx.doudiz.domain.Room;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMapper {

    @Select("SELECT * FROM room")
    List<Room> selectAllRoom();

    @Select("SELECT * FROM room WHERE room_id = #{roomId}")
    Room selectRoomById(Integer roomId);

    Room insertRoom(Room newRoom);


}
