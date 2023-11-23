package Receiver.ContentTree.Visitor;

import Receiver.ContentTree.Node.TreeNode;

public class TreeVisitor {
    //    ├ alt+195 └─── └ ─ alt+192 & 196
    public void visit(TreeNode node){
        StringBuilder indent = new StringBuilder();
        indent.append("    ".repeat(Math.max(0, node.getLevel()-2)));
        if(node.getParent() !=null){
//            indent.append((node.getParent().getIsLast()) ? "    " : "|   ");
            indent.append("    ");
        }
        indent.append(node.getIsLast() ? "└── " : "├── ");
        System.out.println(indent + node.getText());
    }
}
