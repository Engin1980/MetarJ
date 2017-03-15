package eng.metarJava;

/**
 * Represents state of one runway.
 * @author Marek Vajgl
 */
public class RunwayState {
  private final String designator;
  private final char deposit;
  private final char contamination;
  private final String depositDepth;
  private final String brakingAction;
  
  public static RunwayState create(String designator, char deposit, char contamination, String depositDepth, String brakingAction){
    RunwayState ret = new RunwayState(designator, deposit, contamination, depositDepth, brakingAction);
    return ret;
  }

  protected RunwayState(String designator, char deposit, char contamination, String depositDepth, String brakingAction) {
    this.designator = designator;
    this.deposit = deposit;
    this.contamination = contamination;
    this.depositDepth = depositDepth;
    this.brakingAction = brakingAction;
  }

  public String getDesignator() {
    return designator;
  }

  public char getDeposit() {
    return deposit;
  }

  public char getContamination() {
    return contamination;
  }

  public String getDepositDepth() {
    return depositDepth;
  }

  public String getBrakingAction() {
    return brakingAction;
  }
  
}
