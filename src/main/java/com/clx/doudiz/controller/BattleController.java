package com.clx.doudiz.controller;


import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.service.BattleService;
import com.clx.doudiz.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BattleController {
    @Autowired
    BattleService battleService;

    @Autowired
    RoomService roomService;

    /*请求房间当前详情*/
    @RequestMapping(value = "/battle/details",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String getBattleDetails(@RequestBody JSONObject jsonData){
        int playerId = jsonData.getInteger("playerId");
        int roomId = jsonData.getInteger("roomId");
        roomService.checkRoomStatus(roomId);
        return battleService.getBattleDetails(playerId,roomId).toJSONString();
    }


}
