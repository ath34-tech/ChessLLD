package com.chess.Helper;

import com.chess.GameCoreEntity.Board;
import com.chess.GameCoreEntity.Piece;

public final class Cell {
    private final Position position;
    private final Board board;
    private Piece piece;
    public Cell(Position position,Board board){
        this.position=position;
        this.board=board;
        this.piece=null;
    }
    public boolean isEmpty(){
        return (piece == null);
    }
    public Piece getPiece(){
            return this.piece;
    }
    public void setPiece(Piece piece){
        this.piece=piece;
    }
    public void removePiece(){
        this.piece=null;
    }
    public Position getPosition(){
        return this.position;
    }
}
