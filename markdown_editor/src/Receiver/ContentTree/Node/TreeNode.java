package Receiver.ContentTree.Node;

import Receiver.ContentTree.Visitor.TreeVisitor;

public abstract class TreeNode {
    protected TreeNode parent;
    public boolean isLeafNode;
    protected int level;

    protected String text;
    protected boolean isLast;
    public TreeNode(String text, int level){
        this.text = text;
        this.level = level;
        this.isLast = false;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
    public boolean getIsLast(){
        return isLast;
    }

    public String getText(){
        return text;
    }
    public int getLevel(){
        return level;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void accept(TreeVisitor visitor){}

    public void add(TreeNode component) {
    }
}
