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

    private final double TOTAL_LONGITUDE = 122.29980468 - 122.21191406;
    private final double TOTAL_LATITUDE = 37.89219554 - 37.82280243;

    private final double START_LONGITUDE = -122.29980468;
    private final double END_LONGITUDE = -122.21191406;
    private final double START_LATITUDE = 37.82280243;
    private final double END_LATITUDE = 37.89219554;

    private final double PIXEL = 256;

    public Rasterer() {
        for (int Dth = 0; Dth <= 7; Dth++) {
            /*
                @num: How many tiles are in a row at the Dth zoom level
                @DthLon: The longitude length of each tiles at Dth zoom level
                @DthLat: The latitude length of each tiles at Dth zoom level
                @DthLonDPP: The LonDPP of Dth zoom level tile
             */
            double num = Math.pow(2, Dth);
            double DthLon = TOTAL_LONGITUDE / num;
            double DthLat = TOTAL_LATITUDE / num;
            double DthLonDPP = DthLon / PIXEL;
            LonDPP_List.add(DthLonDPP);
            ullon_List.add(new ArrayList<>());
            lrlon_List.add(new ArrayList<>());
            ullat_List.add(new ArrayList<>());
            lrlat_List.add(new ArrayList<>());
            for (int i = 0; i < num; i++) {
                ullon_List.get(Dth).add(START_LONGITUDE + i * DthLon);
                lrlon_List.get(Dth).add(START_LONGITUDE + (i + 1) * DthLon);
                ullat_List.get(Dth).add(START_LATITUDE + i * DthLat);
                lrlat_List.get(Dth).add(START_LATITUDE + (i + 1) * DthLat);
            }
            System.out.println("LonDPP of " + Dth + "th level is: " + DthLonDPP);
        }
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
         System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double w = params.get("ullon");
        double h = params.get("ullon");
        double LonDPP = (lrlon - ullon) / w;
        System.out.println(LonDPP);

        int depth = 0;
        for (int Dth = 0; Dth <= 7; Dth++) {
            if (LonDPP_List.get(Dth) < LonDPP) {
                depth = Dth - 1;
                break;
            }
        }
        System.out.println(depth + " is the BEST zoom level");

        // TODO: finish render_grid...
        // TODO: (int) Math.pow(2, depth) is not good answer. Try to calculate Query Box's position
        String[][] render_grid = new String[(int) Math.pow(2, depth)][(int) Math.pow(2, depth)];

        return results;
    }

}
