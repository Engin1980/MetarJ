package eng.metarJava.exception;

/**
 *
 * @author Marek Vajgl
 */
public class NonsenseRequestException extends UnsupportedOperationException {

  public NonsenseRequestException(String message) {
    super(message);
  }
  
}
