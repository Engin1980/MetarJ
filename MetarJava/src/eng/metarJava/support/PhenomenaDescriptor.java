package eng.metarJava.support;

/**
 *
 * @author Marek Vajgl
 */
@Deprecated
public enum PhenomenaDescriptor {
  none,
  MI, BC, PR, DR, BL, SH, TS, FZ;
  
  public static TryResult<PhenomenaDescriptor> tryValueOf(String s){
    TryResult<PhenomenaDescriptor> ret;
    try{
      PhenomenaDescriptor tmp = PhenomenaDescriptor.valueOf(s);
      ret = TryResult.createSuccess(tmp);
    } catch (Exception ex){
      ret = TryResult.createFail();
    }
    return ret;
  }
}
