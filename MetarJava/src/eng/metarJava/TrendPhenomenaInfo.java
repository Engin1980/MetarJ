package eng.metarJava;

import eng.metarJava.exception.NonsenseRequestException;
import eng.metarJava.exception.NullArgumentException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class TrendPhenomenaInfo {
  private final boolean NSW;
  private final List<PhenomenaInfo> phenomenas;

  protected TrendPhenomenaInfo(boolean isNSW, List<PhenomenaInfo> phenomenas) {
    if (isNSW){
      this.NSW = true;
      this.phenomenas = null;
    }else {
      if (phenomenas == null)
        throw new NullArgumentException("[phenomenas]");
      if (phenomenas.isEmpty()){
        throw new IllegalArgumentException("[phenomenas] should not be empty.");
      }
    this.NSW = false;
    this.phenomenas = phenomenas;
    }
  }
  
  public static TrendPhenomenaInfo createNSW(){
    TrendPhenomenaInfo ret = new TrendPhenomenaInfo(true, null);
    return ret;
  }
  public static TrendPhenomenaInfo create(List<PhenomenaInfo> phenomenas){
    if (phenomenas == null)
      throw new NullArgumentException("[phenomenas]");
    if (phenomenas.isEmpty())
      throw new IllegalArgumentException("[phenomenas] cannot be empty list.");
    TrendPhenomenaInfo ret = new TrendPhenomenaInfo(false, phenomenas);
    return ret;
  }

  public boolean isNSW() {
    return NSW;
  }

  public List<PhenomenaInfo> getPhenomenas() {
    if (isNSW())
      throw new NonsenseRequestException("No phenomenas available for NSW (no-significant-weather) settings.");
    return phenomenas;
  }
}
