package com.clx.doudiz.controller;

import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.domain.Player;
import com.clx.doudiz.domain.Room;
import com.clx.doudiz.service.MatchService;
import com.clx.doudiz.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/find/room", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findRoomById(@RequestBody JSONObject jsonData) {
        int roomId = (int) jsonData.get("roomId");

        Room findRoom = matchService.findRoomById(roomId);
        JSONObject resJson = new JSONObject();
        if (findRoom != null) {
            resJson.put("status", true);
        } else {
            resJson.put("status", false);
        }
        return resJson.toJSONString();
    }

    @RequestMapping(value = "/create/room", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String createRoom(@RequestBody JSONObject jsonData) {
        int playerId = (int) jsonData.get("playerId");
        Room newRoom = matchService.createRoom(playerId);
        JSONObject resJson = new JSONObject();
        if (newRoom != null) {
            resJson.put("status", false);
        } else {
            resJson.put("status", true);
            resJson.put("roomId", newRoom.getRoomId());
        }
        return resJson.toJSONString();
    }


    @RequestMapping("/getAllRoom")
    public List<Room> getAllRoom() {
        return matchService.getAllRoom();
    }
}


