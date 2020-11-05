package com.clx.doudiz.domain.enums;

public enum Status {
    /*Room使用的状态码*/
    /**未知状态,不允许使用*/
    UNKNOWN(0),
    /**有玩家但是没有满员*/
    NOT_FULL(1),
    /**房间满员,但是有玩家没有准备*/
    FULL_NOT_READY(3),
    /**房间满员,玩家全部准备完毕,等待服务器生成battle开始游戏*/
    FULL_AND_READY(4),
    /**正在游戏*/
    PLAYING(5),
    /** 房间为空,需要清理 */
    EMPTY(104),

    /*600到800的状态码属于Battle使用,在BattleService中定义*/

    /*以下状态不允许Dao层使用,网络传输的状态码*/
    /** 进入成功 */
    ENTER_SUCCEED(200),
    /** 离开成功 */
    LEAVE_SUCCEED(202),
    /**出牌成功*/
    PUSH_SUCCEED(203),
    /** 成功弃牌 */
    ABANDON_SUCCEED(204),
    /** 获得战局详情成功 */
    GET_BATTLE_SUCCEED(204),
    /**该房间已满*/
    FULL(401),
    /**房间或用户或战局不存在*/
    NOT_EXIST(404),
    /**传递json缺失或者错误*/
    JSON_ERROR(411),
    /**未知错误*/
    UNKNOWN_ERROR(444);


    private int code;

    Status(int code) {
        this.code = code;
    }


    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
