/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.minesweeper;

/**
 * Service wrapper class for the class MineBoard.
 *
 * @author Keenan Gebze
 */
public class MineBoardGameService {

    private MineBoard mb;
    private boolean lost;

    public MineBoardGameService(String country, int size) {
        this.mb = MineBoard.fromCountry(country, size);
        if (this.mb == null) {
            this.mb = MineBoard.fromCountry("Singapore", 400);
        }
    }

    public String open(int x, int y) {
        if (isLost()) {
            return "{\"status\":\"Game ended\"}";
        }
        StringBuilder sb = new StringBuilder("{");
        String sessionId = "hehehehe";
        //sb.append("\"sessionId\": \"").append(sessionId).append("\",\n");
        int[][] opened = mb.open(x, y);
        sb.append("\"grid\": ")
                .append(MineBoardGameService.arrayToJson2(opened))
                .append("}");
        MineBoard.printArray(opened);
        return sb.toString();
    }

    public String getCurrentState() {
        //if (isLost()) {
        //    return "{\"status\":\"Game ended\"}";
        //}
        StringBuilder sb = new StringBuilder("{");
        String sessionId = "hehehehe";
        //sb.append("\"sessionId\": \"").append(sessionId).append("\",\n");
        sb.append("\"grid\": ")
                .append(MineBoardGameService.arrayToJson(mb.getGrid()))
                .append("}");
        return sb.toString();
    }

    public boolean isLost() {
        return lost;
    }

    public static String arrayToJson(int[][] grid) {
        StringBuilder jsonArray = new StringBuilder("[");
        for (int h = 0; h < grid.length; h++) {
            jsonArray.append('[');
            for (int w = 0; w < grid[h].length; w++) {
                jsonArray.append(grid[h][w] >= MineBoard.OUTSIDE-10 ? grid[h][w] : 0).append(',');
            }
            jsonArray.setCharAt(jsonArray.length() - 1, ']');
            jsonArray.append(',');
        }
        jsonArray.setCharAt(jsonArray.length() - 1, ']');
        return jsonArray.toString();
    }
    
    public static String arrayToJson2(int[][] grid) {
        StringBuilder jsonArray = new StringBuilder("[");
        for (int h = 0; h < grid.length; h++) {
            jsonArray.append('[');
            for (int w = 0; w < grid[h].length; w++) {
                jsonArray.append(grid[h][w]+"").append(',');
            }
            jsonArray.setCharAt(jsonArray.length() - 1, ']');
            jsonArray.append(',');
        }
        jsonArray.setCharAt(jsonArray.length() - 1, ']');
        return jsonArray.toString();
    }
}
