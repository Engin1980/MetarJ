package eng.metarJava.exception;

/**
 * Raised when null value is passed into the function as a parameter.
 * @author Marek Vajgl
 */
public class NullArgumentException extends IllegalArgumentException {
  
  /**
   * Creates a new instance of exception. Argument name is required.
   * @param argumentName name of argument which value is null.
   */
  public NullArgumentException(String argumentName){
    super("Value of argument [" + argumentName + "] cannot be null.");
  }
}
