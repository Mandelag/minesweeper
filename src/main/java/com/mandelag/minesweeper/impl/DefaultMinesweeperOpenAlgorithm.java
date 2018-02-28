/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.minesweeper.impl;

import com.mandelag.minesweeper.ImmutableGrid;
import static com.mandelag.minesweeper.MineBoard.VISITED;
import com.mandelag.minesweeper.MinesweeperOpenAlgorithm;

/**
 *
 * @author Keenan
 */
public class DefaultMinesweeperOpenAlgorithm implements MinesweeperOpenAlgorithm {

    ImmutableGrid grid;
    
    @Override
    public void setGrid(ImmutableGrid ig) {
        this.grid = ig;
    }

    @Override
    /**
     * Method that are called when the a tile is clicked. It will reveal the
     * tile number in the resulting array. If grid[y,x] >= 1 && grid[y,x] <= 8
     * then only that particular tile is revealed. If grid[y,x] == 0 then it
     * will search recursively for neighboring zeros and reveal the adjancent
     * numbers. If grid[y,x] < 0 then a mine is clicked and counted as game over
     * on the server side.
     *
     * @param x the x location of the requested cell
     * @param y the y location of the requested cell
     * @return an array containing opened cells
     */
    public int[][] open(int x, int y) {
        int height =  grid.getHeight();
        int width = grid.getWidth();
        if (grid.get(x, y) > 0 || grid.get(x,y)<-90) {
            int[][] res = new int[height][width];
            res[y][x] = grid.get(x, y);
            return res;
        } else if (grid.get(x, y) < 0 && grid.get(x, y) >= -90) {
            int[][] res = new int[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    res[i][j] = grid.get(j, i);
                }
            }
            return res;
        }
        int[][] result = open(new int[height][width], x, y);
        int[][] openedGrid = new int[height][width];
        // register the opened grid in the openedGrid array

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                //openedGrid[i][j] = result[i][j] == -99 ? grid[i][j] : openedGrid[i][j];
                switch (result[i][j]) {
                    case 9:
                        openedGrid[i][j] = grid.get(j, i);
                        break;
                    case -99:
                        openedGrid[i][j] = 0;
                        break;
                    default:
                        openedGrid[i][j] = 0;
                }
            }
        }
        return result;
    }

    /**
     * Method that is called when the a tile is clicked. It will reveal the
     * tile number in the resulting array. If grid[y,x] >= 1 && grid[y,x] <= 8
     * then only that particular tile is revealed. If grid[y,x] == 0 then it
     * will search recursively for neighboring zeros and reveal the adjancent
     * numbers. If grid[y,x] < 0 then a mine is clicked and counted as game over
     * in the server side.
     *
     * @param result the container for the resulting array (used for searching
     * recursively)
     * @param x the x location of the requested cell
     * @param y the y location of the requested cell
     * @return an array containing opened cells
     */
    private int[][] open(int[][] result, int x, int y) {
        if (grid.get(x, y) == 0) {
            result[y][x] = VISITED;
            try {
                result[y + 1][x + 1] = (result[y + 1][x + 1] == 0 && grid.get(x + 1, y + 1) == 0) ? 0 : (result[y + 1][x + 1] == VISITED ? VISITED : grid.get(x + 1, y + 1));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                result[y + 1][x - 1] = (result[y + 1][x - 1] == 0 && grid.get(x - 1, y + 1) == 0) ? 0 : (result[y + 1][x - 1] == VISITED ? VISITED : grid.get(x - 1, y + 1));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                result[y - 1][x + 1] = (result[y - 1][x + 1] == 0 && grid.get(x + 1, y - 1) == 0) ? 0 : (result[y - 1][x + 1] == VISITED ? VISITED : grid.get(x + 1, y - 1));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                result[y - 1][x - 1] = (result[y - 1][x - 1] == 0 && grid.get(x - 1, y - 1) == 0) ? 0 : (result[y - 1][x - 1] == VISITED ? VISITED : grid.get(x - 1, y - 1));
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            try {
                result[y - 1][x] = (result[y - 1][x] == 0 && grid.get(x, y - 1) == 0) ? 0 : (result[y - 1][x] == VISITED ? VISITED : grid.get(x, y - 1));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                result[y][x - 1] = (result[y][x - 1] == 0 && grid.get(x - 1, y) == 0) ? 0 : (result[y][x - 1] == VISITED ? VISITED : grid.get(x - 1, y));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                result[y][x + 1] = (result[y][x + 1] == 0 && grid.get(x + 1, y) == 0) ? 0 : (result[y][x + 1] == VISITED ? VISITED : grid.get(x + 1, y));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                result[y + 1][x] = (result[y + 1][x] == 0 && grid.get(x, y + 1) == 0) ? 0 : (result[y + 1][x] == VISITED ? VISITED : grid.get(x, y + 1));
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            try {
                if (grid.get(x, y - 1) == 0 && result[y - 1][x] != VISITED) {
                    open(result, x, y - 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid.get(x + 1, y) == 0 && result[y][x + 1] != VISITED) {
                    open(result, x + 1, y);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid.get(x - 1, y) == 0 && result[y][x - 1] != VISITED) {
                    open(result, x - 1, y);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid.get(x, y + 1) == 0 && result[y + 1][x] != VISITED) {
                    open(result, x, y + 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid.get(x - 1, y - 1) == 0 && result[y - 1][x - 1] != VISITED) {
                    open(result, x - 1, y - 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid.get(x - 1, y + 1) == 0 && result[y + 1][x - 1] != VISITED) {
                    open(result, x - 1, y + 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid.get(x + 1, y - 1) == 0 && result[y - 1][x + 1] != VISITED) {
                    open(result, x + 1, y - 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid.get(x + 1, y + 1) == 0 && result[y + 1][x + 1] != VISITED) {
                    open(result, x + 1, y + 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            
        } else {
            result[y][x] = grid.get(x, y);
        }

        return result;
    }
    
}
