package com.chess.GameCoreEntity;

import com.chess.Types.Color;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String username;
    private final Color color;
    private final List<Piece> capturedPieces;

    public Player(String username, Color color) {
        this.username = username;
        this.color = color;
        this.capturedPieces = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public Color getColor() {
        return color;
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public void addCapturedPiece(Piece piece) {
        capturedPieces.add(piece);
    }
}