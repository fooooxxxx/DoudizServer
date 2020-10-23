package com.clx.doudiz.service.impl;

import com.clx.doudiz.domain.Player;
import com.clx.doudiz.domain.mapper.PlayerMapper;
import com.clx.doudiz.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public List<Player> getAllPlayer() {
        return playerMapper.getAllPlayer();
    }

    @Override
    public int createPlayer(String playName){
        Player newPlayer = new Player(playName);
        return playerMapper.insertPlayer(newPlayer);
    }
}
