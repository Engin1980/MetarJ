/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.objectTreeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class TreeNode<T> {
  private TreeNode<T> parent = null;
  private final List<TreeNode<T>> children = new ArrayList();
  private final T value;
  
  public TreeNode(T value){
    this.value = value;
  }

  public TreeNode<T> getParent() {
    return parent;
  }

  public List<TreeNode<T>> getChildren() {
    return children;
  }

  public T getValue() {
    return value;
  }
  
  public static <T> void join(TreeNode<T> parent, TreeNode<T> children){
    if (children.parent != null)
      throw new IllegalArgumentException("Children [" + children + "] has already a parent [" + children.parent + "].");
    
    children.parent = parent;
    parent.children.add(children);
  }
  
  public static <T> void separate(TreeNode<T> parent, TreeNode<T> children){
    if (parent.children.contains(children) == false){
      throw new IllegalArgumentException("Children [" + children + "] is not a child of parent [" + parent + "], so cannot be separated.");
    }
    if (children.parent != parent){
      throw new IllegalArgumentException("Children [" + children + "] cannot be separated from parent [" + parent + "] because its parent is [" + children.parent + "]");
    }
    
    parent.children.remove(children);
    children.parent = null;
  }

  @Override
  public String toString() {
    return "[Node] " + value;
  }
  
  
}
