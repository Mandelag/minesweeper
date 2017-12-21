package com.mandelag.minesweeper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.util.Random;
import java.io.File;
import java.io.IOException;
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
 * Class representing minesweeper board. It is a rectangular grid that have a
 * width, a height, and are represented by an array of integers. Each integer in
 * the array represent object mapping: [ 0 - 8] -> represent the number of mine
 * in the eight surrounding grid 9 -> is a special value that represents a
 * cluster of zeros [-8 - -1] -> represents a mine in the grid.
 *
 * @author Keenan Gehze
 * @email keenan.gebze@gmail.com
 */
public class MineBoard {

    private int width, height;
    private int bombs;
    private int[][] grid;
    private int[][] openedGrid;
    
    static final int VISITED = 9;
    static final int OUTSIDE = -99;

    public MineBoard() {
        this(10, 10);
    }

    public MineBoard(int width, int height) {
        this(width, height, 10);
    }

    public MineBoard(int width, int height, int nBombs) {
        this.width = width;
        this.height = height;
        this.bombs = nBombs;
        this.grid = new int[height][width];
        initializeBoard();
    }

    public MineBoard(int[][] grid) {
        this.height = grid.length;
        /* height are determined by the array length of the array first entry */
        this.width = grid[0].length;
        this.grid = grid;
    }

    @Override
    public String toString() {
        String result = "";
        for (int[] grid1 : grid) {
            for (int w = 0; w < grid1.length; w++) {
                result += "0 ";
            }
            result += "\r\n";
        }
        return result;
    }

    public int[][] getGrid() {
        return grid;
    }

    /**
     * Method that are called when the a tile is clicked. It will reveal the
     * tile number in the resulting array. If grid[y,x] >= 1 && grid[y,x] <= 8
     * then only that particular tile is revealed. If grid[y,x] == 0 then it
     * will search recursively for neighboring zeros and reveal the adjancent
     * numbers. If grid[y,x] < 0 then a mine is clicked and counted as game over
     * on the server side.
     *
     * @param x the x location of the requested cell
     * @param y the y location of the requested cell
     * @return an array containing opened cells
     */
    public int[][] open(int x, int y) {
        int[][] result = open(new int[height][width], x, y);
        // register the opened grid in the openedGrid array
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                openedGrid[i][j] = result[i][j] != 0 ? result[i][j] : openedGrid[i][j];
            }
        }
        return result;
    }

    /**
     * Method that are called when the a tile is clicked. It will reveal the
     * tile number in the resulting array. If grid[y,x] >= 1 && grid[y,x] <= 8
     * then only that particular tile is revealed. If grid[y,x] == 0 then it
     * will search recursively for neighboring zeros and reveal the adjancent
     * numbers. If grid[y,x] < 0 then a mine is clicked and counted as game over
     * in the server side.
     *
     * @param result the container for the resulting array (used for searching
     * recursively)
     * @param x the x location of the requested cell
     * @param y the y location of the requested cell
     * @return an array containing opened cells
     */
    private int[][] open(int[][] result, int x, int y) {
        if (grid[y][x] == 0) {
            result[y][x] = VISITED;
            try {
                result[y + 1][x + 1] = (result[y + 1][x + 1] == 0 && grid[y + 1][x + 1] == 0) ? 0 : (result[y + 1][x + 1] == VISITED ? VISITED : grid[y + 1][x + 1]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                result[y + 1][x - 1] = (result[y + 1][x - 1] == 0 && grid[y + 1][x - 1] == 0) ? 0 : (result[y + 1][x - 1] == VISITED ? VISITED : grid[y + 1][x - 1]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                result[y - 1][x + 1] = (result[y - 1][x + 1] == 0 && grid[y - 1][x + 1] == 0) ? 0 : (result[y - 1][x + 1] == VISITED ? VISITED : grid[y - 1][x + 1]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                result[y - 1][x - 1] = (result[y - 1][x - 1] == 0 && grid[y - 1][x - 1] == 0) ? 0 : (result[y - 1][x - 1] == VISITED ? VISITED : grid[y - 1][x - 1]);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid[y - 1][x] == 0 && result[y - 1][x] != VISITED) {
                    open(result, x, y - 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid[y][x + 1] == 0 && result[y][x + 1] != VISITED) {
                    open(result, x + 1, y);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid[y][x - 1] == 0 && result[y][x - 1] != VISITED) {
                    open(result, x - 1, y);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (grid[y + 1][x] == 0 && result[y + 1][x] != VISITED) {
                    open(result, x, y + 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        } else {
            result[y][x] = grid[y][x];
        }
        return result;
    }

    private void initializeBoard() {
        int x, y;
        boolean bombExist = true;
        Random r = new Random();
        for (int b = bombs; b > 0; b--) {
            x = r.nextInt(width);
            y = r.nextInt(height);
            if (grid[y][x] < 0) {
                b++;
            } else {
                grid[y][x] = -9;
                try {
                    grid[y][x + 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y][x - 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y + 1][x] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y + 1][x + 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y + 1][x - 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y - 1][x] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y - 1][x + 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    grid[y - 1][x - 1] += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
    }

    public static int[][] countryToGrid(String country, int nGrid) throws IOException, CQLException {
        File worldShapefile = new File("C:\\Users\\keenan\\shapefile\\TM_WORLD_BORDERS-0.3.shp");
        FileDataStore fds = FileDataStoreFinder.getDataStore(worldShapefile);
        SimpleFeatureSource featureSource = fds.getFeatureSource();

        try (SimpleFeatureIterator iterator = featureSource.getFeatures(CQL.toFilter("NAME = '" + country.replace("'", "\\'") + "'")).features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Object og = feature.getAttribute(0);
                if (og instanceof Geometry) {
                    Geometry g = (Geometry) og;
                    try {
                        return geometryToGrid(g, nGrid);
                    } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                        Logger.getLogger(MineBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("");
            }
        }
        return countryToGrid("Indonesia", 400);
    }

    private static int[][] geometryToGrid(Geometry geom, int nGrid) throws FactoryException, MismatchedDimensionException, TransformException {
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
        int[][] resultArray = new int[nGridY][nGridX];
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
        return resultArray;
    }

    public static void printArray(int[][] array) {
        for (int h = 0; h < array.length; h++) {
            for (int w = 0; w < array[h].length; w++) {
                System.out.print(array[h][w] >= 0 ? "  " + array[h][w] : " " + array[h][w]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static MineBoard fromCountry(String country, int size) {
        MineBoard result = null;
        try {
            result = new MineBoard(countryToGrid(country, size));
        } catch (IOException | CQLException ex) {
            Logger.getLogger(MineBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static void main(String[] args) throws IOException, CQLException {
        System.out.println(MineBoardGameService.arrayToJson(countryToGrid("Thailand", 400)));
    }
}
