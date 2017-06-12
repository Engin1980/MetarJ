/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.objectTreeBuilder;

/**
 *
 * @author Marek Vajgl
 */
public class ItemInfo {
  private String label;
  private String value;
  private String type;

  public ItemInfo(String label, String value, String type) {
    this.label = label;
    this.value = value;
    this.type = type;
  }

  public String getLabel() {
    return label;
  }

  public String getValue() {
    return value;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return this.label + " : " + this.value + " [" + this.type + "]";
  }
  
}
