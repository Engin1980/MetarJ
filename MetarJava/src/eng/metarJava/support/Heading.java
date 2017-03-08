package eng.metarJava.support;

/**
 *
 * @author Marek Vajgl
 */
public class Heading {

  private final int value;

  public Heading(int value) {
    if (value < 0) {
      value = (int) Math.abs(value);
      value = value % 360;
      value = 360 - value;
    } else if (value > 360) {
      value = value % 360;
    }
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
