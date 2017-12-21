/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.minesweeper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.filter.text.cql2.CQLException;

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
        if(this.mb == null){
            this.mb = MineBoard.fromCountry("Singapore", 400);
        }
    }

    public String open(int x, int y) {
        return "";
    }

    public String getCurrentState() {
        StringBuilder sb = new StringBuilder("{");
        String sessionId = "hehehehe";
        sb.append("\"sessionId\": \"").append(sessionId).append("\",\n");
        sb.append("\"grid\": ").append(mb.arrayToJson()).append("}");
        return sb.toString();
    }

    public boolean isLost() {
        return lost;
    }
}
