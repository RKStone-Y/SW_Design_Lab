package Receiver.ContentTree.Node;

import Receiver.ContentTree.Visitor.TreeVisitor;

import java.util.ArrayList;
import java.util.List;

public class HeadingNode extends TreeNode {
    protected List<TreeNode> children = new ArrayList<>();
    public String getText(){
        return text;
    }
    public int getLevel(){
        return level;
    }
    public HeadingNode(String text, int level) {
        super(text,level);
        this.isLeafNode = false;
    }

    public void add(TreeNode component) {
        children.add(component);
    }
    public void accept(TreeVisitor visitor){
        boolean isLast ;
        int numChildren = this.children.size();
        visitor.visit(this);
        for (int i = 0; i < numChildren; i++) {
            TreeNode child = this.children.get(i);
            isLast = (i == numChildren-1);
            child.setLast(isLast);
            child.accept(visitor);
        }
    }
}
