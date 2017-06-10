package eng.metarJava;

import eng.metarJava.exception.NonsenseRequestException;
import eng.metarJava.exception.NullArgumentException;
import java.util.ArrayList;
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
    this.NSW = false;
    this.phenomenas = phenomenas;
    }
  }
  
  /**
   * Create new trend phenomena info for no-significant-weather setting.
   * @return New instance of phenomenas trend info with NSW flag set.
   */
  public static TrendPhenomenaInfo createNSW(){
    TrendPhenomenaInfo ret = new TrendPhenomenaInfo(true, null);
    return ret;
  }
  
  /**
   * Creates new trend phenomena info with selected phenomenas.
   * @param phenomenas Phenomenas to be set.
   * @return New instance of phenomenas trend info.
   */
  public static TrendPhenomenaInfo create(List<PhenomenaInfo> phenomenas){
    if (phenomenas == null)
      throw new NullArgumentException("[phenomenas]");
    if (phenomenas.isEmpty())
      throw new IllegalArgumentException("[phenomenas] cannot be empty list.");
    TrendPhenomenaInfo ret = new TrendPhenomenaInfo(false, phenomenas);
    return ret;
  }
  
  /**
   * To create new instance for trend, when neither NSW nor some phenomena is reported.
   * @return New instance of phenomenas trend info.
   */
  public static TrendPhenomenaInfo createEmpty(){
    TrendPhenomenaInfo ret = new TrendPhenomenaInfo(false, new ArrayList<PhenomenaInfo>());
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

  /**
   * Returns true if phenomena-info has no flag and phenomenas collection is empty.
   * @return 
   */
  public boolean isEmpty() {
    return !isNSW() && (
            phenomenas == null || phenomenas.isEmpty());
  }
}
