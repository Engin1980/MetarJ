package eng.metarJava.exceptions;

/**
 * Exception used when a method (typically getXXX()) function is called when it is a nonsence to do so,
 * typically due to object internal state. For example, if CoudInfo is in CAVOK, it is a nonsece to get
 * defined visibility.
 * @author Marek Vajgl
 */
public class NonsenseRequestException extends UnsupportedOperationException {

  public NonsenseRequestException(String message) {
    super(message);
  }
  
}
