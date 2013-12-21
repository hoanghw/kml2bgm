package implementations;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import interfaces.Node;

import java.util.ArrayList;

public class DynamicNode implements Node {

    private String id;
    private Coordinate coor;
    private ArrayList<Coordinate> coorList;
    private Geometry geometry;

    public DynamicNode(String id, Coordinate c){
        this.id = id;
        this.coor = c;
        geometry = new GeometryFactory().createPoint(c);
    }

    public DynamicNode(String id, ArrayList<Coordinate> c){
        this.id = id;
        this.coorList = c;
        geometry = new GeometryFactory().createMultiPoint(c.toArray(new Coordinate[] {}));
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public void setArity(int arity) {

    }

    @Override
    public Geometry getGeometry() {
        return this.geometry;
    }

    @Override
    public void setGeometry(Geometry g){
        this.geometry = g;
    }

    @Override
    public boolean isInside(Node n) {
        IntersectionMatrix iMatrix = this.geometry.convexHull().relate(n.getGeometry().convexHull());
        return iMatrix.isWithin();
    }

    @Override
    public boolean isContains(Node n) {
        IntersectionMatrix iMatrix = this.geometry.convexHull().relate(n.getGeometry().convexHull());
        return iMatrix.isContains();
    }

    @Override
    public boolean isRelated(Node n) {
        IntersectionMatrix iMatrix = this.geometry.convexHull().relate(n.getGeometry().convexHull());
        return !iMatrix.isDisjoint();
    }

    @Override
    public String toString(){
        return id;
    }
}
