package com.clx.doudiz;

import com.alibaba.fastjson.JSONObject;
import com.clx.doudiz.domain.Battle;
import com.clx.doudiz.domain.mapper.BattleMapper;
import com.clx.doudiz.service.BattleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseTests {
    @Autowired
    DataSource dataSource;

    @Autowired
    BattleMapper battleMapper;

    @Autowired
    BattleService battleService;

    @Test
    public void contextLoads() throws SQLException{
        System.out.println(dataSource.getConnection());
        Battle battle = battleMapper.selectBattleById(22);
        System.out.println(battle.getPlayerList().toJSONString());

    }

}
