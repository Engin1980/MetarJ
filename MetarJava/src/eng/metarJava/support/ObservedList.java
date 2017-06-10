//package eng.metarJava.support;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// *
// * @author Marek Vajgl
// */
//public class ObservedList<T> implements Iterable<T> {
//
//  public abstract class ListObserver<T> {
//
//    public abstract void invoke(ObservedList<T> list, T element);
//  }
//
//  private List<T> inner = new ArrayList();
//  private ListObserver<T> addObserver = null;
//  private ListObserver<T> removeObserver = null;
//
//  protected void setAddObserver(ListObserver addObserver) {
//    this.addObserver = addObserver;
//  }
//
//  protected void setRemoveObserver(ListObserver removeObserver) {
//    this.removeObserver = removeObserver;
//  }
//  
//  public void addAll(Iterable<T> collection){
//    for (T t : collection) {
//      add(t);
//    }
//  }
//
//  public void add(T item) {
//    inner.add(item);
//    if (addObserver != null) {
//      addObserver.invoke(this, item);
//    }
//  }
//
//  public void add(int index, T item) {
//    this.inner.add(index, item);
//    if (addObserver != null) {
//      addObserver.invoke(this, item);
//    }
//  }
//
//  public void remove(T item) {
//    inner.remove(item);
//    if (removeObserver != null) {
//      removeObserver.invoke(this, item);
//    }
//  }
//
//  public boolean isEmpty() {
//    return this.inner.isEmpty();
//  }
//
//  public void clear() {
//    while (this.inner.isEmpty() == false) {
//      this.inner.remove(0);
//    }
//  }
//
//  @Override
//  public Iterator<T> iterator() {
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//  }
//
//}
