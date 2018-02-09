package com.august.connectfour;

/**
 * Created by mrx on 2017-12-04.
 */

public class GameEngine {

    private int[][] board;
    private int rows, columns;

    public GameEngine(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        board = new int[rows][columns];
    }

    public GameEngine getCopy(GameEngine engine) {
        GameEngine copy = new GameEngine(rows, columns);
        for (int i=0;i<engine.getRows();i++) {
            for (int j=0;j<engine.getColumns();j++)
                copy.board[i][j] = engine.getBoard()[i][j];
        }
        return copy;
    }

    public void addCoin(int player, int column) {
        int row = getFree(column);
        if (row>-1)
            board[row][column] = player;
    }

    public void deleteCoin(int column) {
        board[getFree(column)+1][column] = 0;
    }

    //0 = draw, 1 och 2 = players?
    //Flytta till analyzer?
    public int getWinner() {
        return 0;
    }

    public int getFree(int column) {
        for (int i=0;i<rows;i++) {
            if (board[i][column]!=0)
                return i-1;
        }
        return rows-1;
    }

    public int getAnyFreeColumn() {
        for (int i=0;i<columns;i++) {
            if (getFree(i)>-1)
                return i;
        }
        return 3;
    }

    public int[][] getBoard() {
        return board;
    }
    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
}
