import factories.BigraphFactory;
import factories.NodeFactory;
import interfaces.Node;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        File f = new File("report.kml");
        NodeFactory nodeFactory = new NodeFactory(f);
        BigraphFactory bigraphFactory = new BigraphFactory("report.bgm", "BigMC");
        try {
            ArrayList<Node> nodes = nodeFactory.getNodes();
            System.out.println(nodes);
            bigraphFactory.addNodes(nodes);
        } catch (Exception e) {
            System.out.println(e);
        }
        bigraphFactory.toFile();
    }
}
