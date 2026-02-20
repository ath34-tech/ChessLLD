package com.chess;

import com.chess.GameCoreEntity.Game;
import com.chess.GameCoreEntity.Player;
import com.chess.Helper.ChessParser;
import com.chess.Types.Color;

import java.util.Scanner;

public class App {
  public static void main(String[] args) {
    Player white = new Player("White", Color.WHITE);
    Player black = new Player("Black", Color.BLACK);

    Game game = new Game(white, black);

    Scanner sc = new Scanner(System.in);

    while(true){

      game.getBoard().printBoard();
      System.out.println(game.getCurrentTurn()+" to move");

      System.out.print("Enter move (e2 e4): ");
      String from = sc.next();
      String to   = sc.next();

      int fr = ChessParser.row(from.charAt(1));
      int fc = ChessParser.col(from.charAt(0));

      int tr = ChessParser.row(to.charAt(1));
      int tc = ChessParser.col(to.charAt(0));

      boolean success = game.playMove(fr,fc,tr,tc);

      if(!success){
        System.out.println("Try again!");
        continue;
      }

      String result = game.getGameResult();
      if(!result.equals("ONGOING")){
        game.getBoard().printBoard();
        System.out.println(result);
        break;
      }
    }
  }
}
