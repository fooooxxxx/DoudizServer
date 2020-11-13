package com.clx.doudiz.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.domain.Battle;
import com.clx.doudiz.domain.Card;
import com.clx.doudiz.domain.Room;
import com.clx.doudiz.domain.enums.Status;
import com.clx.doudiz.domain.mapper.BattleMapper;
import com.clx.doudiz.domain.mapper.PlayerMapper;
import com.clx.doudiz.domain.mapper.RoomMapper;
import com.clx.doudiz.service.BattleService;
import com.clx.doudiz.tools.PublicTools;
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
        room.setBattleId(room.getRoomId());//为room设置battleId
        return null;
    }

    @Override
    public Status resetBattle(int roomId) {
        Room room = roomMapper.selectRoomById(roomId);
        if(room!=null) createBattle(room);
        return null;
    }

    @Override
    public boolean assignLandlord(Battle battle, int assignPlayer) {
        if (battle.getBattleStatus() != STAGE_NO_CALL_LANDLORD && battle.getBattleStatus() != STAGE_ROB_LANDLORD) {
            //如果不在抢地主或者叫地主阶段,则返回false
            return false;
        }
        JSONArray identityList = battle.getIdentity();//首先分配身份
        identityList.set(assignPlayer, LANDLORD);//分配地主
        /*修改出牌者为地主,倒计时为35秒*/
        battle.setCurrentPlayer(assignPlayer);
        battle.setCountDown(LANDLORD_COUNTDOWN);
        /*为地主分配额外的牌*/
        JSONArray handCardList = battle.getPlayerHandCards();
        handCardList.set(assignPlayer, handCardList.getString(assignPlayer) + "," + battle.getExtraCards());
        /*分配农民身份*/
        assignPlayer = nextPlayer(assignPlayer);
        identityList.set(assignPlayer, PEASANT);
        identityList.set(nextPlayer(assignPlayer), PEASANT);

        //battleMapper.updateBattle(battle);
        return true;
    }

    @Override
    public Status abandonPushCard(int battleId, int playerId) {

        Battle battle = battleMapper.selectBattleById(battleId);
        if(battle == null) return Status.NOT_EXIST;
        int nowPlayer = getPlayerPosition(battle,playerId);
        if(nowPlayer == -1 ) return Status.NOT_EXIST;
        /*禁止非跟牌阶段弃牌*/
        if(battle.getBattleStatus() ==STAGE_FIRST_CARD){
            return Status.NOT_EXIST;//懒得改了,就用这个错误码
        }
        return abandonPushCard(battle,nowPlayer);
    }

    @Override
    public Status abandonPushCard(Battle battle, int playerPosition) {
        JSONArray showCardList = battle.getPlayerShowCards();
        showCardList.set(playerPosition, "");//设置为不出牌状态
        if (showCardList.get(lastPlayer(playerPosition)).equals("")) {
            //如果上家也是不出牌,则进入STAGE_FIRST_CARD,让下家出牌
            //清空showCardList所有卡牌
            showCardList.set(0,"");
            showCardList.set(1,"");
            showCardList.set(2,"");
            battle.setBattleStatus(STAGE_FIRST_CARD);
        }
        battle.setCurrentPlayer(nextPlayer(playerPosition));//出牌者设置为下家
        showCardList.set(battle.getCurrentPlayer(),"");
        battle.setCountDown(NORMAL_COUNTDOWN);
        battleMapper.updateBattle(battle);//数据更新到数据库
        return Status.ABANDON_SUCCEED;
    }

    @Override
    public String getExtraCard(int battleId) {
        Battle battle = battleMapper.selectBattleById(battleId);
        if(battle == null) return "";
        return battle.getExtraCards();
    }

    @Override
    public JSONObject getBattleDetails(int playerId, int roomId) {
        JSONObject jsonResult = new JSONObject();
        Battle battle = battleMapper.selectBattleById(roomId);
        jsonResult.put("playerId", playerId);
        if (battle != null) {//如果battle存在
            int nowPlayer = battle.getPlayerList().indexOf(playerId);
            if (nowPlayer != -1) {//如果用户存在
                //手牌只获取自己的
                jsonResult.put("handCards",battle.getPlayerHandCards().get(nowPlayer));
                jsonResult.put("showCards",battle.getPlayerShowCards());
                jsonResult.put("playerList",battle.getPlayerList());
                jsonResult.put("currentPlayer",battle.getCurrentPlayer());
                jsonResult.put("battleStatus",battle.getBattleStatus());
                jsonResult.put("countdown",battle.getCountDown());
                jsonResult.put("identity",battle.getIdentity());
                jsonResult.put("extraCardStr",battle.getExtraCards());
                jsonResult.put("status",true);
                jsonResult.put("statusCode",Status.GET_BATTLE_SUCCEED.getCode());
                return jsonResult;
            }

        }
        //如果battle不存在
        jsonResult.put("status", false);
        jsonResult.put("statusCode", Status.NOT_EXIST.getCode());
        return jsonResult;
    }

    @Override
    public List<Battle> getBattleListInPlaying() {
        return battleMapper.selectBattleInPlaying();
    }

    @Override
    public boolean executeBattle(Battle battle) {
        final int countDown = battle.autoReduceCountDown();
        System.out.println(countDown);
        final JSONArray identityList = battle.getIdentity();//身份列表
        int nowPlayer = battle.getCurrentPlayer();//获得当前玩家顺位,比如0,1,2
        final int battleStatus = battle.getBattleStatus();//获得当前状态
        JSONArray showCardList = battle.getPlayerShowCards();
        JSONArray handCardList = battle.getPlayerHandCards();
        if (battleStatus == STAGE_NO_CALL_LANDLORD) {
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
        } else if (battleStatus == STAGE_FIRST_CARD) {
            if (countDown <= 0 ) {
                //随机出一张单牌
                pushCardByIndex(0,battle,nowPlayer,true);
                if(checkWinner(battle) ==-1) battle.setBattleStatus(STAGE_FOLLOW_CARD);
            }
        } else if (battleStatus == STAGE_FOLLOW_CARD) {
            if (countDown <= 0) {
                abandonPushCard(battle,nowPlayer);//自动放弃出牌
            }
        } else if (battleStatus == STAGE_PEASANT_WIN) {

        } else if (battleStatus == STAGE_LANDLORD_WIN) {

        }
        battleMapper.updateBattle(battle);//更新battle到数据库
        return true;
    }

    Battle initBattle(Room room) {
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
        for (int type = 1; type <= 4; type++) {
            for (int value = 3; value <= 15; value++) {//从3到A
                originCards.add(type + "_" + value);
            }
        }
        originCards.add(5 + "_" + "16");
        originCards.add(5 + "_" + "17");

        for (int i = 0; i < 3; i++) {//分别为三个人发牌
            StringBuilder cardStr = new StringBuilder();
            for (int j = 0; j < 17; j++) {//发17张牌
                int rand = (int) (Math.random() * originCards.size());
                cardStr.append(originCards.remove(rand));
                if (j != 16) cardStr.append(",");//如果不是末尾,则添加逗号进行分割
            }
            handCardList.add(cardStr.toString());
            //添加额外的三张牌
            extraCard.append(originCards.remove(originCards.size() - 1));
            if (i != 2) extraCard.append(",");
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
        newBattle.setCountDown(LANDLORD_COUNTDOWN);//
        newBattle.setCurrentPlayer(0);//当前由第一位玩家操作
        newBattle.setIdentity(identityList);
        newBattle.setBattleStatus(BattleService.STAGE_NO_CALL_LANDLORD);//开始阶段

        /*临时方案,随机分配地主*/
        int randomLandlord = (int) (Math.random() * 3);//生成0到2的随机值,用于分配地主
        assignLandlord(newBattle, randomLandlord);
        newBattle.setBattleStatus(STAGE_FIRST_CARD);//设置为可出牌阶段
        return newBattle;

    }

    /**
     * 获取下家玩家顺位
     *
     * @param currentPlayer 当前玩家顺位
     * @return 下家的玩家顺位, 即0, 1, 2
     */
    public static int nextPlayer(int currentPlayer) {
        if (++currentPlayer > 2) {//轮流出牌
            currentPlayer = 0;
        }
        return currentPlayer;
    }

    /**
     * 获取上家玩家顺位
     *
     * @param currentPlayer 当前玩家顺位
     * @return 上家的玩家顺位, 即0, 1, 2
     */
    public static int lastPlayer(int currentPlayer) {
        if (--currentPlayer < 0) {
            currentPlayer = 2;
        }
        return currentPlayer;
    }


    @Override
    public int checkWinner(Battle battle){
        JSONArray handCardArray = battle.getPlayerHandCards();
        for(int i = 0;i < handCardArray.size();i++){
            System.out.println(handCardArray.getString(i)+"-");
            if(handCardArray.getString(i).equals("")){
                //与空字符串比较,说明牌被打空了
                JSONArray identityArray = battle.getIdentity();
                if(identityArray.getIntValue(i) == LANDLORD){
                    //如果获胜者是地主
                    battle.setBattleStatus(STAGE_LANDLORD_WIN);
                }else{//如果获胜者是农民
                    battle.setBattleStatus(STAGE_PEASANT_WIN);
                }
                return i;
            }
        }
        return -1;
    }

    /** 清空展示区,将所出的牌加入到展示区中
     * @param pushCardList 出的牌组
     * @param battle 牌局
     * @param playerPosition 出牌者顺位
     */
    public static void addShowCard(List<Card> pushCardList,Battle battle, int playerPosition){
        JSONArray showArray = battle.getPlayerShowCards();
        showArray.set(playerPosition,Card.convertToString(pushCardList));
    }

    /** 自动出牌时使用,清空展示区,并将自动出的单牌加入到展示区中
     * @param pushCard 自动所出的单牌
     * @param battle 牌局
     * @param playerPosition 出牌者顺位
     */
    public static void addShowCard(Card pushCard,Battle battle, int playerPosition){
        JSONArray showArray = battle.getPlayerShowCards();
        showArray.set(playerPosition,pushCard.toString());
    }

    @Override
    public Status pushCardByString(String pushCardStr, int battleId, int playerId) {
        Battle battle = battleMapper.selectBattleById(battleId);
        if(battle == null) return Status.NOT_EXIST;
        int playerPosition = getPlayerPosition(battle,playerId);
        if(playerPosition==-1) return Status.NOT_EXIST;//判断该玩家是否存在于该battle中
        JSONArray handCardArray = battle.getPlayerHandCards();
        List<Card> pushCardList = Card.convertToCardList(pushCardStr);
        List<Card> originCardList = Card.convertToCardList(handCardArray.getString(playerPosition));
        if(!Card.removeInCardList(originCardList,pushCardList)){
            //出牌失败
            return Status.NOT_EXIST;
        }
        //出牌成功后将牌组加入到展示区
        addShowCard(pushCardList,battle,playerPosition);
        //修改手牌为打出牌后的手牌
        handCardArray.set(playerPosition,Card.convertToString(originCardList));
        //切换出牌者为下家
        battle.setCurrentPlayer(PublicTools.playerCyclic(playerPosition));
        //重置倒计时
        battle.setCountDown(NORMAL_COUNTDOWN);
        battle.setBattleStatus(STAGE_FOLLOW_CARD);
        //清空下家的展示区
        JSONArray showCards = battle.getPlayerShowCards();
        showCards.set(battle.getCurrentPlayer(),"");
        checkWinner(battle);
        //更新battle
        battleMapper.updateBattle(battle);
        return Status.PUSH_SUCCEED;
    }

//    @Override
//    public boolean pushCardByIndex(int pushCardIndex, Battle battle, int playerId) {
//        int playerPosition = getPlayerPosition(battle,playerId);
//        if(playerPosition==-1) return false;//判断该玩家是否存在于该battle中
//        return pushCardByIndex(pushCardIndex,battle,playerId,true);
//    }

    @Override
    public boolean pushCardByIndex(int pushCardIndex, Battle battle, int playerPosition, boolean isSort) {
        JSONArray handCardArray = battle.getPlayerHandCards();
        List<Card> originCardList = Card.convertToCardList(handCardArray.getString(playerPosition));
        if(originCardList.size()<=pushCardIndex) return false;//防止下标越界
        //移除originCardList中的所出牌,并将其添加到展示区
        addShowCard(originCardList.remove(pushCardIndex),battle,playerPosition);
        handCardArray.set(playerPosition,Card.convertToString(originCardList));
        //切换出牌者为下家
        battle.setCurrentPlayer(PublicTools.playerCyclic(playerPosition));
        //清空下家展示区
        JSONArray showCards = battle.getPlayerShowCards();
        showCards.set(battle.getCurrentPlayer(),"");
        //重置倒计时
        battle.setCountDown(NORMAL_COUNTDOWN);
        battleMapper.updateBattle(battle);//更新battle
        return true;
    }

    @Override
    public int getPlayerPosition(Battle battle, int playerId) {
        JSONArray playerIdList = battle.getPlayerList();
        for(int i = 0;i<playerIdList.size();i++){
            if(playerId == playerIdList.getIntValue(i)){
                return i;
            }
        }
        return -1;
    }
}
