//package eng.metarJava;
//
//import eng.metarJava.exception.NullArgumentException;
//import eng.metarJava.support.ObservedList;
//import java.util.List;
//
///**
// *
// * @author Marek Vajgl
// */
//public class TrendPhenomenaInfo2 extends ObservedList<PhenomenaInfo> {
//  private boolean NSW;
//  
//  /**
//   * Create new trend phenomena info for no-significant-weather setting.
//   * @return New instance of phenomenas trend info with NSW flag set.
//   */
//  public static TrendPhenomenaInfo2 createNSW(){
//    TrendPhenomenaInfo2 ret = new TrendPhenomenaInfo2(true, null);
//    return ret;
//  }
//  
//  /**
//   * Creates new trend phenomena info with selected phenomenas.
//   * @param phenomenas List of phenomenas to be set.
//   * @return New instance of phenomenas trend info.
//   */
//  public static TrendPhenomenaInfo2 create(List<PhenomenaInfo> phenomenas){
//    if (phenomenas == null)
//      throw new NullArgumentException("phenomenas");
//    if (phenomenas.isEmpty())
//      throw new IllegalArgumentException("[phenomenas] cannot be empty list.");
//    TrendPhenomenaInfo2 ret = new TrendPhenomenaInfo2(false, phenomenas);
//    return ret;
//  }
//  
//  private TrendPhenomenaInfo2(){
//    super.setAddObserver(new ObservedList.ListObserver() {
//      @Override
//      public void invoke(ObservedList list, Object element) {
//        phenomenaAdded(list, element);
//      }
//    });
//  }
//  
//  protected TrendPhenomenaInfo2(boolean isNSW, List<PhenomenaInfo> phenomenas) {
//    this();
//    if (isNSW){
//      this.NSW = true;
//    }else {
//      if (phenomenas == null)
//        throw new NullArgumentException("phenomenas");
//    this.NSW = false;
//    this.addAll(phenomenas);
//    }
//  }
//  
//  private void phenomenaAdded(ObservedList<PhenomenaInfo> list, PhenomenaInfo element){
//    if (NSW) NSW = false;
//  }
//
//  public boolean isNSW() {
//    return NSW;
//  }
//
//  public void setNSW(boolean isNSW) {
//    this.NSW = isNSW;
//    if (isNSW)
//      this.clear();
//  }
//  
//  
//  
//}
