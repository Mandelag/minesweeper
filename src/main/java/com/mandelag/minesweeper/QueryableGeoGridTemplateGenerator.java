/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.minesweeper;

import java.util.ArrayList;
/**
 *
 * @author Keenan
 */
public interface QueryableGeoGridTemplateGenerator {
    public abstract ArrayList<String> listFieldEntry(String field);
    public abstract ImmutableGrid query(String featureQuery, int nGrid);
}
