package com.clx.doudiz.domain.mapper;


import com.clx.doudiz.domain.Battle;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BattleMapper {

    @Select("SELECT * FROM battle WHERE battle_id = #{battleId}")
    Battle selectBattleById(int battleId);

    @Select("SELECT * FROM battle")
    List<Battle> selectBattleInPlaying();

    boolean insertBattle(Battle battle);

    @Delete("DELETE FROM battle WHERE battle_id = #{battleId}")
    boolean deleteBattleById(int battleId);

    boolean updateBattle(Battle battle);

}
