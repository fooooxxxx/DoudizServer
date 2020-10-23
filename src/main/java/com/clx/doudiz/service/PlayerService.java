package com.clx.doudiz.service;

import com.clx.doudiz.domain.Player;

import java.util.List;

public interface PlayerService {
    List<Player> getAllPlayer();

    int createPlayer(String playerName);

}
