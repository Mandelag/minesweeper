/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.minesweeper;


/**
 * A wrapper class for two dimensional array.
 * Applying Immutable Object design pattern.
 * 
 * @author Keenan
 */
public class ImmutableGrid {
    private final byte[][] grid;
    private final int width;
    private final int height;
    
    public ImmutableGrid(int height, int width){
        this(new byte[height][width]);
    }
    
    public ImmutableGrid(byte[][] grid){
        this.grid = grid;
        this.height = grid.length;
        this.width = grid[0].length;
    }
    
    public ImmutableGrid(int[][] grid){
        byte[][] byteGrid = new byte[grid.length][grid[0].length];
        for(int y=0; y<byteGrid.length; y++){
            for(int x=0;x<byteGrid[y].length;x++){
                byteGrid[y][x] = (byte) grid[y][x];
            }
        }
        this.grid = byteGrid;
        this.height = grid.length;
        this.width = grid[0].length;
    }
    
    /**
     * Wrapper method to access the grid value.
     * 
     * @param x
     * @param y
     * @return the integer value of the grid located at grid[y][x].
     */
    public int get(int x, int y){
        return grid[y][x];
    }
    
    public static void main(String[] args){
        int[][] intGrid = {{3,9,49},
            {1,2,10},
            {100,12,30}};
        ImmutableGrid ig = new ImmutableGrid(intGrid);
        System.out.println(ig.get(1, 2));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    @Override
    public String toString(){
        String result = "";
        for (int h = 0; h <= this.getHeight(); h++) {
            for (int w = 0; w < this.getWidth(); w++) {
                result += this.get(h,w)+" ";
            }
            result += "\r\n";
        }
        return result;
    }
    
}
