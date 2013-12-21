package implementations;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import interfaces.Node;

public class KmlNode implements Node {
    private String id;
    private Geometry geometry;
    private int arity = 1;

    public KmlNode(String id, Geometry geometry){
        this.id = id;
        this.geometry = geometry;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public int getArity() {
        return this.arity;
    }

    @Override
    public void setArity(int arity) {
        this.arity = arity;
    }

    @Override
    public Geometry getGeometry() {
        return geometry;
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
        //return !(isContains(n)&&isInside(n));
    }

    @Override
    public String toString(){
        return id;
    }
}
