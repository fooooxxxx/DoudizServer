package com.clx.doudiz.domain.mapper;


import com.clx.doudiz.domain.Room;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMapper {

    @Select("SELECT * FROM room")
    List<Room> selectAllRoom();

    @Select("SELECT * FROM room WHERE room_id = #{roomId}")
    Room selectRoomById(int roomId);

    @Delete("DELETE FROM room WHERE room_id = #{roomId}")
    boolean deleteRoom(Room room);

    @Delete("DELETE FROM room WHERE room_id = #{roomId}")
    boolean deleteRoomById(int roomId);

    boolean insertRoom(Room newRoom);

    boolean updateRoom(Room room);




}
