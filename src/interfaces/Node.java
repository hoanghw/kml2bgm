package interfaces;

import com.vividsolutions.jts.geom.Geometry;

public interface Node {
    String getID();
    void setID(String id);
    int getArity();
    void setArity(int arity);
    Geometry getGeometry();
    void setGeometry(Geometry g);
    boolean isInside(Node n);
    boolean isContains(Node n);
    boolean isRelated(Node n);
}
