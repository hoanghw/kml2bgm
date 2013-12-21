package Utils;

import java.util.ArrayList;
import java.util.List;

public class PlaceTree<T> {

    T data;
    PlaceTree<T> parent;
    List<PlaceTree<T>> children;

    public PlaceTree(T data) {
        this.data = data;
        this.children = new ArrayList<PlaceTree<T>>();
        this.parent = null;
    }

    public PlaceTree<T> addChild(T child) {
        PlaceTree<T> childNode = new PlaceTree<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public PlaceTree<T> addChildToNode(T child, T node){
        PlaceTree<T> tree = getSubTree(node);
        tree.addChild(child);
        return tree;
    }

    public PlaceTree<T> getSubTree(T data){
        if (this.data.toString().equals(data.toString())){
            return this;
        }
        for (PlaceTree<T> t : this.children){
            return t.getSubTree(data);
        }
        return null;
    }

    public PlaceTree<T> addTree(PlaceTree<T> childTree){
        childTree.parent = this;
        this.children.add(childTree);
        return childTree;
    }

    public T getData(){
        return data;
    }
    public PlaceTree<T> getParent(){
        return parent;
    }

    public List<PlaceTree<T>> getChildren(){
        return children;
    }

    public String toString(){
        if (this.children.isEmpty()){
            return this.data.toString();
        }

        String result = this.data.toString() + ".(";
        for (PlaceTree<T> t : this.children){
            result += t.toString() + "|";
        }
        result = result.substring(0, result.length() - 1);
        result += ");";
        return result;
    }
}
