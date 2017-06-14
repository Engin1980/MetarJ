package eng.metarJava.decoders.support;

/**
 *
 * @author Marek Vajgl
 */
public class TemperatureAndDewPoint {
  public final int temperature;
  public final int dewPoint;

  public TemperatureAndDewPoint(int temperature, int dewPoint) {
    this.temperature = temperature;
    this.dewPoint = dewPoint;
  }
}
