package com.clx.doudiz.controller;

import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.domain.Player;
import com.clx.doudiz.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 基础控制器,用于进行创建用户,以及其他操作
 */
@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/player/create",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String createPlayer(@RequestBody JSONObject jsonData){
        String newPlayerName = (String) jsonData.get("playerName");

        JSONObject resJson = new JSONObject();
        resJson.put("status",true);
        resJson.put("playerId",playerService.createPlayer(newPlayerName));
        return resJson.toJSONString();
    }

    @RequestMapping("/")
    public String index(){
        return "啥都没有";
    }

    @RequestMapping("/getAllPlayer")
    public List<Player> getAllPlayer(){
        return playerService.getAllPlayer();
    }




}
