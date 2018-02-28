package com.mandelag.minesweeper;

import com.mandelag.minesweeper.impl.DefaultMinesweeperOpenAlgorithm;
import com.mandelag.minesweeper.impl.RandomBombsPlacer;
import com.mandelag.minesweeper.services.MineBoardGameService;

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
    private MinesweeperOpenAlgorithm moa;

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
        int[][] gridWithBombs = placeBombs(gridTemplate, this.bombs);
        this.grid = new ImmutableGrid(gridWithBombs);
        this.moa = new DefaultMinesweeperOpenAlgorithm();
        this.moa.setGrid(this.grid);
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
    
    public int[][] open(int x, int y) {
        return moa.open(x, y);
    }
    
    private int[][] placeBombs(int[][] grid, int nBombs) {
        return new RandomBombsPlacer().placeBombs(grid, nBombs);
    }
}
