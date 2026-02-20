package com.chess.GameCoreEntity;

import com.chess.GameCoreEntity.Pieces.*;
import com.chess.Helper.Cell;
import com.chess.Relational.Move;
import com.chess.Types.Color;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Board board;
    private final Player whitePlayer;
    private final Player blackPlayer;

    private final List<Move> moveHistory;
    private Color currentTurn;

    public Game(Player white, Player black) {

        this.whitePlayer = white;
        this.blackPlayer = black;

        this.board = new Board(white, black);
        this.moveHistory = new ArrayList<>();

        this.currentTurn = Color.WHITE;
    }

    public Board getBoard() {
        return board;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    private Player getCurrentPlayer() {
        return currentTurn == Color.WHITE ? whitePlayer : blackPlayer;
    }

    private Player getOpponentPlayer() {
        return currentTurn == Color.WHITE ? blackPlayer : whitePlayer;
    }


    private void switchTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
    public boolean isCheckmate(Color color){

        if(!MoveValidator.isKingInCheck(board,color))
            return false;

        return !MoveValidator.hasAnyLegalMove(board,color);
    }
    public boolean isStalemate(Color color){

        if(MoveValidator.isKingInCheck(board,color))
            return false;

        return !MoveValidator.hasAnyLegalMove(board,color);
    }
    public boolean isInsufficientMaterial(){

        int whitePieces=0;
        int blackPieces=0;

        for(int r=0;r<8;r++){
            for(int c=0;c<8;c++){

                Cell cell = board.getCell(r,c);
                if(cell.isEmpty()) continue;

                Piece p = cell.getPiece();

                // ignore kings
                if(p instanceof King)
                    continue;

                if(p instanceof Bishop || p instanceof Knight){
                    if(p.getColor()==Color.WHITE)
                        whitePieces++;
                    else
                        blackPieces++;
                }
                else{
                    return false;
                }
            }
        }

        return whitePieces<=1 && blackPieces<=1;
    }
    public boolean isFiftyMoveDraw(){

        int count = 0;

        for(int i=moveHistory.size()-1;i>=0 && count<100;i--){

            Move m = moveHistory.get(i);

            if(m.getCapturedPiece()!=null)
                return false;

            if(m.getMovedPiece() instanceof Pawn)
                return false;

            count++;
        }
        return count>=100;
    }

    public String getGameResult(){

        if(isCheckmate(currentTurn))
            return "CHECKMATE! " + (currentTurn==Color.WHITE?"Black":"White") + " wins";

        if(isStalemate(currentTurn))
            return "DRAW - Stalemate";

        if(isInsufficientMaterial())
            return "DRAW - Insufficient material";

        if(isFiftyMoveDraw())
            return "DRAW - 50 move rule";

        return "ONGOING";
    }
    public boolean playMove(int fromRow, int fromCol, int toRow, int toCol){

        Cell from = board.getCell(fromRow, fromCol);
        Cell to   = board.getCell(toRow, toCol);

        if(from.isEmpty()){
            System.out.println("No piece at source!");
            return false;
        }

        Piece piece = from.getPiece();

        if(piece.getColor()!=currentTurn){
            System.out.println("Wrong turn!");
            return false;
        }

        List<Move> legalMoves = MoveValidator.getLegalMoves(board,piece);

        Move selectedMove = null;

        for(Move m : legalMoves){
            if(m.getTo()==to){
                selectedMove = m;
                break;
            }
        }

        if(selectedMove==null){
            System.out.println("Illegal move!");
            return false;
        }

        if(!to.isEmpty()){
            getCurrentPlayer().addCapturedPiece(to.getPiece());
        }

        piece.move(selectedMove);
        piece = handlePromotion(piece);

        moveHistory.add(selectedMove);

        switchTurn();
        return true;
    }
    private Piece handlePromotion(Piece piece){

        if(!(piece instanceof Pawn))
            return piece;

        Cell cell = piece.getCell();
        int row = cell.getPosition().getRow();

        boolean promote =
                (piece.getColor()==Color.WHITE && row==0) ||
                        (piece.getColor()==Color.BLACK && row==7);

        if(!promote)
            return piece;

        System.out.println("Pawn Promotion!");
        System.out.print("Choose (Q,R,B,N): ");

        java.util.Scanner sc = new java.util.Scanner(System.in);
        String choice = sc.next().toUpperCase();

        Player owner = (piece.getColor()==Color.WHITE) ? whitePlayer : blackPlayer;

        Piece newPiece;

        switch(choice){
            case "R":
                newPiece = new Rook(piece.getColor(), owner, cell);
                break;
            case "B":
                newPiece = new Bishop(piece.getColor(), owner, cell);
                break;
            case "N":
                newPiece = new Knight(piece.getColor(), owner, cell);
                break;
            default:
                newPiece = new Queen(piece.getColor(), owner, cell);
                break;
        }

        cell.setPiece(newPiece);

        System.out.println("Promoted to " + newPiece.getClass().getSimpleName());

        return newPiece;
    }
}