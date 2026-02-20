package com.chess.Helper;

public final class Position {
    private final int row;
    private final int column;

    public Position(int row,int column){
        if(isValid(row,column)){
            this.row=row;
            this.column=column;
        }
        else{
            throw new IllegalArgumentException("Invalid position");
        }
    }
    private final int MAX_LIMIT=7;
    private final int MIN_LIMIT=0;
    public boolean isValid(int row,int column){
        if((row<=MAX_LIMIT && row>=MIN_LIMIT) && (column<=MAX_LIMIT && column>=MIN_LIMIT)){
            return true;
        }
        return false;
    }
    public int getRow(){
        return this.row;
    }
    public int getColumn(){
        return this.column;
    }
}
