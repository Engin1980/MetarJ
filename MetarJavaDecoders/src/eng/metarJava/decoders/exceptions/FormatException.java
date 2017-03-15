package eng.metarJava.decoders.exceptions;

import eng.metarJava.decoders.fields.ReportField;

/**
 *
 * @author Marek Vajgl
 */
public class FormatException extends RuntimeException {
  
  public enum ErrorType{
    IsNull,
    IsInvalid,
    Other
  }
  
  private final ReportField reportField;
  private final ErrorType errorType;

  public ReportField getReportField() {
    return reportField;
  }

  public FormatException(ReportField reportField,  ErrorType errorType,
          String message, 
          Exception cause) {
    super("Illegal report state at " + reportField.toString(), cause);
    this.reportField = reportField;
    this.errorType = errorType;
  }
  
  public FormatException(ReportField reportField,  ErrorType errorType,
          String message) {
    super("Illegal report state at " + reportField.toString());
    this.reportField = reportField;
    this.errorType = errorType;
  }
}
