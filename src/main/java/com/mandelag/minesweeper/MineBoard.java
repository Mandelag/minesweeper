package com.mandelag.minesweeper;

import java.util.Random;
import java.util.ArrayList;

public class MineBoard{
    private int width, height;
    private int bombs;
    private int[][] grid;
    private static final int VISITED = 9;
    private static final int NULL = 99;
    
    public MineBoard(){
        this(10, 10);
    }
    
    public MineBoard(int width, int height){
        this(width, height, 10);
    }
    
    public MineBoard(int width, int height, int nBombs){
        this.width = width;
        this.height = height;
        this.bombs = nBombs;
        this.grid = new int[height][width];
        initializeBoard();
    }
    
    public static void printArray(int[][] array){
        for(int h=0;h<array.length;h++){
            for(int w=0;w<array[h].length;w++){
                System.out.print(array[h][w]>=0?"  "+array[h][w]:" "+array[h][w]);
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    public String toString(){
        return "Not implemented yet.";
    }
    
    public int[][] getGrid(){
        return grid;
    }
    
    public int[][] open(int x, int y){
        return open(new int[height][width], x, y);
    }
    
    private int[][] open(int[][] result, int x, int y){
        if(grid[y][x] == 0){
            result[y][x] = VISITED;
            try{result[y+1][x+1] = (result[y+1][x+1]==0 && grid[y+1][x+1]==0)?0:(result[y+1][x+1]==VISITED?VISITED:grid[y+1][x+1]);} catch(ArrayIndexOutOfBoundsException e) {}
            try{result[y+1][x-1] = (result[y+1][x-1]==0 && grid[y+1][x-1]==0)?0:(result[y+1][x-1]==VISITED?VISITED:grid[y+1][x-1]);} catch(ArrayIndexOutOfBoundsException e) {}
            try{result[y-1][x+1] = (result[y-1][x+1]==0 && grid[y-1][x+1]==0)?0:(result[y-1][x+1]==VISITED?VISITED:grid[y-1][x+1]);} catch(ArrayIndexOutOfBoundsException e) {}
            try{result[y-1][x-1] = (result[y-1][x-1]==0 && grid[y-1][x-1]==0)?0:(result[y-1][x-1]==VISITED?VISITED:grid[y-1][x-1]);} catch(ArrayIndexOutOfBoundsException e) {}
            try{if(grid[y-1][x] == 0 && result[y-1][x] != VISITED)open(result, x, y-1);}catch(ArrayIndexOutOfBoundsException e) {}
            try{if(grid[y][x+1] == 0 && result[y][x+1] != VISITED)open(result, x+1, y);}catch(ArrayIndexOutOfBoundsException e) {}
            try{if(grid[y][x-1] == 0 && result[y][x-1] != VISITED)open(result, x-1, y);}catch(ArrayIndexOutOfBoundsException e) {}
            try{if(grid[y+1][x] == 0 && result[y+1][x] != VISITED)open(result, x, y+1);}catch(ArrayIndexOutOfBoundsException e) {}
        }else{
            result[y][x] = grid[y][x];
        }
        return result;
    }
    
    private void initializeBoard(){
        int x, y;
        boolean bombExist = true;
        Random r = new Random();
        for(int b=bombs;b>0;b--){
            x = r.nextInt(width);
            y = r.nextInt(height);
            if(grid[y][x] < 0){
                b++;
            }else{
                grid[y][x] = -9;
                try {grid[y][x+1] += 1;} catch(ArrayIndexOutOfBoundsException e) {}
                try {grid[y][x-1] += 1;} catch(ArrayIndexOutOfBoundsException e) {}
                try {grid[y+1][x] += 1;} catch(ArrayIndexOutOfBoundsException e) {}
                try {grid[y+1][x+1] += 1;} catch(ArrayIndexOutOfBoundsException e) {}
                try {grid[y+1][x-1] += 1;} catch(ArrayIndexOutOfBoundsException e) {}
                try {grid[y-1][x] += 1;} catch(ArrayIndexOutOfBoundsException e) {}
                try {grid[y-1][x+1] += 1;} catch(ArrayIndexOutOfBoundsException e) {}
                try {grid[y-1][x-1] += 1;} catch(ArrayIndexOutOfBoundsException e) {}
            }
        }
    }
    
    public static void main(String[] args) {
        MineBoard mb = new MineBoard(16, 16, 25);
        MineBoard.printArray(mb.getGrid());
        MineBoard.printArray(mb.open(2,2));
    }
}