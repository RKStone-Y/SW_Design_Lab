package Receiver.ContentTree;

import Receiver.ContentTree.Node.TreeNode;
import Receiver.ContentTree.Visitor.TreeVisitor;

import java.util.ArrayList;
import java.util.List;

public class MarkdownFileTree {
    public List<TreeNode> components = new ArrayList<>();

    public void addComponent(TreeNode component) {
        components.add(component);
    }

    public void accept(TreeVisitor visitor) {
        boolean isLast;
        int numComponents=components.size();
        for(int i = 0; i < numComponents; i++ ){
            TreeNode component = components.get(i);
            isLast = (i == numComponents - 1);
            component.setLast(isLast);
            component.accept(visitor);
        }
    }

}
