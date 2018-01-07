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
    private static QueryableGeoGridTemplateGenerator templater;

    public MineBoardGameService(String country, int size) {
        if(templater == null){
            templater = new CachedGeoToolsGridGenerator("C:\\Users\\keenan\\shapefile\\TM_WORLD_BORDERS-0.3.shp");
        }
        this.mb = new MineBoard(templater.query(country, size), 10);
        System.out.println(mb);
    }

    public String open(int x, int y) {
        if (isLost()) {
            return "{\"status\":\"Game ended\"}";
        }
        StringBuilder sb = new StringBuilder("{");
        String sessionId = "hehehehe";
        //sb.append("\"sessionId\": \"").append(sessionId).append("\",\n");
        int[][] opened = mb.open(x, y);
        if(opened[y][x] < 0 && opened[y][x] >= -90){
            lost = true;
        }
        sb.append("\"grid\": ")
                .append(MineBoardGameService.arrayToJson(opened))
                .append("}");
        MineBoard.printArray(opened);
        return sb.toString();
    }

    public String getCurrentState(boolean hidden) {
        //if (isLost()) {
        //    return "{\"status\":\"Game ended\"}";
        //}
        StringBuilder sb = new StringBuilder("{");
        String sessionId = "hehehehe";
        //sb.append("\"sessionId\": \"").append(sessionId).append("\",\n");
        sb.append("\"grid\": ")
                .append(MineBoardGameService.immutableGridToJson(mb.getGrid(), hidden))
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
                jsonArray.append(grid[h][w] + "").append(',');
            }
            jsonArray.setCharAt(jsonArray.length() - 1, ']');
            jsonArray.append(',');
        }
        jsonArray.setCharAt(jsonArray.length() - 1, ']');
        return jsonArray.toString();
    }

    public static String immutableGridToJson(ImmutableGrid ig, boolean hide) {
        StringBuilder jsonArray = new StringBuilder("[");
        for (int y = 0; y < ig.getHeight(); y++) {
            jsonArray.append('[');
            for (int x = 0; x < ig.getWidth(); x++) {
                jsonArray.append(hide ? (ig.get(x, y) < -90 ? -99 : 0) : ig.get(x, y) + "").append(',');
            }
            jsonArray.setCharAt(jsonArray.length() - 1, ']');
            jsonArray.append(',');
        }
        jsonArray.setCharAt(jsonArray.length() - 1, ']');
        return jsonArray.toString();
    }
}
