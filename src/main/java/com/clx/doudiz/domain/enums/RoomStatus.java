package com.clx.doudiz.domain.enums;

public enum RoomStatus {
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
    EMPTY(104);


    private int code;

    RoomStatus(int code) {
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
