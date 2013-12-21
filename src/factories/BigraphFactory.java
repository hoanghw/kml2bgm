package factories;

import Utils.PlaceTree;
import implementations.BgmBigraph;
import interfaces.Bigraph;
import interfaces.Node;

import java.io.File;
import java.util.ArrayList;

public class BigraphFactory implements Bigraph {
    Bigraph bigraph;
    String type;


    public BigraphFactory(String name, String type){
        this.type = type;
        if (type.equals("BigMC")){
            bigraph = new BgmBigraph(name);
        }
    }

    public void construct(){

    }

    public int verifyNodes(ArrayList<Node> nodes){
        return 0;
    }

    @Override
    public String getType() {
        return bigraph.getType();
    }

    @Override
    public boolean addNode(Node n) {
        return bigraph.addNode(n);
    }

    @Override
    public boolean addNodes(ArrayList<Node> n) {
        return bigraph.addNodes(n);
    }

    @Override
    public boolean addReactionRule(String s) {
        return bigraph.addReactionRule(s);
    }

    @Override
    public PlaceTree<Node> getPlaceTree() {
        return bigraph.getPlaceTree();
    }

    @Override
    public File toFile() {
        return bigraph.toFile();
    }

    @Override
    public void setBigraph(File f) {
        bigraph.setBigraph(f);
    }

    @Override
    public File getBigraph() {
        return bigraph.getBigraph();
    }

    @Override
    public void visualizeBigraph() {
        bigraph.visualizeBigraph();
    }
}
