package eng.metarJava.support;

/**
 *
 * @author Marek Vajgl
 */
public class TryResult<T> {
  private final T value;
  private final boolean success;

  private TryResult(T value, boolean success) {
    this.value = value;
    this.success = success;
  }
  
  public static <T> TryResult<T> createSuccess(T value){
    TryResult<T> ret = new TryResult<>(value, true);
    return ret;
  }
  
  public static <T> TryResult<T> createFail(){
    TryResult<T> ret = new TryResult<>(null, false);
    return ret;
  }

  public T getValue() {
    return value;
  }

  public boolean isSuccess() {
    return success;
  }
}
