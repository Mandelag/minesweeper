/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.minesweeper;

/**
 *
 * @author Keenan Gebze
 */
public interface MinesweeperOpenAlgorithm {
    void setGrid(ImmutableGrid ig);
    int[][] open(int x, int y);
}
