package eng.metarJava.support;

/**
 *
 * @author Marek Vajgl
 */
public enum PhenomenaType {
  none,
  MI, BC, PR, DR, BL, SH, TS, FZ, // descriptors
  DZ, RA, SN, SG, IC, PL, GR, GS, UP, //precipitations
  BR, FG, FU, VA, DU, SA, HZ, // obscurations
  PO, SQ, FC, SS, DS; // others
  
    public static TryResult<PhenomenaType> tryValueOf(String s){
    TryResult<PhenomenaType> ret;
    try{
      PhenomenaType tmp = PhenomenaType.valueOf(s);
      ret = TryResult.createSuccess(tmp);
    } catch (Exception ex){
      ret = TryResult.createFail();
    }
    return ret;
  }
}
