package com.chess.GameCoreEntity;

import com.chess.Helper.Cell;
import com.chess.Relational.Move;
import com.chess.Types.Color;

import java.util.ArrayList;
import java.util.List;

public class MoveValidator {

    public static boolean isKingInCheck(Board board, Color color){

        Cell kingCell = board.findKing(color);
        if(kingCell == null) return false;

        for(int r=0; r<8; r++){
            for(int c=0; c<8; c++){

                Cell cell = board.getCell(r,c);
                if(cell.isEmpty())
                    continue;

                Piece piece = cell.getPiece();
                if(piece.getColor() == color)
                    continue;
                List<Move> moves = piece.getLegalMoves(board);
                for(Move m : moves){
                    if(m.getTo() == kingCell){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static List<Move> getLegalMoves(Board board, Piece piece){

        List<Move> legalMoves = new ArrayList<>();
        List<Move> pseudoMoves = piece.getLegalMoves(board);

        for(Move move : pseudoMoves){

            Cell from = move.getFrom();
            Cell to = move.getTo();

            Piece captured = to.getPiece();

            from.setPiece(null);
            to.setPiece(piece);
            piece.setCurrentCell(to);

            boolean kingInCheck = isKingInCheck(board, piece.getColor());

            to.setPiece(captured);
            from.setPiece(piece);
            piece.setCurrentCell(from);

            if(!kingInCheck){
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }
    public static boolean hasAnyLegalMove(Board board, Color color){

        for(int r=0; r<8; r++){
            for(int c=0; c<8; c++){

                Cell cell = board.getCell(r,c);
                if(cell.isEmpty())
                    continue;

                Piece piece = cell.getPiece();

                if(piece.getColor()!=color)
                    continue;

                List<Move> legalMoves = getLegalMoves(board,piece);

                if(!legalMoves.isEmpty())
                    return true;
            }
        }
        return false;
    }
}
