package com.example.backend.dto.response;

public class GameResponse {

    private int gameId;
    private String gameName;

    public GameResponse(int gameId, String gameName) {
        this.gameId = gameId;
        this.gameName = gameName;
    }

    public int getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }
}