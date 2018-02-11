package com.mandelag.minesweeper;

import java.util.ArrayList;
/**
 * An interface for any queryable (polygon) shape that can be converted into grid template.
 * @author Keenan Gebze
 */
public interface QueryableGeoGridTemplateGenerator {
    public abstract ArrayList<String> listFieldEntry(String field);
    public abstract ImmutableGrid query(String featureQuery, int nGrid);
}
