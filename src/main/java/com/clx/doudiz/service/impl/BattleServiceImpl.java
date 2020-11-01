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
import java.util.List;

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
    public boolean assignLandlord(Battle battle, int assignPlayer) {
        if(battle.getBattleStatus()!=STAGE_NO_CALL_LANDLORD && battle.getBattleStatus()!=STAGE_ROB_LANDLORD){
            //如果不在抢地主或者叫地主阶段,则返回false
            return false;
        }
        JSONArray identityList = battle.getIdentity();//首先分配身份
        identityList.set(assignPlayer,LANDLORD);//分配地主
        /*修改出牌者为地主,倒计时为35秒*/
        battle.setCurrentPlayer(assignPlayer);
        battle.setCountDown(35);
        /*为地主分配额外的牌*/
        JSONArray handCardList = battle.getPlayerHandCards();
        handCardList.set(assignPlayer,handCardList.getString(assignPlayer)+","+battle.getExtraCards());
        /*分配农民身份*/
        assignPlayer = nextPlayer(assignPlayer);
        identityList.set(assignPlayer,PEASANT);
        identityList.set(nextPlayer(assignPlayer),PEASANT);

        //battleMapper.updateBattle(battle);
        return true;
    }



    @Override
    public JSONObject getBattleDetails(int playerId, int roomId) {
        JSONObject jsonResult = new JSONObject();
        Battle battle = battleMapper.selectBattleById(roomId);
        jsonResult.put("playerId",playerId);
        if(battle!=null){//如果battle存在
            int nowPlayer = battle.getPlayerList().indexOf(playerId);


        }else{//如果battle不存在
            jsonResult.put("status",false);
            jsonResult.put("statusCode",Status.NOT_EXIST);
        }


        return jsonResult;
    }

    @Override
    public List<Battle> getBattleListInPlaying() {
        return battleMapper.selectBattleInPlaying();
    }

    @Override
    public boolean executeBattle(Battle battle){
        final int countDown = battle.autoReduceCountDown();
        final JSONArray identityList = battle.getIdentity();//身份列表
        int nowPlayer = battle.getCurrentPlayer();//获得当前玩家顺位,比如0,1,2
        final int battleStatus = battle.getBattleStatus();//获得当前状态
        JSONArray showCardList = battle.getPlayerShowCards();
        JSONArray handCardList = battle.getPlayerHandCards();
        if(battleStatus == STAGE_NO_CALL_LANDLORD) {
            //叫地主阶段
            //该阶段暂时被废弃
//            if(countDown <= 0){//如果倒计时结束
//                identityList.set(nowPlayer,NOT_CALL);//当前玩家不叫地主
//                nowPlayer = playerCyclic(nowPlayer);//轮到下一名玩家
//                battle.setCurrentPlayer(nowPlayer);//对实体进行更新
//                //如果没有人叫地主
//                if(identityList.getIntValue(nowPlayer)==NOT_CALL){
//                    //直接为nowPlayer分配地主
//                    battle.setBattleStatus(STAGE_FIRST_CARD);
//                    assignLandlord(battle,nowPlayer);
//                }else{//允许下一个玩家叫地主
//                    battle.setCountDown(15);
//                }
//            }
//        }else if(battleStatus == STAGE_ROB_LANDLORD){
//            //抢地主阶段
//            if(countDown <= 0){
//                if(identityList.getIntValue(nowPlayer) == CALL_LANDLORD){
//                    //如果该玩家是叫地主的玩家,放弃了抢地主,可以直接指定上一位抢地主玩家为地主
//
//                }else if(identityList.getIntValue(nowPlayer)== BLANK){
//                    identityList.set(nowPlayer,BLANK);
//
//                }
//            }
//        }
        }else if(battleStatus == STAGE_FIRST_CARD){
            if(countDown == 0){
                //出最右边的一张单牌
            }
        }else if(battleStatus == STAGE_FOLLOW_CARD){
            if(countDown<=0) {
                showCardList.set(nowPlayer, "");//设置为不出牌状态
                if (showCardList.get(lastPlayer(nowPlayer)).equals("")) {
                    //如果上家也是不出牌,则进入STAGE_FIRST_CARD,让下家出牌
                    battle.setBattleStatus(STAGE_FIRST_CARD);
                }
                battle.setCurrentPlayer(nextPlayer(nowPlayer));//出牌者设置为下家
            }
        }else if(battleStatus == STAGE_PEASANT_WIN){

        }else if(battleStatus == STAGE_LANDLORD_WIN){

        }
        battleMapper.updateBattle(battle);//更新battle到数据库
        return true;
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
        originCards.add(5+"_"+"16");
        originCards.add(5+"_"+"17");

        for(int i = 0;i < 3;i++){//分别为三个人发牌
            StringBuilder cardStr = new StringBuilder();
            for(int j = 0;j < 17;j++){//发17张牌
                int rand = (int) (Math.random() * originCards.size());
                cardStr.append(originCards.remove(rand));
                if(j!=16) cardStr.append(",");//如果不是末尾,则添加逗号进行分割
            }
            handCardList.add(cardStr.toString());
            //添加额外的三张牌
            extraCard.append(originCards.remove(originCards.size()-1));
            if(i!=2) extraCard.append(",");
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

        /*紧急方案,随机分配地主*/
        int randomLandlord = (int)(Math.random()*3);//生成0到2的随机值,用于分配地主
        assignLandlord(newBattle,randomLandlord);
        newBattle.setBattleStatus(STAGE_FIRST_CARD);//设置为可出牌阶段
        return newBattle;

    }

    /** 获取下家玩家顺位
     * @param currentPlayer 当前玩家顺位
     * @return 下家的玩家顺位,即0,1,2
     */
    public static int nextPlayer(int currentPlayer){
        if(++currentPlayer > 2){//轮流出牌
            currentPlayer = 0;
        }
        return currentPlayer;
    }

    /** 获取上家玩家顺位
     * @param currentPlayer  当前玩家顺位
     * @return 上家的玩家顺位,即0,1,2
     */
    public static int lastPlayer(int currentPlayer){
        if(--currentPlayer < 0){
            currentPlayer = 2;
        }
        return currentPlayer;
    }

}
