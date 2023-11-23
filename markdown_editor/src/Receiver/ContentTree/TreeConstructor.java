package Receiver.ContentTree;

import Receiver.ContentTree.Node.ContentLeaf;
import Receiver.ContentTree.Node.HeadingNode;
import Receiver.ContentTree.Node.TreeNode;

import java.util.List;

public class TreeConstructor {
    public MarkdownFileTree parseMarkdownFile(List<String> file_content) {
        MarkdownFileTree markdown_file_tree = new MarkdownFileTree();
        TreeNode currentComponent = null;
        int currentLevel = 0;
        int first_level = 0;
        boolean first_flag = true;
        for(String line: file_content)  {

            if (line.startsWith("#")) {
                int level = countHeadingLevel(line);
                if(first_flag){
                    first_level = level;
                    first_flag =false;
                }
                String text = line.substring(level).trim();
                HeadingNode heading_node = new HeadingNode(text, level);
                if(currentComponent != null && level !=currentLevel) {
                    currentComponent.setLast(true);
                }
                if (currentComponent != null && (level != first_level)) {
                    while (level <= currentLevel) {
                        currentComponent = currentComponent.getParent();
                        currentLevel--;
                    }
                    currentComponent.add(heading_node);
                    heading_node.setParent(currentComponent);
                } else {
                    markdown_file_tree.addComponent(heading_node);
                }
                currentComponent = heading_node;
                currentLevel = level;

            } else {
                if (currentComponent != null) {
                    if(currentComponent.isLeafNode){
                        currentComponent = currentComponent.getParent();
                    }
                    ContentLeaf content = new ContentLeaf(line.replaceFirst("\\* ","·"), currentComponent.getLevel()+1);
                    content.setParent(currentComponent);
                    currentComponent.add(content);
                    currentComponent = content;
                    currentLevel = currentComponent.getLevel();
                }
            }

        }
        if(currentComponent != null) {
            currentComponent.setLast(true);
        }
        return markdown_file_tree;
    }

    public static int countHeadingLevel(String line) {
        int level = 0;
        while (level < line.length() && line.charAt(level) == '#') {
            level++;
        }
        return level;
    }
}
