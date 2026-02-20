package com.chess.GameCoreEntity.Pieces;

import com.chess.GameCoreEntity.Board;
import com.chess.GameCoreEntity.Piece;
import com.chess.GameCoreEntity.Player;
import com.chess.Helper.Cell;
import com.chess.Helper.MovementScanner;
import com.chess.Relational.Move;
import com.chess.Types.Color;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Color color, Player owner,Cell cell){
        super(color,owner,cell);
    }
    @Override
    public List<Move> getLegalMoves(Board board){
        List<Move> moves=new ArrayList<>();
        MovementScanner.scanDirection(board, this, moves, 1, 1);   // down
        MovementScanner.scanDirection(board, this, moves, -1, -1);  // up

        MovementScanner.scanDirection(board, this, moves, 1, -1);   // right
        MovementScanner.scanDirection(board, this, moves, -1, 1);  // left

        return moves;
    }

    @Override
    public String getName() {
        return "b";
    }
}
