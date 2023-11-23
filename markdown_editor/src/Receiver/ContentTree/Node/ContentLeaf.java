package Receiver.ContentTree.Node;

import Receiver.ContentTree.Visitor.TreeVisitor;

public class ContentLeaf extends TreeNode {

    public ContentLeaf(String text, int level){
        super(text,level);
        this.isLeafNode = true;
    }
    public void accept(TreeVisitor visitor){
        visitor.visit(this);
    }

}
