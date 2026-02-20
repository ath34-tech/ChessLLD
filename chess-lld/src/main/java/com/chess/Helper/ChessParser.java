package com.chess.Helper;

public class ChessParser {

    public static int col(char file){
        return file - 'a';
    }

    public static int row(char rank){
        return 8 - (rank - '0');
    }
}