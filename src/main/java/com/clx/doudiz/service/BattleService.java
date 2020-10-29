package com.clx.doudiz.service;

import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.domain.Room;
import com.clx.doudiz.domain.enums.Status;

public interface BattleService {
    /*争夺地主阶段*/
    int STAGE_NO_CALL_LANDLORD = 600;//暂时无人叫地主
    int STAGE_ROB_LANDLORD = 601;//已经有人叫过地主了,接下来只能抢地主
    /*游戏阶段*/
    int STAGE_FIRST_CARD = 700;//非跟牌阶段,可以任意出牌
    int STAGE_FOLLOW_CARD = 702;//跟牌阶段
    int STAGE_LANDLORD_WIN = 777;//地主胜利
    int STAGE_PEASANT_WIN = 788;//农民胜利
    /*身份*/
    int BLANK = 603;//还没轮到叫地主或者抢地主
    int NOT_CALL =604;//不叫地主
    int CALL_LANDLORD = 605;//叫地主 身份
    int ROB_LANDLORD = 606;//抢地主 身份
    int PEASANT = 680;//农民
    int LANDLORD = 690;//地主
    
    Status createBattle(Room room);

    JSONObject getBattleDetails(int playerId,int roomId);

}
