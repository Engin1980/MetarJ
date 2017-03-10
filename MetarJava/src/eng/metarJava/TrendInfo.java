package eng.metarJava;

import eng.metarJava.exception.NullArgumentException;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class TrendInfo {
  private final boolean isNoSignificantChange;
  // TODO zvazit nahrazeni listu vlastni implementaci Read-Only listu
  // protoze tady nedokazu pohlidat, jestli mi to pak nekdo nesmaze
  private final List<TrendReport> trends;
  

  public TrendInfo(boolean isNoSignificantChange) {
    if (!isNoSignificantChange){
      throw new IllegalArgumentException("[isNoSignificantChange] must be true. Use different constructor otherwise.");
    }
    this.isNoSignificantChange = isNoSignificantChange;
    this.trends = null;
  }

  public TrendInfo(List<TrendReport> trends) {
    if (trends == null)
      throw new NullArgumentException("[trends] cannot be null. Use different constructor instead.");
    if (trends.isEmpty()){
      throw new IllegalArgumentException("[trens] cannot be empty. If so, use different constructor instead.");
    }
    this.trends = trends;
    this.isNoSignificantChange = false;
  }

  public boolean isIsNoSignificantChange() {
    return isNoSignificantChange;
  }

  public List<TrendReport> getTrends() {
    return trends;
  }
}
