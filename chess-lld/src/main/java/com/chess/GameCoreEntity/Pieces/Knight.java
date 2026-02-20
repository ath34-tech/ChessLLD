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

public class Knight extends Piece {

    public Knight(Color color, Player owner, Cell cell) {
        super(color, owner, cell);
    }

    private static final int[][] OFFSETS = {
            {2,1},{2,-1},{-2,1},{-2,-1},
            {1,2},{1,-2},{-1,2},{-1,-2}
    };

    @Override
    public List<Move> getLegalMoves(Board board) {

        List<Move> moves = new ArrayList<>();
        Cell current = getCell();

        for(int[] o : OFFSETS){

            int r = current.getPosition().getRow() + o[0];
            int c = current.getPosition().getColumn() + o[1];

            if(r<0 || r>=8 || c<0 || c>=8)
                continue;

            Cell target = board.getCell(r,c);

            if(target.isEmpty()){
                moves.add(new Move(current,target,this, MoveType.NORMAL,false,0));
            }
            else if(target.getPiece().getColor()!=getColor()){
                moves.add(new Move(current,target,this, MoveType.CAPTURE,false,0));
            }
        }
        return moves;
    }
    @Override
    public String getName() {
        return "k";
    }
}