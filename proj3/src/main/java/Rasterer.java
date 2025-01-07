import java.util.*;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private final List<Double> LonDPP_List = new LinkedList<>();
    /*
        Store the ul (or lr) lon (or lat) of each tiles at Dth zoom level
        lon_List.get(Dth zoom level).get(x)
        lat_List.get(Dth zoom level).get(y)
     */
    private final List<List<Double>> ullon_List = new LinkedList<>();
    private final List<List<Double>> ullat_List = new LinkedList<>();
    private final List<List<Double>> lrlon_List = new LinkedList<>();
    private final List<List<Double>> lrlat_List = new LinkedList<>();

    private final double ROOT_LON = 122.29980468 - 122.21191406;
    private final double ROOT_LAT = 37.89219554 - 37.82280243;

    private final double ROOT_ULLON = -122.29980468;
    private final double ROOT_LRLON = -122.21191406;
    private final double ROOT_ULLAT = 37.89219554;
    private final double ROOT_LRLAT = 37.82280243;

    private final double PIXEL = 256;

    public Rasterer() {
        for (int Dth = 0; Dth <= 7; Dth++) {
            /*
                @numbersARaw: How many tiles are in a row at the Dth zoom level
                @DthLon: The longitude length of each tiles at Dth zoom level
                @DthLat: The latitude length of each tiles at Dth zoom level
                @DthLonDPP: The LonDPP of Dth zoom level tile
             */
            int numbersAll = (int) Math.pow(2, Dth);
//            int numbersOneRaw = Dth + 1;
            double DthLon = ROOT_LON / numbersAll;
            double DthLat = ROOT_LAT / numbersAll;
            double DthLonDPP = DthLon / PIXEL;
            LonDPP_List.add(DthLonDPP);
            ullon_List.add(new ArrayList<>());
            lrlon_List.add(new ArrayList<>());
            ullat_List.add(new ArrayList<>());
            lrlat_List.add(new ArrayList<>());
            for (int i = 0; i < numbersAll; i++) {
                ullon_List.get(Dth).add(ROOT_ULLON + i * DthLon);
                lrlon_List.get(Dth).add(ROOT_ULLON + (i + 1) * DthLon);
                ullat_List.get(Dth).add(ROOT_ULLAT - i * DthLat);
                lrlat_List.get(Dth).add(ROOT_ULLAT - (i + 1) * DthLat);
//                System.out.println("ullat at " + i + ullat_List.get(Dth).get(i));
//                System.out.println("lrlat at " + i + lrlat_List.get(Dth).get(i));
            }
//            System.out.println("LonDPP of " + Dth + "th level is: " + DthLonDPP);
//            System.out.println(lrlat_List.get(Dth).get(numbersAll - 1));
//            System.out.println(ROOT_LRLAT);
        }
        for (int i = 0; i < 8; i++) {
//            System.out.println("ullat of " + i + " at 3 level is " + ullat_List.get(3).get(i));
//            System.out.println("lrlat of " + i + " at 3 level is " + lrlat_List.get(3).get(i));
        }
//        System.out.println("d1_x1_y1, lrlon: " + lrlon_List.get(1).get(1) + " ullon: " + ullon_List.get(1).get(1));
//        System.out.println("LonDPP: " + (lrlon_List.get(1).get(1) - ullon_List.get(1).get(1)) / PIXEL);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
//        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double w = params.get("w");
        double h = params.get("h");
        double LonDPP = (lrlon - ullon) / w;
//        System.out.println(lrlon - ullon);
//        System.out.println(LonDPP);

        int depth = 0;
        for (int Dth = 0; Dth <= 7; Dth++) {
            if (LonDPP_List.get(Dth) < LonDPP) {
                // TODO: Dth -1 or Dth
                depth = Dth;
                break;
            }
        }
//        System.out.println(depth + " is the BEST zoom level");

        int ulx = 0, uly = 0, lrx = 0, lry = 0;
        double raster_ul_lon = 0, raster_ul_lat = 0, raster_lr_lon = 0, raster_lr_lat = 0;
        for (int i = 0; i < (int) Math.pow(2, depth); i++) {
            if (ullon_List.get(depth).get(i) <= ullon && ullon <= lrlon_List.get(depth).get(i)) {
                ulx = i;
                raster_ul_lon = ullon_List.get(depth).get(i);
            }
            if (ullat_List.get(depth).get(i) >= ullat && ullat >= lrlat_List.get(depth).get(i)) {
                uly = i;
                raster_ul_lat = ullat_List.get(depth).get(i);
            }
            if (ullon_List.get(depth).get(i) <= lrlon && lrlon <= lrlon_List.get(depth).get(i)) {
                lrx = i;
                raster_lr_lon = lrlon_List.get(depth).get(i);
            }
            if (ullat_List.get(depth).get(i) >= lrlat && lrlat >= lrlat_List.get(depth).get(i)) {
                lry = i;
                raster_lr_lat = lrlat_List.get(depth).get(i);
            }
        }
//        System.out.println("ulx: " + ulx + " uly: " + uly + " lrx: " + lrx + " lry: " + lry);

        String[][] render_grid = new String[lry - uly + 1][lrx - ulx + 1];
        for (int i = 0; i <= lry - uly; i++) {
            for (int j = 0; j <= lrx - ulx; j++) {
                StringBuffer str = new StringBuffer();
                str.append("d").append(depth).append("_x").append(ulx + j).append("_y").append(uly + i).append(".png");
                render_grid[i][j] = str.toString();
//                System.out.println(str);
            }
        }

        boolean query_success = true;

        results.put("render_grid", render_grid);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", depth);
        results.put("query_success", query_success);
        return results;
    }

}
