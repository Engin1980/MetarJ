package eng.metarJava.decoders.exceptions;

import eng.metarJava.decoders.support.ReportField;

/**
 *
 * @author Marek Vajgl
 */
public class MissingFieldException extends ParseException {
  public MissingFieldException(ReportField field, String readyText, String processedText){
    super(field, "Missing mandatory field.", processedText, readyText, null);
  }
}
