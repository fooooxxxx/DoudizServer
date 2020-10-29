package com.clx.doudiz.task;


import com.clx.doudiz.domain.Room;
import com.clx.doudiz.service.RoomService;
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

    @Async
    @Scheduled(fixedDelay = 1000)
    public void autoDealBattle(){
        System.out.println("开始自动处理 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
        List<Room> roomList =  roomService.getAllRoom();


    }
}
