package eng.metarJava.support;

import eng.metarJava.exceptions.NullArgumentException;

/**
 * Represents variation of something.
 * @author Marek Vajgl
 */
public class Variation<T> {
  private final T from;
  private final T to;
  
  /**
   * Creates new variation. Neither lower nor upper bound can be null.
   * @param from Lower bound, cannot be null.
   * @param to Upper bound, cannot be null.
   */
  public Variation(T from, T to){
    if (from == null) throw new NullArgumentException("from");
    if (to == null) throw new NullArgumentException("to");
    
    this.from = from;
    this.to = to;
  }

  /**
   * Gets lower bound of variation.
   * @return 
   */
  public T getFrom() {
    return from;
  }

  /**
   * Gets upper bound of variation.
   * @return 
   */
  public T getTo() {
    return to;
  }
  
  @Override
  public String toString(){
    return "[" + this.getFrom() + " .. " + this.getTo() + "]";
  }
}
