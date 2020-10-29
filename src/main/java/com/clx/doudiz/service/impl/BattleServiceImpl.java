package com.clx.doudiz.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.domain.Battle;
import com.clx.doudiz.domain.Room;
import com.clx.doudiz.domain.enums.Status;
import com.clx.doudiz.domain.mapper.BattleMapper;
import com.clx.doudiz.domain.mapper.PlayerMapper;
import com.clx.doudiz.domain.mapper.RoomMapper;
import com.clx.doudiz.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BattleServiceImpl implements BattleService {
    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    BattleMapper battleMapper;


    @Override
    public Status createBattle(Room room) {
        battleMapper.deleteBattleById(room.getRoomId());//首先删除之前的battle
        battleMapper.insertBattle(initBattle(room));
        return null;
    }

    @Override
    public JSONObject getBattleDetails(int playerId, int roomId) {
        JSONObject jsonResult = new JSONObject();

        return jsonResult;
    }

    Battle initBattle (Room room){
        Battle newBattle = new Battle();
        newBattle.setBattleId(room.getRoomId());//roomId和battleId相同
        JSONArray playerList = new JSONArray();
        JSONArray handCardList = new JSONArray();//手牌
        JSONArray showCardList = new JSONArray();//打出的牌
        StringBuilder extraCard = new StringBuilder();//额外的三张牌
        JSONArray identityList = new JSONArray();//身份

        playerList.add(room.getPlayer1Id());
        playerList.add(room.getPlayer2Id());
        playerList.add(room.getPlayer3Id());

        ArrayList<String> originCards = new ArrayList<>();
        for(int type = 1; type <= 4;type++){
            for(int value = 3; value <= 15;value++){//从3到A
                originCards.add(type+"_"+value);
            }
        }

        for(int i = 0;i < 3;i++){//分别为三个人发牌
            StringBuilder cardStr = new StringBuilder();
            for(int j = 0;j < 17;j++){//发17张牌
                cardStr.append(originCards.remove((int) (Math.random() * originCards.size())));
                if(j!=16) cardStr.append(",");//如果不是末尾,则添加逗号进行分割
            }
            handCardList.add(cardStr.toString());
            //添加额外的三张牌
            extraCard.append(originCards.remove(originCards.size()-1));
        }

        showCardList.add("");
        showCardList.add("");
        showCardList.add("");
        //身份全部设置为空白
        identityList.add(BattleService.BLANK);
        identityList.add(BattleService.BLANK);
        identityList.add(BattleService.BLANK);

        newBattle.setPlayerList(playerList);
        newBattle.setPlayerHandCards(handCardList);
        newBattle.setPlayerShowCards(showCardList);
        newBattle.setExtraCards(extraCard.toString());
        newBattle.setCountDown(15);//倒计时15秒
        newBattle.setCurrentPlayer(0);//当前由第一位玩家操作
        newBattle.setIdentity(identityList);
        newBattle.setBattleStatus(BattleService.STAGE_NO_CALL_LANDLORD);//开始阶段
        return newBattle;
    }


}
