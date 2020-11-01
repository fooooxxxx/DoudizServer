package com.clx.doudiz.service;

import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.domain.Battle;
import com.clx.doudiz.domain.Room;
import com.clx.doudiz.domain.enums.Status;

import java.util.List;

public interface BattleService {
    /*争夺地主阶段*/
    int STAGE_NO_CALL_LANDLORD = 701;//暂时无人叫地主
    int STAGE_ROB_LANDLORD = 705;//已经有人叫过地主了,接下来只能抢地主
    /*游戏阶段*/
    int STAGE_FIRST_CARD = 710;//非跟牌阶段,可以任意出牌
    int STAGE_FOLLOW_CARD = 720;//跟牌阶段
    int STAGE_LANDLORD_WIN = 777;//地主胜利
    int STAGE_PEASANT_WIN = 788;//农民胜利
    /*身份*/
    int BLANK = 600;//还没轮到叫地主或者抢地主
    int NOT_CALL =604;//不叫地主
    int CALL_LANDLORD = 605;//叫地主 身份
    int ROB_LANDLORD = 606;//抢地主 身份
    int PEASANT = 680;//农民
    int LANDLORD = 690;//地主
    /*别的参数*/
    int LANDLORD_COUNTDOWN =35;//地主第一次出牌的倒计时
    int NORMAL_COUNTDOWN =30;//正常的出牌倒计时


    Status createBattle(Room room);

    /** 为指定玩家分配地主,将出牌者设置为地主,倒计时35秒,以及额外的牌分配给地主
     * @param assignPlayer 玩家顺位,比如0,1,2
     *
     */
    boolean assignLandlord(Battle battle,int assignPlayer);

    JSONObject getBattleDetails(int playerId,int roomId);

    List<Battle> getBattleListInPlaying();



    boolean executeBattle(Battle battle);

}
