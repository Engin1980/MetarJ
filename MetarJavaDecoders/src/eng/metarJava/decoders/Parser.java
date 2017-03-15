package eng.metarJava.decoders;

import eng.metarJava.Report;

/**
 *
 * @author Marek Vajgl
 */
public interface Parser {
  public Report parse(String line);
}
