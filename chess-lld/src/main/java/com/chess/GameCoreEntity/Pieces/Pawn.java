package com.chess.GameCoreEntity.Pieces;

import com.chess.GameCoreEntity.Board;
import com.chess.GameCoreEntity.Piece;
import com.chess.GameCoreEntity.Player;
import com.chess.Helper.Cell;
import com.chess.Relational.Move;
import com.chess.Types.Color;
import com.chess.Types.MoveType;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Color color, Player owner, Cell cell) {
        super(color, owner, cell);
    }

    @Override
    public List<Move> getLegalMoves(Board board) {

        List<Move> moves = new ArrayList<>();
        Cell current = getCell();

        int direction = (getColor() == Color.WHITE) ? -1 : 1;

        int r = current.getPosition().getRow();
        int c = current.getPosition().getColumn();

        // -------- 1 square forward --------
        int forwardRow = r + direction;
        if(forwardRow >= 0 && forwardRow < 8){

            Cell forward = board.getCell(forwardRow, c);

            if(forward.isEmpty()){
                moves.add(new Move(current, forward, this, MoveType.NORMAL, false, 0));

                // -------- 2 square first move --------
                boolean startingRow =
                        (getColor() == Color.WHITE && r == 6) ||
                                (getColor() == Color.BLACK && r == 1);

                if(startingRow){
                    int twoForwardRow = r + 2 * direction;
                    Cell twoForward = board.getCell(twoForwardRow, c);

                    if(twoForward.isEmpty()){
                        moves.add(new Move(current, twoForward, this, MoveType.NORMAL, true, 0));
                    }
                }
            }
        }

        // -------- capture left --------
        if(c - 1 >= 0){
            Cell diag = board.getCell(r + direction, c - 1);
            if(!diag.isEmpty() && diag.getPiece().getColor() != getColor()){
                moves.add(new Move(current, diag, this, MoveType.CAPTURE, false, 0));
            }
        }

        // -------- capture right --------
        if(c + 1 < 8){
            Cell diag = board.getCell(r + direction, c + 1);
            if(!diag.isEmpty() && diag.getPiece().getColor() != getColor()){
                moves.add(new Move(current, diag, this, MoveType.CAPTURE, false, 0));
            }
        }

        return moves;
    }
    @Override
    public String getName() {
        return "p";
    }
}