package com.clx.doudiz.domain.mapper;

import com.clx.doudiz.domain.Player;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper 因为DoudizApplication中已经添加了注解,所以不需要再加这个注解了
@Repository
public interface PlayerMapper {

    @Select("SELECT * FROM player")
    List<Player> getAllPlayer();


    Player findPlayerById(int playerId);

    /** 将新player插入到数据库中
     * @param player 新的Player对象
     * @return Player的id
     */
    int insertPlayer(Player player);
}
