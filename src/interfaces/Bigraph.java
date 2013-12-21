package interfaces;

import Utils.PlaceTree;

import java.io.File;
import java.util.ArrayList;

public interface Bigraph {
    String getType();
    boolean addNode(Node n);
    boolean addNodes(ArrayList<Node> n);
    boolean addReactionRule(String s);
    PlaceTree<Node> getPlaceTree();
    File toFile();
    void setBigraph(File f);
    File getBigraph();
    void visualizeBigraph();
}
