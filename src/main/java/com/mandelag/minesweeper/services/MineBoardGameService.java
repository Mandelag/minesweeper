package com.mandelag.minesweeper.services;

import com.mandelag.minesweeper.impl.CachedGeoToolsGridGenerator;
import com.mandelag.minesweeper.ImmutableGrid;
import com.mandelag.minesweeper.MineBoard;
import com.mandelag.minesweeper.QueryableGeoGridTemplateGenerator;

/**
 * Service wrapper class for the class MineBoard.
 *
 * @author Keenan Gebze
 */
public class MineBoardGameService {

    private MineBoard mineBoard;
    private boolean lost;
    private static QueryableGeoGridTemplateGenerator templater;

    public MineBoardGameService(String country, int size) {
        if(templater == null){
            templater = new CachedGeoToolsGridGenerator("C:\\Users\\keenan\\shapefile\\TM_WORLD_BORDERS-0.3.shp");
        }
        this.mineBoard = new MineBoard(templater.query("NAME='"+country.replace("'","")+"'", size).toIntegerArrays(), 10);
        System.out.println(mineBoard);
    }

    /**
     * Open a minesweeper cell at a given coordinate.
     * @param x the x location of the cell.
     * @param y the y location of the cell.
     * @return a JSON string containing opened minesweeper grid.
     */
    public String open(int x, int y) {
        if (lost) {
            return "{\"status\":\"Game ended\"}";
        }
        StringBuilder sb = new StringBuilder("{");
        int[][] opened = mineBoard.open(x, y);
        if(opened[y][x] < 0 && opened[y][x] >= -90){
            lost = true;
        }
        sb.append("\"grid\": ")
                .append(MineBoardGameService.arrayToJson(opened))
                .append("}");
        return sb.toString();
    }

    /**
     * Get the grid.
     * @param hidden specify whether the returned grid contain grid values or only template (hidden).
     * @return return the minesweeper grid values or grid template.
     */
    public String getCurrentState(boolean hidden) {
        if (lost) {
            return "{\"status\":\"Game ended\"}";
        }
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"grid\": ")
                .append(MineBoardGameService.immutableGridToJson(mineBoard.getGrid(), hidden))
                .append("}");
        return sb.toString();
    }

    /**
     * Convert an integer array to json array.
     * @param grid the input integer array.
     * @return JSON integer array of the integer array.
     */
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

    /**
     * Convert immutable grid into JSON integer array.
     * @param ig input immutable grid object.
     * @param hide specify wether the grid values are hiden or not.
     * @return JSON integer array of the immutable grid.
     */
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
