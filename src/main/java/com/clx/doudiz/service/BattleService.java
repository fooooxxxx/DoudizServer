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
    int LANDLORD_COUNTDOWN =25;//地主第一次出牌的倒计时
    int NORMAL_COUNTDOWN =20;//正常的出牌倒计时


    Status createBattle(Room room);

    Status resetBattle(int roomId);

    /** 为指定玩家分配地主,将出牌者设置为地主,倒计时35秒,以及额外的牌分配给地主
     * @param assignPlayer 玩家顺位,比如0,1,2
     *
     */
    boolean assignLandlord(Battle battle,int assignPlayer);

    JSONObject getBattleDetails(int playerId,int roomId);

    List<Battle> getBattleListInPlaying();

    /** 放弃出牌,让下家出牌,供controller层使用
     * @param battleId 战局ID
     * @param playerId 玩家ID
     * @return 返回状态,是否成功
     */
    Status abandonPushCard(int battleId,int playerId);
    /** 放弃出牌,让下家出牌
     * @param battle 战局
     * @param playerPosition 当前玩家顺位
     * @return 返回状态
     */
    Status abandonPushCard(Battle battle,int playerPosition);

    boolean executeBattle(Battle battle);

    /** 尝试进行出牌操作;
     * 出牌成功后,将currentPlayer设置为下家;
     * 重置倒计时
     * @param pushCardStr 出的牌
     * @param battleId 牌局ID
     * @param playerId 出牌者ID
     * @return false代表出牌失败,true代表出牌成功
     */
    Status pushCardByString(String pushCardStr,int battleId,int playerId);

    /** 检查对局中是否有玩家牌全部出完,如果有,设置地主胜利.
     * 如果胜利玩家是农民,则设置农民胜利
     * @param battle 需要检查的battle
     * @return 返回获胜的玩家顺位,如果返回-1,说明暂时无人胜利
     */
    int checkWinner(Battle battle);

    /**尝试进行通过下标出牌,仅供用于自动出牌;
     * 出牌成功后将currentPlayer切换成下家
     * 该方法通过顺位进行出牌,会自动重置倒计时
     * @param pushCardIndex 出牌下标
     * @param battle 牌局
     * @param playerPosition 出牌者顺位
     * @param isSort 是否进行排序
     * @return 出牌成功为true,失败为false
     */
    boolean pushCardByIndex(int pushCardIndex,Battle battle,int playerPosition,boolean isSort);

    /** 获得玩家顺位
     * @param battle 战局
     * @param playerId 所需要定位的玩家ID
     * @return 返回玩家的顺位,0,1,2;如果玩家不存在于该Battle中,则返回-1
     */
    int getPlayerPosition(Battle battle,int playerId);

//    /** 尝试进行通过下标出牌,出牌成功后检查是否有玩家胜出
//     * @param pushCardIndex 所出牌的下标
//     * @param battle 牌局
//     * @param playerId 出牌者ID
//     * @return 出牌成功为true,失败为false
//     */
//    boolean pushCardByIndex(int pushCardIndex,Battle battle,int playerId);
}
