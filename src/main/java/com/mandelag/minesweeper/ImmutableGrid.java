package com.mandelag.minesweeper;

/**
 * Two dimensional immutable byte array.
 * 
 * @author Keenan Gebze
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
        
        for(int i=0;i<ig.getWidth();i++){
            for(int j=0;j<ig.getWidth();j++){
                System.out.print(ig.get(j,i)+" ");
            }
            System.out.println("");
        }
    }

    /**
     * Get the width of the array.
     * The width of this array 2nd dimension of the array.
     * @return the width of the two dimensional array.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the array.
     * The width of this array 1st dimension of the array.
     * @return the height of the two dimensional array.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Convert the internal byte array as an integer array.
     * @return the integer array.
     */
    public int[][] toIntegerArrays(){
        int[][] result = new int[getHeight()][getWidth()];
        for (int h = 0; h < this.getHeight(); h++) {
            for (int w = 0; w < this.getWidth(); w++) {
                result[h][w] = get(w,h);
            }
        }
        return result;
    }
    
    @Override
    public String toString(){
        String result = "";
        for (int h = 0; h < this.getHeight(); h++) {
            for (int w = 0; w < this.getWidth(); w++) {
                result += this.get(w,h)+" ";
            }
            result += "\r\n";
        }
        return result;
    }
}
