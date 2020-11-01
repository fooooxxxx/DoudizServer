package com.clx.doudiz.task;


import com.alibaba.fastjson.JSONArray;
import com.clx.doudiz.domain.Battle;
import com.clx.doudiz.domain.Room;
import com.clx.doudiz.service.BattleService;
import com.clx.doudiz.service.RoomService;
import com.clx.doudiz.tools.PublicTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
@EnableAsync
public class BattleScheduleTask {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BattleService battleService;

    /**
     * 自动检查房间状态
     * 用于清除空房间,为人满的房间创建Battle
     */
    @Async
    @Scheduled(fixedDelay = 5000)
    public void autoCheckBattle(){
//        System.out.println("自动检查房间状态 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
//        System.out.print("自动检查房间状态 : " + LocalDateTime.now().toLocalTime() );
        List<Room> roomList =  roomService.getAllRoom();
        for(Room room : roomList){
            roomService.checkRoomStatus(room.getRoomId());//每5秒钟检查room状态
        }
//        System.out.println("---房间状态检查完毕 : " + LocalDateTime.now().toLocalTime() );
    }

    /**
     * 减少倒计时,并且处理各种清空
     */
    @Async
    @Scheduled(fixedDelay = 1000)
    public void autoExecuteBattle(){
        List<Battle> battleList = battleService.getBattleListInPlaying();
        for(Battle battle : battleList){
            battleService.executeBattle(battle);
        }
    }


}
