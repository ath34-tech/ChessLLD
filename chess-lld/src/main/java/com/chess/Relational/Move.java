package com.chess.Relational;

import com.chess.GameCoreEntity.Piece;
import com.chess.Helper.Cell;
import com.chess.Types.MoveType;

public final class Move {
    private final Cell from;
    private final Cell to;
    private final Piece movedPiece;

    private Piece capturedPiece;
    private final MoveType type;
    private Piece promotionPiece;

    private final boolean firstMove;

    private boolean causeCheck;
    private boolean causeCheckmate;

    private final int moveNumber;

    // ---------------- Constructor ----------------
    public Move(Cell from, Cell to, Piece movedPiece, MoveType type,
                boolean firstMove, int moveNumber) {

        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.type = type;
        this.firstMove = firstMove;
        this.moveNumber = moveNumber;
    }

    // ---------------- Getters ----------------
    public Cell getFrom() { return from; }

    public Cell getTo() { return to; }

    public Piece getMovedPiece() { return movedPiece; }

    public Piece getCapturedPiece() { return capturedPiece; }

    public MoveType getType() { return type; }

    public Piece getPromotionPiece() { return promotionPiece; }

    public boolean isFirstMove() { return firstMove; }

    public boolean isCauseCheck() { return causeCheck; }

    public boolean isCauseCheckmate() { return causeCheckmate; }

    public int getMoveNumber() { return moveNumber; }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public void setPromotionPiece(Piece promotionPiece) {
        this.promotionPiece = promotionPiece;
    }

    public void setCauseCheck(boolean causeCheck) {
        this.causeCheck = causeCheck;
    }

    public void setCauseCheckmate(boolean causeCheckmate) {
        this.causeCheckmate = causeCheckmate;
    }
}
