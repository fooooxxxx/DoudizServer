package com.clx.doudiz.controller;


import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.domain.Battle;
import com.clx.doudiz.domain.enums.Status;
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

    /*请求房间和战局当前详情*/
    @RequestMapping(value = "/battle/details", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getBattleDetails(@RequestBody JSONObject jsonData) {
        int playerId = jsonData.getInteger("playerId");
        int roomId = jsonData.getInteger("roomId");
        roomService.checkRoomStatus(roomId);
        return battleService.getBattleDetails(playerId, roomId).toJSONString();
    }

    /**
     * 进行弃牌操作,非跟牌阶段禁止弃牌
     *
     * @param jsonData {"battleId":int,"playerId":int}
     * @return 返回json字符串 {"status":boolean,"statusCode":int} status代表其请求是否成功
     */
    @RequestMapping(value = "/battle/card/abandon", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String abandonPushCard(@RequestBody JSONObject jsonData) {
        JSONObject resJson = new JSONObject();
        if (!jsonData.containsKey("battleId") || !jsonData.containsKey("playerId")) {
            resJson.put("status", false);
            resJson.put("statusCode", Status.JSON_ERROR.getCode());
        } else {
            int battleId = jsonData.getInteger("battleId");
            int playerId = jsonData.getInteger("playerId");
            Status resStatus = battleService.abandonPushCard(battleId, playerId);
            if (resStatus == Status.ABANDON_SUCCEED) {
                resJson.put("status", true);
            } else resJson.put("status", false);
            resJson.put("statusCode", resStatus.getCode());
        }
        return resJson.toJSONString();
    }

    /** 出牌
     * @param jsonData {"battleId":int,"playerId":int,"pushCardStr":String}
     *                 pushCardStr 为出牌牌组的String
     * @return 返回status和"statusCode
     */
    @RequestMapping(value = "/battle/card/push", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String pushCard(@RequestBody JSONObject jsonData) {
        JSONObject resJson = new JSONObject();
        if (jsonData.containsKey("battleId") && jsonData.containsKey("playerId")
                && jsonData.containsKey("pushCardStr")) {
            Status status = battleService.pushCardByString(jsonData.getString("pushCardStr"),
                    jsonData.getInteger("battleId"), jsonData.getInteger("playerId"));
            if (status == Status.PUSH_SUCCEED){
                resJson.put("status",true);//如果出牌成功
                resJson.put("statusCode",Status.PUSH_SUCCEED.getCode());
            }else{
                resJson.put("status",false);
                resJson.put("statusCode",status.getCode());
            }
        } else {
            resJson.put("status", false);//传递json参数错误
            resJson.put("statusCode", Status.JSON_ERROR.getCode());
        }
        return resJson.toJSONString();
    }

    /** 重置battle,测试用方法
     * @param jsonData {"roomId":int}
     * @return 必定返回true
     */
    @RequestMapping(value = "/battle/reset",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String resetBattle(@RequestBody JSONObject jsonData){
        JSONObject resJson = new JSONObject();
        battleService.resetBattle(jsonData.getInteger("roomId"));
        resJson.put("status",true);
        return resJson.toJSONString();
    }



}
