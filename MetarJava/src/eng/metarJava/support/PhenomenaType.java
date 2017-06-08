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
    
    public boolean isPrecipitation(){
      switch(this){
        case DZ:
        case RA:
        case SN:
        case SG:
        case IC:
        case PL:
        case GR:
        case GS:
        case UP:
          return true;
        default:
          return false;
      }
    }
    
    public boolean isDescription(){
      switch(this){
        case MI:
        case BR:
        case PR:
        case DR:
        case BL:
        case SH:
        case TS:
        case FZ:
          return true;
        default:
          return false;
      }
    }
    
    public boolean isObscuration(){
      switch(this){
        case BR:
        case FG:
        case FU:
        case VA:
        case DU:
        case SA:
        case HZ:
          return true;
        default:
          return false;
      }
    }
    
    public boolean isOther(){
      switch(this){
        case PO:
        case SQ:
        case FC:
        case SS:
        case DS:
          return true;
        default:
          return false;
      }
    }
}
