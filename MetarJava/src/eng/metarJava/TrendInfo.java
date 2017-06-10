package eng.metarJava;

import eng.metarJava.exception.NonsenseRequestException;
import eng.metarJava.exception.NullArgumentException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class TrendInfo {

  private final boolean noSignificantChange;
  private final TrendReport[] trends;

  /**
   * Creates new trend-info as NOSIG - no significant change trend.
   *
   * @return
   */
  public static TrendInfo createNOSIG() {
    TrendInfo ret = new TrendInfo(true, null);
    return ret;
  }

  /**
   * Creates new trend-info with trend report.
   *
   * @param trend Trend report message
   * @return
   */
  public static TrendInfo create(TrendReport trend) {
    TrendInfo ret = create(new TrendReport[]{trend});
    return ret;
  }

  /**
   * Creates new trend-info with trend reports. Set of trends is fixed, non-empty, and cannot be later changed.
   *
   * @param trends non-empty array of trends
   * @return
   */
  public static TrendInfo create(TrendReport[] trends) {
    TrendInfo ret = new TrendInfo(false, trends);
    return ret;
  }

  /**
   * Creates new trend-info with trend reports. Set of trends is fixed, non-empty and cannot be later changed.
   *
   * @param trends non-empty list of trends
   * @return
   */
  public static TrendInfo create(List<TrendReport> trends) {
    TrendReport[] arr = trends.toArray(new TrendReport[0]);
    TrendInfo ret = create(arr);
    return ret;
  }

  /**
   * Creates new instance of TrendInfo object. Only for internal use. For public access, see {@linkplain #create(java.util.List) }
   * or {@linkplain #create(eng.metarJava.TrendReport[]) } methods to create a new instance.
   *
   * @param isNosig
   * @param trends
   * @throws IllegalArgumentException if [isNosig] is true and [trends] is not null
   * @throws IllegalArgumentException if [trends] is not null but contains no elements
   */
  protected TrendInfo(boolean isNosig, TrendReport[] trends) {
    if (isNosig && trends != null) {
      throw new IllegalArgumentException("[trends] must be null if [isNosig] flag is set.");
    }
    if (trends != null && trends.length == 0) {
      throw new IllegalArgumentException("If [trends] are set, they cannot be empty.");
    }
    this.noSignificantChange = isNosig;
    if (trends == null) {
      this.trends = null;
    } else {
      this.trends = Arrays.copyOf(trends, trends.length);
    }
  }

  /**
   * Returns if trend is NOSIG - no significant change. False otherwise. Mandatory. Readonly.
   *
   * @return
   */
  public boolean isIsNoSignificantChange() {
    return noSignificantChange;
  }

  /**
   * Return trends as copy of array. Array is read-only.
   *
   * @return
   */
  public TrendReport[] getTrends() {
    if (this.trends == null) {
      return null;
    }

    TrendReport[] ret = Arrays.copyOf(this.trends, this.trends.length);
    return ret;
  }
}
