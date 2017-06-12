/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.objectTreeBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class ObjectTreeBuilder {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    int i = 5;
    Double d = null;

    List<Integer> ints = new ArrayList<Integer>();
    ints.add(5);
    ints.add(2);
    ints.add(9);

    double[] doubles = new double[]{1, 2, 3, 2, 1};

    A a = new A();

    TreeNode<ItemInfo> root = TreeFactory.build(a);

    printTree(root);
  }

  private static void printTree(TreeNode<ItemInfo> root) {
    printNode(root, 0);
  }

  private static void printNode(TreeNode<ItemInfo> root, int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("\t");
    }
    System.out.println(root.getValue());
    for (TreeNode<ItemInfo> child : root.getChildren()) {
      printNode(child, level + 1);
    }
  }

}

class A {

  private int elementX = 1;
  private Double elementY = null;
  private float elementZ = 5.3f;
  private List<Integer> ints = new ArrayList<Integer>();
  private String greeting = "nazdar";
  private java.util.Date date = new java.util.Date();
  private eE enuma = eE.jedna;

  public A() {
    ints.add(5);
    ints.add(2);
    ints.add(9);
  }

  @DisplayLabel(label = "Element X", orderIndex = -2)
  public int getElementX() {
    return elementX;
  }

  @DisplayLabel(label = "Element Y", orderIndex = -1)
  public Double getElementY() {
    return elementY;
  }

  public float getElementZ() {
    return elementZ;
  }

  public List<Integer> getInts() {
    return ints;
  }

  public Date getDate() {
    return date;
  }

  public String getGreeting() {
    return greeting;
  }

  public eE getEnuma() {
    return enuma;
  }
  
  

}

enum eE {
  jedna,
  dva,
  tri
}
