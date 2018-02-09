package com.august.connectfour;

import java.util.ArrayList;

/**
 * Created by mrx on 2017-12-04.
 */

public class Analyzer {

    private GameEngine engine;

    public Analyzer(GameEngine engine) {
        this.engine = engine;
    }

    //Checking for victory or draw
    public boolean won(int player) {
        return wonHorizontal(player) || wonVertical(player) || wonDiagonalDown(player) || wonDiagonalUp(player);
    }
    public boolean wonHorizontal(int player) {
        for (int i = 0; i < engine.getRows(); i++) {
            int connected = 0;
            for (int j = 0; j < engine.getColumns(); j++) {
                if (engine.getBoard()[i][j] == player) {
                    connected++;
                    if (connected == 4)
                        return true;
                } else
                    connected = 0;
            }
        }
        return false;
    }
    public boolean wonVertical(int player) {
        for (int i = 0; i < engine.getColumns(); i++) {
            int connected = 0;
            for (int j = 0; j < engine.getRows(); j++) {
                if (engine.getBoard()[j][i] == player) {
                    connected++;
                    if (connected == 4)
                        return true;
                } else
                    connected = 0;
            }
        }
        return false;
    }
    public boolean wonDiagonalDown(int player) {
        for (int i = 0; i < engine.getRows(); i++) {
            for (int j = 0; j < engine.getColumns(); j++) {
                int k = 0;
                int connected = 0;
                while (i + k < engine.getRows() && j + k < engine.getColumns()) {
                    if (engine.getBoard()[i + k][j + k] == player) {
                        connected++;
                        if (connected == 4)
                            return true;
                    } else
                        connected = 0;
                    k++;
                }
            }
        }
        return false;
    }
    public boolean wonDiagonalUp(int player) {
        for (int i = engine.getRows() - 1; i > -1; i--) {
            for (int j = 0; j < engine.getColumns(); j++) {
                int k = 0;
                int connected = 0;
                while (i - k > -1 && j + k < engine.getColumns()) {
                    if (engine.getBoard()[i - k][j + k] == player) {
                        connected++;
                        if (connected == 4)
                            return true;
                    } else
                        connected = 0;
                    k++;
                }
            }
        }
        return false;
    }
    public boolean isDraw() {
        for (int i = 0; i < engine.getColumns(); i++) {
            if (engine.getFree(i) > -1)
                return false;
        }
        return true;
    }

    //Best chances in arraylists
    //nedvärdera vertical mindre än 3?
    public int[] getHorizontalChance(int player, int column) {
        int[] chance = new int[2];
        chance[0] = column;
        if (!canWinHorizontal(player, column))
            return chance;
        for (int i = engine.getFree(column) + 1; i < engine.getRows(); i++) {
            if (engine.getBoard()[i][column] == player)
                chance[1]++;
            else
                break;
        }
        return chance;
    }
    public int[] getVerticalChance(int player, int column) {
        int[] chance = new int[2];
        chance[0] = column;
        if (!canWinVertical(player, column))
            return chance;
        int row = engine.getFree(column);
        if (column > 0) {
            for (int i = column - 1; i > -1; i--) {
                if (engine.getBoard()[row][i] == player)
                    chance[1]++;
                else
                    break;
            }
        }
        if (column < engine.getColumns() - 1) {
            for (int j = column + 1; j < engine.getColumns(); j++) {
                if (engine.getBoard()[row][j] == player)
                    chance[1]++;
                else
                    break;
            }
        }
        return chance;
    }
    public int[] getDiagonalDownChance(int player, int column) {
        int[] chance = new int[2];
        chance[0] = column;
        if (!canWinDiagonalDown(player, column))
            return chance;
        int row = engine.getFree(column);
        int i = 1;
        if (column>0) {
            while (row-i>-1 && column-i>-1) {
                if (engine.getBoard()[row-i][column-i]==player)
                    chance[1]++;
                else
                    break;
                i++;
            }
        }
        i = 1;
        if (column<engine.getColumns()-1) {
            while (row+i<engine.getRows() && column+i<engine.getColumns()) {
                if (engine.getBoard()[row+i][column+i]==player)
                    chance[1]++;
                else
                    break;
                i++;
            }
        }
        return chance;
    }
    public int[] getDiagonalUpChance(int player, int column) {
        int[] chance = new int[2];
        chance[0] = column;
        if (!canWinDiagonalUp(player, column))
            return chance;
        int row = engine.getFree(column);
        int i = 1;
        if (column>0) {
            while (row+i<engine.getRows() && column-i>-1) {
                if (engine.getBoard()[row+i][column-i]==player)
                    chance[1]++;
                else
                    break;
                i++;
            }
        }
        i = 1;
        if (column<engine.getColumns()-1) {
            while (row-i>-1 && column+i<engine.getColumns()) {
                if (engine.getBoard()[row-i][column+i]==player)
                    chance[1]++;
                else
                    break;
                i++;
            }
        }
        return chance;
    }
    public int[] getChance(int player, int column) {
        int[] chance = new int[2];
        chance[0] = column;
        chance[1] = Math.max(getHorizontalChance(player, column)[1],
                    Math.max(getVerticalChance(player, column)[1],
                            Math.max(getDiagonalDownChance(player, column)[1],
                                    getDiagonalUpChance(player, column)[1])));
        return chance;
    }
    public ArrayList<int[]> getChances(int player) {
        ArrayList<int[]> chances = new ArrayList<>();

        for (int i = 0; i < engine.getColumns(); i++) {
            if (engine.getFree(i)>-1)
              chances.add(getChance(player, i));
            else {
                int[] dead = {i,0};
                chances.add(dead);
            }
        }
        return chances;
    }
    public int[] bestArrayChance(int player) {
        ArrayList<int[]> chances = getChances(player);
        if (chances.size()==0) {
            int[] lastChance = {engine.getAnyFreeColumn(), player};
            return lastChance;
        }
        if (chances.size()==1)
            return chances.get(0);

        int best = 0;
        for (int i=1;i<chances.size();i++) {
            if (createsWinningMove(player, chances.get(i)[0]))
                continue;
            if (chances.get(best)[1]<chances.get(i)[1])
                best = i;
            else if (chances.get(best)[1]==chances.get(i)[1]) {
                if (Math.abs(chances.get(best)[0]-3) > Math.abs(chances.get(i)[0]-3))
                    best = i;
                if (openChance(player, chances.get(i)[0]))
                    best = i;
            }
        }
        return chances.get(best);
    }

    //Checking for best chances (0=column, 1=length)
    public int[] bestHorizontal(int player) {
        int[] best = new int[2];
        for (int i = 0; i < engine.getColumns(); i++) {
            if (!canWinHorizontal(player, i))
                continue;
            else {
                int connected = 0;
                if (engine.getFree(i) > -1 && engine.getFree(i) < engine.getRows()) {
                    if (i > 0) {
                        for (int j = i - 1; j > -1; j--) {
                            if (engine.getBoard()[engine.getFree(i)][j] == player)
                                connected++;
                            else
                                break;
                        }
                    }
                    if (i < engine.getColumns() - 1) {
                        for (int k = i + 1; k < engine.getColumns(); k++) {
                            if (engine.getBoard()[engine.getFree(i)][k] == player)
                                connected++;
                            else
                                break;
                        }
                    }
                    if (connected > best[1] || (connected == best[1]) && Math.abs(i - 3) < Math.abs(best[0] - 3)) {
                        best[0] = i;
                        best[1] = connected;
                    }
                }
            }
        }
        return best;
    }
    public int[] bestVertical(int player) {
        int[] best = new int[2];
        for (int i = 0; i < engine.getColumns(); i++) {
            if (!canWinVertical(player, i))
                continue;
            else {
                int connected = 0;
                if (engine.getFree(i) > -1 && engine.getFree(i) < engine.getRows() - 1 && engine.getBoard()[engine.getFree(i) + 1][i] == player) {
                    for (int j = engine.getFree(i) + 1; j < engine.getRows(); j++) {
                        if (engine.getBoard()[j][i] == player)
                            connected++;
                        else
                            break;
                    }
                    if (connected > best[1] || (connected == best[1]) && Math.abs(i - 3) < Math.abs(best[0] - 3)) {
                        best[0] = i;
                        best[1] = connected;
                    }
                }
            }
        }
        return best;
    }
    public int[] bestDiagonalDown(int player) {
        int[] best = new int[2];
        for (int i = 0; i < engine.getColumns(); i++) {
            if (!canWinDiagonalDown(player, i))
                continue;
            else {
                int connected = 0;
                if (engine.getFree(i) > -1 && engine.getFree(i) < engine.getRows()) {
                    int j = 1;
                    while (i - j > -1 && engine.getFree(i) - j > -1) {
                        if (engine.getBoard()[engine.getFree(i) - j][i - j] == player)
                            connected++;
                        else
                            break;
                        j++;
                    }
                    j = 1;
                    while (i + j < engine.getColumns() && engine.getFree(i) + j < engine.getRows()) {
                        if (engine.getBoard()[engine.getFree(i) + j][i + j] == player)
                            connected++;
                        else
                            break;
                        j++;
                    }
                }
                if (connected > best[1] || (connected == best[1]) && Math.abs(i - 3) < Math.abs(best[0] - 3)) {
                    best[0] = i;
                    best[1] = connected;
                }
            }
        }
        return best;
    }
    public int[] bestDiagonalUp(int player) {
        int[] best = new int[2];
        for (int i = 0; i < engine.getColumns(); i++) {
            if (!canWinDiagonalUp(player, i))
                continue;
            else {
                int connected = 0;
                if (engine.getFree(i) > -1 && engine.getFree(i) < engine.getRows()) {
                    int j = 1;
                    while (i - j > -1 && engine.getFree(i) + j < engine.getRows()) {
                        if (engine.getBoard()[engine.getFree(i) + j][i - j] == player)
                            connected++;
                        else
                            break;
                        j++;
                    }
                    j = 1;
                    while (i + j < engine.getColumns() && engine.getFree(i) - j > -1) {
                        if (engine.getBoard()[engine.getFree(i) - j][i + j] == player)
                            connected++;
                        else
                            break;
                        j++;
                    }
                }
                if (connected > best[1] || (connected == best[1]) && Math.abs(i - 3) < Math.abs(best[0] - 3)) {
                    best[0] = i;
                    best[1] = connected;
                }
            }
        }
        return best;
    }
    //Hur rangordna alternativ?
    public int[] bestChance(int player) {
        int best = Math.max(bestHorizontal(player)[1], Math.max(bestVertical(player)[1],
                Math.max(bestDiagonalDown(player)[1], bestDiagonalUp(player)[1])));

        if (best == bestVertical(player)[1])
            return bestVertical(player);
        else if (best == bestDiagonalUp(player)[1])
            return bestDiagonalUp(player);
        else if (best == bestDiagonalDown(player)[1])
            return bestDiagonalDown(player);
        else
            return bestHorizontal(player);
    }

    //Open chances (2 in length)
    public boolean openHorizontal(int player, int column) {
        int connected = 0;
        int free = 0;
        int row = engine.getFree(column);
        if (row<0)
            return false;
        if (column > 0) {
            for (int i=column-1;i>-1;i--) {
                if (engine.getBoard()[row][i]==player)
                    connected++;
                else if (engine.getFree(i)==row) {
                    free++;
                    break;
                } else
                    break;
            }
        }
        if (column<engine.getColumns()-1) {
            for (int j=column+1;j<engine.getColumns();j++) {
                if (engine.getBoard()[row][j]==player)
                    connected++;
                else if (engine.getFree(j)==row) {
                    free++;
                    break;
                } else
                    break;
            }
        }
        return connected==2 && free==2;
    }
    public boolean openDiagonalDown(int player, int column) {
        int connected= 0;
        int free = 0;
        int row = engine.getFree(column);
        if (row<0)
            return false;
        int i = 1;
        if (column > 0) {
            while (row-i>-1 && column-i>engine.getColumns()) {
                if (engine.getBoard()[row-i][column-i]==player)
                    connected++;
                else if (engine.getFree(column-i)==row-i) {
                    free++;
                    break;
                } else
                    break;
                i++;
            }
        }
        i = 1;
        if (column<engine.getColumns()-1) {
            while (row+i<engine.getRows() && column+i<engine.getColumns()) {
                if (engine.getBoard()[row+i][column+i]==player)
                    connected++;
                else if (engine.getFree(column+i)==row+i) {
                    free++;
                    break;
                } else
                    break;
                i++;
            }
         }
        return connected==2 && free==2;
    }
    public boolean openDiagonalUp(int player, int column) {
        int connected= 0;
        int free = 0;
        int row = engine.getFree(column);
        if (row<0)
            return false;
        int i = 1;
        if (column > 0) {
            while (row+i<engine.getRows() && column-i>engine.getColumns()) {
                if (engine.getBoard()[row+i][column-i]==player)
                    connected++;
                else if (engine.getFree(column-i)==row+i) {
                    free++;
                    break;
                } else
                    break;
                i++;
            }
        }
        i = 1;
        if (column<engine.getColumns()-1) {
            while (row-i>-1 && column+i<engine.getColumns()) {
                if (engine.getBoard()[row-i][column+i]==player)
                    connected++;
                else if (engine.getFree(column+i)==row-i) {
                    free++;
                    break;
                } else
                    break;
                i++;
            }
        }
        return connected==2 && free==2;
    }
    public boolean openChance(int player, int column) {
        return openHorizontal(player, column) || openDiagonalDown(player, column) || openDiagonalUp(player, column);
    }

    //Creates winning move
    public boolean createsWinningMove(int player, int column) {
        int opId = player == 1 ? 2 : 1;
        GameEngine tempEngine = engine.getCopy(engine);
        Analyzer tempAnalyser = new Analyzer(tempEngine);

        ArrayList<int[]> oldChances = tempAnalyser.getChances(opId);

        if (engine.getFree(column) > -1)
            tempEngine.addCoin(player, column);
        else
            return false;

        ArrayList<int[]> newChances = tempAnalyser.getChances(opId);

        for (int i=0;i<oldChances.size();i++) {
            if (oldChances.get(i)[1]<newChances.get(i)[1] && newChances.get(i)[1]>2)
                return true;
        }
        return false;
    }

    //Can win from position
    public boolean canWinHorizontal(int player, int column) {
        int row = engine.getFree(column);
        if (row < 0)
            return false;
        int oppId = player == 1 ? 2 : 1;
        int open = 0;
        if (column > 0) {
            for (int i = column - 1; i > -1; i--) {
                if (engine.getBoard()[row][i] != oppId)
                    open++;
                else
                    break;
            }
        }
        if (column < engine.getColumns() - 1) {
            for (int j = column + 1; j < engine.getColumns(); j++) {
                if (engine.getBoard()[row][j] != oppId)
                    open++;
                else
                    break;
            }
        }
        return open >= 3;
    }
    public boolean canWinVertical(int player, int column) {
        if (engine.getFree(column) > 2)
            return true;
        else {
            for (int i = engine.getFree(column) + 1; i < 4 - engine.getFree(column); i++) {
                if (engine.getBoard()[i][column] != player)
                    return false;
            }
        }
        return true;
    }
    public boolean canWinDiagonalDown(int player, int column) {
        int row = engine.getFree(column);
        int oppId = player == 1 ? 2 : 1;
        int open = 0;
        int i = 1;
        if (column > 0) {
            while (row - i > -1 && column - i > -1) {
                if (engine.getBoard()[row - i][column - i] != oppId)
                    open++;
                else
                    break;
                i++;
            }
        }
        i = 1;
        if (column < engine.getColumns() - 1) {
            while (row + i < engine.getRows() && column + i < engine.getColumns()) {
                if (engine.getBoard()[row + i][column + i] != oppId)
                    open++;
                else
                    break;
                i++;
            }
        }
        return open >= 3;
    }
    public boolean canWinDiagonalUp(int player, int column) {
        int row = engine.getFree(column);
        int oppId = player == 1 ? 2 : 1;
        int open = 0;
        int i = 1;
        if (column > 0) {
            while (row + i < engine.getRows() && column - i > -1) {
                if (engine.getBoard()[row + i][column - i] != oppId)
                    open++;
                else
                    break;
                i++;
            }
        }
        i = 1;
        if (column < engine.getColumns() - 1) {
            while (row - i > -1 && column + i < engine.getColumns()) {
                if (engine.getBoard()[row - i][column + i] != oppId)
                    open++;
                else
                    break;
                i++;
            }
        }
        return open >= 3;
    }

    //Get winning holes
    //0 = row, 1 = column where startpoint is
    public int[] getWinningHoles() {
        if (getWinningHorizontal(1) != null)
            return getWinningHorizontal(1);
        if (getWinningHorizontal(2) != null)
            return getWinningHorizontal(2);
        if (getWinningVertical(1) != null)
            return getWinningVertical(1);
        if (getWinningVertical(2) != null)
            return getWinningVertical(2);
        if (getWinningDiagonalDown(1) != null)
            return getWinningDiagonalDown(1);
        if (getWinningDiagonalDown(2) != null)
            return getWinningDiagonalDown(2);
        if (getWinningDiagonalUp(1) != null)
            return getWinningDiagonalUp(1);
        if (getWinningDiagonalUp(2) != null)
            return getWinningDiagonalUp(2);
        else
            return null;
    }
    private int[] getWinningHorizontal(int player) {
        int[] result = new int[2];
        for (int i = 0; i < engine.getRows(); i++) {
            int connected = 0;
            for (int j = 0; j < engine.getColumns(); j++) {
                if (engine.getBoard()[i][j] == player) {
                    connected++;
                    if (connected == 4) {
                        result[0] = i;
                        result[1] = j - 3;
                        return result;
                    }
                } else
                    connected = 0;
            }
        }
        return null;
    }
    private int[] getWinningVertical(int player) {
        int[] result = new int[2];
        for (int i = 0; i < engine.getColumns(); i++) {
            int connected = 0;
            for (int j = 0; j < engine.getRows(); j++) {
                if (engine.getBoard()[j][i] == player) {
                    connected++;
                    if (connected == 4) {
                        result[0] = j;
                        result[1] = i;
                        return result;
                    }
                } else
                    connected = 0;
            }
        }
        return null;
    }
    private int[] getWinningDiagonalDown(int player) {
        int result[] = new int[2];
        for (int i = 0; i < engine.getRows(); i++) {
            for (int j = 0; j < engine.getColumns(); j++) {
                int k = 0;
                int connected = 0;
                while (i + k < engine.getRows() && j + k < engine.getColumns()) {
                    if (engine.getBoard()[i + k][j + k] == player) {
                        connected++;
                        if (connected == 4) {
                            result[0] = i + k - 3;
                            result[1] = j + k - 3;
                            return result;
                        }
                    } else
                        connected = 0;
                    k++;
                }
            }
        }
        return null;
    }
    private int[] getWinningDiagonalUp(int player) {
        int[] result = new int[2];
        for (int i = engine.getRows() - 1; i > -1; i--) {
            for (int j = 0; j < engine.getColumns(); j++) {
                int k = 0;
                int connected = 0;
                while (i - k > -1 && j + k < engine.getColumns()) {
                    if (engine.getBoard()[i - k][j + k] == player) {
                        connected++;
                        if (connected == 4) {
                            result[0] = i - k + 3;
                            result[1] = j + k - 3;
                            return result;
                        }
                    } else
                        connected = 0;
                    k++;
                }
            }
        }
        return null;
    }

    //Get winning mode in camelCase
    private String getWinningMode(int player) {
        if (wonVertical(player))
            return "vertical";
        if (wonHorizontal(player))
            return "horizontal";
        if (wonDiagonalDown(player))
            return "diagonalDown";
        if (wonDiagonalUp(player))
            return "diagonalUp";
        else
            return null;
    }
    public String getWinningMode() {
        if (getWinningMode(1) != null)
            return getWinningMode(1);
        if (getWinningMode(2) != null)
            return getWinningMode(2);
        else
            return null;
    }

    //MinMax
    public int getLead(int player) {
        return 0;
    }
    public int tempAdd(int player, int column) {
        int opId = player == 1 ? 2 : 1;
        GameEngine tempEngine = engine.getCopy(engine);
        Analyzer tempAnalyser = new Analyzer(tempEngine);

        if (tempEngine.getFree(column)==-1)
            return -1;
        else
            tempEngine.addCoin(player, column);

        return tempAnalyser.getLead(player);
    }
    public int lookOneStep(int player) {
        int maxLead = 0;
        int bestColumn = 0;
        for (int i=0;i<engine.getColumns();i++) {
            if (tempAdd(player, i)>maxLead) {
                maxLead = tempAdd(player, i);
                bestColumn = i;
            }
        }
        return bestColumn;
    }
    public int lookManySteps(int player, int steps) {



        return 0;
    }


}
