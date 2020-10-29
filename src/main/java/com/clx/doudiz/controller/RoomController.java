package com.clx.doudiz.controller;

import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.domain.Room;
import com.clx.doudiz.domain.enums.Status;
import com.clx.doudiz.service.RoomService;
import com.clx.doudiz.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomController {

    @Autowired
    private RoomService matchService;

    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/room/find", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findRoomById(@RequestBody JSONObject jsonData) {
        int roomId = jsonData.getInteger("roomId");
        int playerId = jsonData.getInteger("playerId");
        matchService.leaveRoom(playerId);//进入新的房间前,先离开老房间
        JSONObject resJson = new JSONObject();
        Status roomStatus = matchService.enterRoomById(roomId, playerId);
        resJson.put("statusCode", roomStatus.getCode());//将状态码写入json
        if (roomStatus == Status.ENTER_SUCCEED) {//进入成功
            resJson.put("status", true);
        } else {
            resJson.put("status", false);
        }
        return resJson.toJSONString();
    }

    /** 创建房间
     * @param jsonData 包含playerId
     * @return 是否成功,如果成功返回roomId
     */
    @RequestMapping(value = "/room/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String createRoom(@RequestBody JSONObject jsonData) {
        int playerId = jsonData.getInteger("playerId");
        matchService.leaveRoom(playerId);//进入新的房间前,先离开老房间
        int newRoomId = matchService.createRoom(playerId);
        JSONObject resJson = new JSONObject();
        if (newRoomId == 0) {//房间创建失败
            resJson.put("status", false);
        } else {
            resJson.put("status", true);
            resJson.put("roomId", newRoomId);
        }
        return resJson.toJSONString();
    }

    @RequestMapping(value = "/room/leave", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String leaveRoom(@RequestBody JSONObject jsonData) {
        int playerId = jsonData.getInteger("playerId");
        Status status = matchService.leaveRoom(playerId);
        JSONObject resJson = new JSONObject();
        if (status != Status.LEAVE_SUCCEED) {//房间离开失败
            resJson.put("status", false);
        } else {
            resJson.put("status", true);
        }
        return resJson.toJSONString();
    }

    @RequestMapping("/getAllRoom")
    public List<Room> getAllRoom() {
        return matchService.getAllRoom();
    }
}


