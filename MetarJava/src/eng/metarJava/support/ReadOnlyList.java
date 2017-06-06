package eng.metarJava.support;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 *
 * @author Marek Vajgl
 */
public class ReadOnlyList<T> implements Iterable<T> {

  private final List<T> inner;
  
  public ReadOnlyList(List<T> data){
    this.inner = new ArrayList(data);
  }
  
  public ReadOnlyList(Set<T> data){
    this.inner = new ArrayList(data);
  }
  
  public int size() {
    return inner.size();
  }

  public boolean isEmpty() {
    return inner.isEmpty();
  }

  public boolean contains(T o) {
    return inner.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return inner.iterator();
  }

  public T[] toArray() {
    //T[] arr = (T[])Array.newInstance(T.class, 0);
    throw new UnsupportedOperationException("Sorry, I dont know how to code this.");
    //return inner.toArray(arr);
  }

  public boolean containsAll(Collection<?> c) {
    return this.inner.containsAll(c);
  }

  public T get(int index) {
    T t = this.inner.get(index);
    return t;
  }

  public int indexOf(T o) {
    return this.inner.indexOf(o);
  }

  public int lastIndexOf(T o) {
    return this.inner.lastIndexOf(o);
  }

  public ListIterator<T> listIterator() {
    return this.inner.listIterator();
  }

  public ListIterator<T> listIterator(int index) {
    return this.inner.listIterator(index);
  }

  public List<T> subList(int fromIndex, int toIndex) {
   return this.inner.subList(fromIndex, toIndex);
  }
  
  public List<T> toList(){
    return new ArrayList<T>(this.inner);
  }
}
