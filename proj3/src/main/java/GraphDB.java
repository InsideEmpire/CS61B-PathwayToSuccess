import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    Map<Long, Node> nodes;
    Map<Long, Edge> edges;

    /**
     * Add a new vertex to the graph.
     * @param ID the ID of the new vertex.
     * @param latitude the latitude of the new vertex.
     * @param longitude the longitude of the new vertex.
     */
    void addVertices(String ID, String latitude, String longitude) {
        Node node = new Node(ID, latitude, longitude);
        nodes.put(node.id, node);
    }

    /**
     * Add a new connection relationship to the graph.
     * @param ID ID of new edge.
     */
    void addAdjacent(String ID) {
        edges.put(Long.valueOf(ID), new Edge(ID));
    }

    void addAdjacent(Edge edge) {
        edges.put(edge.id, edge);
        long last1 = -1L, last2 = -2L;
        if (edge.linkedNode.size() == 2) {
            nodes.get(edge.linkedNode.get(0)).adjacentNode.add(edge.linkedNode.get(1));
            nodes.get(edge.linkedNode.get(1)).adjacentNode.add(edge.linkedNode.get(0));
            nodes.get(edge.linkedNode.get(0)).adjacentEdge.add(edge.id);
            nodes.get(edge.linkedNode.get(1)).adjacentEdge.add(edge.id);
            return;
        }
        for (long reference : edge.linkedNode) {
            nodes.get(reference).adjacentEdge.add(edge.id);
            if (last2 < 0) {
                last2 = last1;
                last1 = reference;
                continue;
            }
            nodes.get(last2).adjacentNode.add(last1);
            nodes.get(last1).adjacentNode.add(last2);
            nodes.get(last1).adjacentNode.add(reference);
            nodes.get(reference).adjacentNode.add(last1);

            nodes.get(last1).adjacentEdge.add(edge.id);
            nodes.get(last2).adjacentEdge.add(edge.id);
            nodes.get(reference).adjacentEdge.add(edge.id);
            last2 = last1;
            last1 = reference;
        }
    }

    void lastIDAddTag(String lastID, String k, String v) {
        long id = Long.parseLong(lastID);
        if (nodes.containsKey(id)) {
            nodes.get(id).addTag(k, v);
        } else if (edges.containsKey(id)) {
            edges.get(id).addTag(k, v);
        }
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            edges = new HashMap<>();
            nodes = new HashMap<>();

            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Set<Long> ID = new HashSet<>();
        for (Long id : nodes.keySet()) {
            if (nodes.get(id).adjacentNode.isEmpty()) {
                ID.add(id);
            }
        }
        for (Long id : ID) {
            nodes.remove(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return new ArrayList<>(nodes.keySet());
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return new ArrayList<>(nodes.get(v).adjacentNode);
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long id = 0L;
        double distance = Double.MAX_VALUE;
        for (Node node : nodes.values()) {
            if (distance(lon, lat, node.lon, node.lat) < distance) {
                distance = distance(lon, lat, node.lon, node.lat);
                id = node.id;
            }
        }
        return id;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }

    public String getWayName(long nextNode, long nextNextNode) {
        Set<Long> intersection = new HashSet<>(nodes.get(nextNode).adjacentEdge);
        intersection.retainAll(nodes.get(nextNextNode).adjacentEdge);
        for (Long id : intersection) {
            return edges.get(id).tags.get("name");
        }
        return "unknown road";
    }

    static class Node {
        long id;
        double lat;
        double lon;
        Map<String, String> tags;
        Set<Long> adjacentEdge;
        Set<Long> adjacentNode;

        Node(long ID, double latitude, double longitude) {
            this.id = ID;
            this.lat = latitude;
            this.lon = longitude;
            this.adjacentEdge = new HashSet<>();
            this.adjacentNode = new HashSet<>();
            this.tags = new HashMap<>();
        }

        public Node(String ID, String latitude, String longitude) {
            this.id = Long.parseLong(ID);
            this.lat = Double.parseDouble(latitude);
            this.lon = Double.parseDouble(longitude);
            this.adjacentEdge = new HashSet<>();
            this.adjacentNode = new HashSet<>();
            this.tags = new HashMap<>();
        }

        void addTag(String k, String v) {
            this.tags.put(k, v);
        }
    }

    static class Edge {
        long id;
        List<Long> linkedNode;
        Map<String, String> tags;

        Edge(long ID) {
            this.id = ID;
            this.linkedNode = new ArrayList<>();
            this.tags = new HashMap<>();
        }
        Edge(String ID) {
            this.id = Long.parseLong(ID);
            this.linkedNode = new ArrayList<>();
            this.tags = new HashMap<>();
        }

        void addTag(String k, String v) {
            this.tags.put(k, v);
        }

        void addLinkedNode(String ID) {
            this.linkedNode.add(Long.valueOf(ID));
        }
    }
}
