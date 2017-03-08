package eng.metarJava.exception;

/**
 * Raised when null value is passed into the function as a parameter.
 * @author Marek Vajgl
 */
public class NullArgumentException extends IllegalArgumentException {
  public NullArgumentException(String argumentName){
    super("Value of argument \"" + argumentName + "\" cannot be null.");
  }
}
