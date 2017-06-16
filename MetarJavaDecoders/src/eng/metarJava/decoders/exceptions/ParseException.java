package eng.metarJava.decoders.exceptions;

import eng.metarJava.decoders.support.ReportField;

/**
 *
 * @author Marek Vajgl
 */
public class ParseException extends RuntimeException {
  
  private final ReportField reportField;
  private final String processedText;
  private final String unprocessedText;

  public ReportField getReportField() {
    return reportField;
  }

  public String getProcessedText() {
    return processedText;
  }

  public String getUnprocessedText() {
    return unprocessedText;
  }
  
  public ParseException(ReportField reportField, 
          String message, 
          String processedText,
          String unprocessedText,
          Exception cause) {
    super("Failed to parse " + reportField.toString(), cause);
    this.reportField = reportField;
    this.processedText = processedText;
    this.unprocessedText = unprocessedText;
  }
}
