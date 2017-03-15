package eng.metarJava.decoders;

import eng.metarJava.Report;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public interface Formatter {
  public String format(Report report);
  public List<Exception> checkForErrors(Report report);
}
