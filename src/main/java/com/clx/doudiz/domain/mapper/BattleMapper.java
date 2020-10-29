package com.clx.doudiz.domain.mapper;


import com.clx.doudiz.domain.Battle;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleMapper {

    @Select("SELECT * FROM battle WHERE battle_id = #{battleId}")
    Battle selectBattleById(int battleId);

    boolean insertBattle(Battle battle);

    @Delete("DELETE FROM battle WHERE battle_id = #{battleId}")
    boolean deleteBattleById(int battleId);


}
