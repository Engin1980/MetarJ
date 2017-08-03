/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.objectTreeBuilder;

import eng.objectTreeBuilder.attributes.DisplayLabel;
import eng.objectTreeBuilder.attributes.DisplayLabelIndex;
import eng.objectTreeBuilder.attributes.DisplayValueFromString;
import java.time.LocalDateTime;
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
    
    Object o = new Value();
    boolean res = o.getClass().isAnnotationPresent(DisplayValueFromString.class);
    System.out.println(res);

//    if (true) {
//      return;
//    }
    int i = 5;
    Double d = null;
    
    List<Integer> ints = new ArrayList<Integer>();
    ints.add(5);
    ints.add(2);
    ints.add(9);
    
    double[] doubles = new double[]{1, 2, 3, 2, 1};
    
    A a = new A();
    
    TreePropertyFactory.registerSpecificTypeDecoders(D.class);
    TreePropertyFactory.registerSpecificTypeDecoders(EEE.class);
    
    TreeNode<ItemInfo> root = TreePropertyFactory.build(a);
    
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
  private Value val = new Value();
  private java.time.LocalDateTime now = java.time.LocalDateTime.now();
  private boolean funny = true;
  
  private EEE inherited = new EEE();
  
  public boolean isFunny() {
    return funny;
  }
  
  public A() {
    ints.add(5);
    ints.add(2);
    ints.add(9);
  }
  
  @DisplayLabel("Element X")
  @DisplayLabelIndex(-2)
  public int getElementX() {
    return elementX;
  }
  
  @DisplayLabel("Element Y")
  @DisplayLabelIndex(-1)
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
  
  public Value getVal() {
    return val;
  }
  
  @DisplayLabel("Aktuální čas")
  @DisplayLabelIndex(5)
  public LocalDateTime getNow() {
    return now;
  }
  
  public EEE getInherited() {
    return inherited;
  }
  
}

enum eE {
  jedna,
  dva,
  tri
}

@DisplayValueFromString
class Value {
  
  private int value = 5;
  
  public int getValue() {
    return value;
  }
  
  @Override
  public String toString() {
    return "Value{" + "value=" + value + '}';
  }
  
}

class D {
  
  private static String getSpecificToString(java.time.LocalDateTime ldt) {
    return "neneneeeeee";
  }
}

class E {

  private int eItemFirst = 5;
  
  public int geteItemFirst() {
    return eItemFirst;
  }
}

class EEE extends E {

  private int eItemSecond = 10;
  
  public int geteItemSecond() {
    return eItemSecond;
  }
}
