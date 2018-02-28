/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.minesweeper.impl;

import com.mandelag.minesweeper.ImmutableGrid;
import com.mandelag.minesweeper.MinesweeperPlaceBombsAlgorithm;
import java.util.Random;

/**
 *
 * @author Keenan
 */
public class RandomBombPlacer implements MinesweeperPlaceBombsAlgorithm{

    private int x, y;
    private final Random r = new Random();
    private int breaker = 0;
    private final int MAX_LOOP = 512;
        
    @Override
    public int[][] placeBombs(int[][] grid, int bombs) {
        //place the bombs
        for (int b = bombs; b > 0 && breaker++ > MAX_LOOP; b--) {
            x = r.nextInt(grid.length);
            y = r.nextInt(grid[0].length);
            
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
