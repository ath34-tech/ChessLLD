package com.chess.Helper;

import com.chess.GameCoreEntity.Board;
import com.chess.GameCoreEntity.Piece;
import com.chess.Relational.Move;
import com.chess.Types.MoveType;

import java.util.List;

public class MovementScanner {
    private MovementScanner() {}

    public static void scanDirection(Board board,
                                     Piece piece,
                                     List<Move> moves,
                                     int rowStep,
                                     int colStep) {

        Cell current = piece.getCell();

        int r = current.getPosition().getRow() + rowStep;
        int c = current.getPosition().getColumn() + colStep;

        while (r >= 0 && r < 8 && c >= 0 && c < 8) {

            Cell target = board.getCell(r, c);

            if (target.isEmpty()) {

                moves.add(new Move(
                        current,
                        target,
                        piece,
                        MoveType.NORMAL,
                        false,
                        0
                ));
            }
            else {
                if (target.getPiece().getColor() != piece.getColor()) {

                    moves.add(new Move(
                            current,
                            target,
                            piece,
                            MoveType.CAPTURE,
                            false,
                            0
                    ));
                }

                break;
            }

            r += rowStep;
            c += colStep;
        }
    }
}
