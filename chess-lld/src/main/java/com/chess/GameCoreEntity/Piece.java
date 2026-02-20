package com.chess.GameCoreEntity;

import com.chess.Helper.Cell;
import com.chess.Relational.Move;
import com.chess.Types.Color;

import java.util.List;

public abstract class Piece {
    private Color color;
    private Cell currentCell;
    private Player owner;

    protected Piece(Color color,Player owner,Cell cell){
        this.color=color;
        this.owner=owner;
        this.currentCell=cell;
    }

//    Getters
    public Color getColor(){
        return this.color;
    }
    public Cell getCell(){
        return this.currentCell;
    }
    public Player getOwner(){
        return this.owner;
    }


//    Setters
    public void setCurrentCell(Cell cell){
        this.currentCell=cell;
    }

    public abstract List<Move> getLegalMoves(Board board);
    public abstract String getName();
    public void move(Move move){
        Cell source = move.getFrom();
        Cell destination = move.getTo();

        if (!destination.isEmpty()) {
            move.setCapturedPiece(destination.getPiece());
        }

        source.setPiece(null);
        destination.setPiece(this);
        setCurrentCell(destination);
    };
}
