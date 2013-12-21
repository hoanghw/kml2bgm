package factories;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import implementations.DynamicNode;
import implementations.KmlNode;
import interfaces.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

public class NodeFactory {
    private static final int NODE_KML = 0;
    private static final int NODE_DYNAMIC_POINT = 1;
    private static final int NODE_DYNAMIC_REGION = 2;
    private static double RADIUS = 0; //meters
    private static int SEGMENTS = 30;

    private File kml;
    private int type;
    private ArrayList<Node> dynamicNodes;
    private ArrayList<Node> kmlNodes;

    public NodeFactory(File kml){
        this.kml = kml;
        this.type = NODE_KML;
    }

    public NodeFactory(String id, Coordinate c){
        this.type = NODE_DYNAMIC_POINT;
        this.dynamicNodes = new ArrayList<Node>();
        this.dynamicNodes.add(new DynamicNode(id,c));
    }

    public NodeFactory(String id, ArrayList<Coordinate> c){
        this.type = NODE_DYNAMIC_REGION;
        this.dynamicNodes = new ArrayList<Node>();
        this.dynamicNodes.add(new DynamicNode(id,c));
    }

    public void setPointToCircle(double radius){
        RADIUS = radius;
    }

    public void setCircleSegments(int segments){
        SEGMENTS = segments;
    }

    public ArrayList<Node> getNodes() throws IOException, SAXException, ParserConfigurationException {
        if (this.type == NODE_KML){
            kmlNodes = processKML(this.kml);
            if (RADIUS != 0){
                kmlNodes = addCircleToPoint(kmlNodes);
            }
            return kmlNodes;
        }

        if (this.type == NODE_DYNAMIC_POINT){
            if (RADIUS != 0){
                dynamicNodes = addCircleToPoint(dynamicNodes);
            }
            return dynamicNodes;
        }

        if (this.type == NODE_DYNAMIC_REGION){
            return dynamicNodes;
        }

        return null;
    }

    private ArrayList<Node> processKML(File kml) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<Node> result = new ArrayList<Node>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new NodeFactory.SimpleErrorHandler());

        InputStream inputStream= new FileInputStream(kml);
        Reader reader = new InputStreamReader(inputStream);
        Document doc = builder.parse(new InputSource(reader));

        doc.getDocumentElement().normalize();

        NodeList placemarks = doc.getElementsByTagName("Placemark");

        for (int i = 0; i < placemarks.getLength(); i++){
            Element placemark = (Element) placemarks.item(i);
            String id = placemark.getElementsByTagName("name").item(0).getTextContent();

            Element point = (Element) placemark.getElementsByTagName("Point").item(0);
            Element linearString = (Element) placemark.getElementsByTagName("LineString").item(0);
            Element polygon = (Element) placemark.getElementsByTagName("Polygon").item(0);
            Element linearRing = (Element) placemark.getElementsByTagName("LinearRing").item(0);
            Geometry geometry = null;

            System.out.println(id+" p:"+point+" ls:"+linearString+" p:"+polygon+" lr:"+linearRing);

            if (point != null){
                geometry = parseCoord(point);
            }else if (linearString != null){
                geometry = parseMultiCoord(linearString);
            }else if (polygon != null){
                Element outerBoundaryIs = (Element) polygon.getElementsByTagName("outerBoundaryIs").item(0);
                geometry = parseMultiCoord((Element) outerBoundaryIs.getElementsByTagName("LinearRing").item(0));
            }else if (linearRing != null){
                geometry = parseMultiCoord(linearRing);
            }

            System.out.println(id+" "+geometry);

            if (id != null && geometry != null){
                result.add(new KmlNode(id,geometry));
            }
        }

        return result;
    }

    private Geometry parseCoord(Element point){
        String[] coordString = point.getElementsByTagName("coordinates").item(0).getTextContent().split(",");
        Coordinate coord = new Coordinate(Double.valueOf(coordString[1]), Double.valueOf(coordString[0]));

        return new GeometryFactory().createPoint(coord);
    }

    private Geometry parseMultiCoord(Element multiCoord){
        String[] coordsString = multiCoord.getElementsByTagName("coordinates").item(0).getTextContent().split(" ");
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        for (int i = 0; i < coordsString.length; i++){
            if (coordsString[i].contains(",")){
                String[] coordString = coordsString[i].split(",");
                coords.add(new Coordinate(Double.valueOf(coordString[1]), Double.valueOf(coordString[0])));
            }
        }

        return new GeometryFactory().createMultiPoint(coords.toArray(new Coordinate[] {}));
    }

    private ArrayList<Node> addCircleToPoint(ArrayList<Node> nodes){
        for (Node node : nodes){

        }
        return null;
    }

    public static class SimpleErrorHandler implements ErrorHandler{
        public void warning(SAXParseException e) throws SAXException{
            System.out.println(e.getMessage());
        }
        public void error(SAXParseException e) throws SAXException{
            System.out.println(e.getMessage());
        }
        public void fatalError(SAXParseException e) throws SAXException{
            System.out.println(e.getMessage());
        }
    }
}
