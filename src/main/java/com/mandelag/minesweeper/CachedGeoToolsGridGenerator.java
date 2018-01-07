/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.minesweeper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Implementation of QueryableGeoGridTemplateGenerator using GeoTools Library.
 * @author Keenan Gebze
 */
public class CachedGeoToolsGridGenerator implements QueryableGeoGridTemplateGenerator{
    
    private final String fileName;
    private final HashMap<Integer, ImmutableGrid> cachedGrid;
    
    public CachedGeoToolsGridGenerator(String fileName) {
        this.fileName = fileName;
        this.cachedGrid = new HashMap<>();
    }
    
    @Override
    public ArrayList<String> listFieldEntry(String field) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ImmutableGrid query(String featureQuery, int nGrid) {
        Integer hash = featureQuery.hashCode() + nGrid + fileName.hashCode();
        ImmutableGrid result = cachedGrid.get(hash);
        if(result == null){
            try {
                cachedGrid.put(hash, countryToGrid(featureQuery, nGrid));
                return cachedGrid.get(hash);
            }catch(CQLException e){
                System.out.println(e);
            }catch(IOException e){
                System.out.println(e);
            }
        }
        System.out.println("Using cache!");
        return result;
    }
    
    private ImmutableGrid countryToGrid(String query, int nGrid) throws IOException, CQLException {
        File worldShapefile = new File(fileName);
        FileDataStore fds = FileDataStoreFinder.getDataStore(worldShapefile);
        SimpleFeatureSource featureSource = fds.getFeatureSource();

        try (SimpleFeatureIterator iterator = featureSource.getFeatures(CQL.toFilter(query)).features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Object og = feature.getAttribute(0);
                if (og instanceof Geometry) {
                    Geometry g = (Geometry) og;
                    try {
                        return geometryToGrid(g, nGrid); //return only the first result of the query
                    } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                        Logger.getLogger(MineBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("");
            }
        }
        return countryToGrid("NAME='Singapore'", 400);
    }

    private ImmutableGrid geometryToGrid(Geometry geom, int nGrid) throws FactoryException, MismatchedDimensionException, TransformException {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        Envelope ei = geom.getEnvelopeInternal();

        Coordinate envelopeCentre = ei.centre();
        CoordinateReferenceSystem crsIn = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem crsOut = CRS.parseWKT("PROJCS[\"Lambert_Azimuthal_Equal_Area\",\n"
                + "    GEOGCS[\"GCS_WGS_1984\",\n"
                + "        DATUM[\"D_WGS_1984\",\n"
                + "            SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],\n"
                + "        PRIMEM[\"Greenwich\",0.0],\n"
                + "        UNIT[\"Degree\",0.0174532925199433]],\n"
                + "    PROJECTION[\"Lambert_Azimuthal_Equal_Area\"],\n"
                + "    PARAMETER[\"False_Easting\",0.0],\n"
                + "    PARAMETER[\"False_Northing\",0.0],\n"
                + "    PARAMETER[\"Central_Meridian\"," + envelopeCentre.x + "],\n"
                + "    PARAMETER[\"Latitude_Of_Origin\"," + envelopeCentre.y + "],\n"
                + "    UNIT[\"Meter\",1.0]]");

        MathTransform transform = CRS.findMathTransform(crsIn, crsOut);
        Geometry projectedGeom = JTS.transform((Geometry) geom.clone(), transform);
        Geometry envelopeGeom = projectedGeom.getEnvelope();
        Envelope envelopeInternal = envelopeGeom.getEnvelopeInternal();
        double envelopeArea = envelopeInternal.getArea();
        double gridSideLength = Math.sqrt(envelopeArea / nGrid);
        int nGridX = (int) Math.ceil((envelopeInternal.getMaxX() - envelopeInternal.getMinX()) / gridSideLength);
        int nGridY = (int) Math.ceil((envelopeInternal.getMaxY() - envelopeInternal.getMinY()) / gridSideLength);
        byte[][] resultArray = new byte[nGridY][nGridX];
        GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(1000), geom.getSRID());
        /* "topLeft" are relative.. */
        Coordinate topLeft;
        Coordinate topRight;
        Coordinate bottomRight;
        Coordinate bottomLeft;

        for (int y = 0; y < nGridY; y++) {
            for (int x = 0; x < nGridX; x++) {
                // represents "top left" corner of the grid 
                double xBase = envelopeInternal.getMinX() + gridSideLength * x;
                double yBase = envelopeInternal.getMaxY() - gridSideLength * y;

                topLeft = new Coordinate(xBase, yBase);
                topRight = new Coordinate(xBase + gridSideLength, yBase);
                bottomRight = new Coordinate(xBase + gridSideLength, yBase + gridSideLength);
                bottomLeft = new Coordinate(xBase, yBase + gridSideLength);

                Geometry grid = geomFactory.createPolygon(new Coordinate[]{topLeft, topRight, bottomRight, bottomLeft, topLeft});
                if (grid.intersection(projectedGeom).getArea() > Math.pow(gridSideLength, 2) / 2) {
                    resultArray[y][x] = 0;
                } else {
                    resultArray[y][x] = MineBoard.OUTSIDE;
                }
            }
        }
        return new ImmutableGrid(resultArray);
    }
}
