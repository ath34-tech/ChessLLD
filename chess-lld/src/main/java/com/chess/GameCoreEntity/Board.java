package com.chess.GameCoreEntity;

import com.chess.GameCoreEntity.Pieces.*;
import com.chess.Helper.Cell;
import com.chess.Helper.Position;
import com.chess.Types.Color;

import java.util.ArrayList;
import java.util.List;

public final class Board {
    private static final int SIZE = 8;

    private final List<List<Cell>> grid;

    private final Player whitePlayer;
    private final Player blackPlayer;

    public Board(Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;

        grid = new ArrayList<>(SIZE);

        initializeBoard();
        initializePieces();
    }

    private void initializeBoard() {

        for (int r = 0; r < SIZE; r++) {

            List<Cell> row = new ArrayList<>(SIZE);

            for (int c = 0; c < SIZE; c++) {
                row.add(new Cell(new Position(r,c),this));
            }

            grid.add(row);
        }
    }

    private void initializePieces() {

        for(int c=0;c<8;c++){
            placePiece(new Pawn(Color.WHITE, whitePlayer, null),6,c);
            placePiece(new Pawn(Color.BLACK, blackPlayer, null),1,c);
        }

        placePiece(new Rook(Color.WHITE,whitePlayer,null),7,0);
        placePiece(new Rook(Color.WHITE,whitePlayer,null),7,7);
        placePiece(new Rook(Color.BLACK,blackPlayer,null),0,0);
        placePiece(new Rook(Color.BLACK,blackPlayer,null),0,7);

        placePiece(new Knight(Color.WHITE,whitePlayer,null),7,1);
        placePiece(new Knight(Color.WHITE,whitePlayer,null),7,6);
        placePiece(new Knight(Color.BLACK,blackPlayer,null),0,1);
        placePiece(new Knight(Color.BLACK,blackPlayer,null),0,6);

        placePiece(new Bishop(Color.WHITE,whitePlayer,null),7,2);
        placePiece(new Bishop(Color.WHITE,whitePlayer,null),7,5);
        placePiece(new Bishop(Color.BLACK,blackPlayer,null),0,2);
        placePiece(new Bishop(Color.BLACK,blackPlayer,null),0,5);

        placePiece(new Queen(Color.WHITE,whitePlayer,null),7,3);
        placePiece(new Queen(Color.BLACK,blackPlayer,null),0,3);

        placePiece(new King(Color.WHITE,whitePlayer,null),7,4);
        placePiece(new King(Color.BLACK,blackPlayer,null),0,4);
    }

    private void placePiece(Piece piece, int row, int col) {

        Cell cell = getCell(row, col);

        cell.setPiece(piece);
        piece.setCurrentCell(cell);
    }

    public Cell getCell(int row, int col) {
        return grid.get(row).get(col);
    }

    public Cell findKing(Color color){

        for(int r=0;r<8;r++){
            for(int c=0;c<8;c++){

                Cell cell = getCell(r,c);

                if(cell.isEmpty())
                    continue;

                Piece piece = cell.getPiece();

                if(piece instanceof King && piece.getColor()==color){
                    return cell;
                }
            }
        }
        return null;
    }

    public void printBoard(){

        for(int r=0;r<8;r++){
            for(int c=0;c<8;c++){

                Cell cell = getCell(r,c);

                if(cell.isEmpty())
                    System.out.print(". ");
                else
                    System.out.print(cell.getPiece().getName() + " ");
            }
            System.out.println();
        }
    }
}
