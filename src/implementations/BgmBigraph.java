package implementations;

import Utils.PlaceTree;
import com.vividsolutions.jts.geom.Coordinate;
import interfaces.Bigraph;
import interfaces.Node;

import java.io.*;
import java.util.ArrayList;

public class BgmBigraph implements Bigraph {
    ArrayList<Node> nodes;
    String reactionRules;
    File file;
    PlaceTree<Node> placeTree;

    public BgmBigraph(String name){
        nodes = new ArrayList<Node>();
        reactionRules=" ";
        file = new File(name);
        placeTree = newPlaceTree();

    }

    private PlaceTree<Node> newPlaceTree(){
        ArrayList<Coordinate> worldCoords = new ArrayList<Coordinate>();
        worldCoords.add(new Coordinate(-180,-180,0));
        worldCoords.add(new Coordinate(180,-180,0));
        worldCoords.add(new Coordinate(180,180,0));
        worldCoords.add(new Coordinate(-180,180,0));
        Node world = new DynamicNode("WORLD",worldCoords);
        return new PlaceTree<Node>(world);
    }

    private PlaceTree<Node> addNodesToPlaceTree(ArrayList<Node> nodes, PlaceTree<Node> tree){
        for (Node n : nodes){
            addNodeToPlaceTree(n,tree);
        }
        return tree;
    }

    private PlaceTree<Node> addNodeToPlaceTree(Node node, PlaceTree<Node> tree){
        ArrayList<PlaceTree<Node>> children = new ArrayList<PlaceTree<Node>>();
        PlaceTree <Node> newTree = new PlaceTree<Node>(node);

        for (PlaceTree<Node> t : tree.getChildren()){
            if (node.isInside(t.getData())){
                addNodeToPlaceTree(node, t);
                return tree;
            }
            if (node.isContains(t.getData())){
                children.add(t);
            }
        }

        for (PlaceTree<Node> child : children){
            tree.getChildren().remove(child);
            newTree.addTree(child);
        }

        tree.addTree(newTree);

        return tree;
    }

    @Override
    public String getType() {
        return "BigMC";
    }

    @Override
    public boolean addNode(Node n) {
        return nodes.add(n);
    }

    @Override
    public boolean addNodes(ArrayList <Node> n) {
        return nodes.addAll(n);
    }

    @Override
    public boolean addReactionRule(String s) {
        reactionRules +=  s;
        return true;
    }

    @Override
    public PlaceTree<Node> getPlaceTree() {
        placeTree = newPlaceTree();
        addNodesToPlaceTree(nodes, placeTree);
        return placeTree;
    }

    @Override
    public File toFile() {
        try {
            if (!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));

            for (Node n : nodes){
                out.println("%active "+n.getID()+" :"+n.getArity()+";");
            }

            out.println();
            getPlaceTree();
            out.println(placeTreeToString());

            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        return file;
    }

    @Override
    public void setBigraph(File f) {
        this.file = f;
    }

    @Override
    public File getBigraph() {
        return file;
    }

    @Override
    public void visualizeBigraph() {

    }

    public String placeTreeToString(){
        StringBuilder sb = new StringBuilder(placeTreeToString(placeTree));
        sb.delete(0,7);
        sb.deleteCharAt(sb.length()-1);
        sb.append(";");
        return sb.toString();
    }

    private String placeTreeToString(PlaceTree<Node> tree){
        if (tree.getChildren().isEmpty()){
            return tree.getData().toString();
        }

        String result = tree.getData().toString() + ".(";
        for (PlaceTree<Node> t : tree.getChildren()){
            result += placeTreeToString(t) + "|";
        }
        result = result.substring(0, result.length() - 1);
        result += ")";
        return result;
    }
}
