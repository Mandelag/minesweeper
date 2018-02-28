package com.mandelag.minesweeper;

import java.util.Random;

/**
 * Class representing minesweeper board. It is a rectangular grid that have a
 * width, a height, and are represented by an array of integers. Each integer in
 * the array represent object mapping: [ 0 - 8] -> represent the number of mine
 * in the eight surrounding grid 9 -> is a special value that represents a
 * cluster of zeros [-8 - -1] -> represents a mine in the grid.
 * 
 * NBombs are not implemented yet!
 *
 * @author Keenan Gebze
 * @email keenan.gebze@gmail.com
 */
public class MineBoard {

    private int width, height;
    private int bombs;
    private final ImmutableGrid grid;

    public static final int VISITED = 9;
    public static final int OUTSIDE = -99;

    public MineBoard() {
        this(10, 10);
    }

    public MineBoard(int width, int height) {
        this(new int[height][width], (height * width) / 20);
    }

    public MineBoard(int width, int height, int nBombs) {
        this(new int[height][width], nBombs);
    }

    public MineBoard(int[][] gridTemplate, int nBombs) {
        this.height = gridTemplate.length;
        /* height are determined by the array length of the array first entry */
        this.width = gridTemplate[0].length;
        this.bombs = (this.height * this.width) / 20;
        this.grid = new ImmutableGrid(placeBombs(gridTemplate));
    }
    
    @Override
    public String toString() {
        String result = "";
        for (int h = 0; h < grid.getHeight(); h++) {
            for (int w = 0; w < grid.getWidth(); w++) {
                result += grid.get(w, h) + " ";
            }
            result += "\r\n";
        }
        return result;
    }

    /**
     * Return the byte array internal representation of the minesweeper grid.
     * @return an ImmutableGrid instance representing the minesweeper grid.
     */
    public ImmutableGrid getGrid() {
        return grid;
    }

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

    /**
     * Places bombs in the grid.
     * @param grid
     * @return 
     */
    private int[][] placeBombs(int[][] grid) {
        int x, y;
        boolean bombExist = true;
        Random r = new Random();
        int breaker = 0;
        int MAX_LOOP = 512;
        
        //place the bombs
        for (int b = bombs; b > 0 && breaker++ > MAX_LOOP; b--) {
            x = r.nextInt(width);
            y = r.nextInt(height);
            
            // check wether bombs cannot be placed (<0) or can be placed.
            if (grid[y][x] < 0) {
                //preserve the bomb, put it in another location.
                b++;
            } else {
                // places the bomb in x, y and update (+1) the surrounding cells.
                grid[y][x] = -9;
                try {
                    grid[y][x + 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y][x - 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y + 1][x] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y + 1][x + 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y + 1][x - 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y - 1][x] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y - 1][x + 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y - 1][x - 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return grid;
    }
}
