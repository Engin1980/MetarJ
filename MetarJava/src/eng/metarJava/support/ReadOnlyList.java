package eng.metarJava.support;

import eng.metarJava.exception.NullArgumentException;
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

  public ReadOnlyList(T... data) {
    if (data == null) {
      this.inner = new ArrayList(0);
    } else {
      this.inner = new ArrayList(data.length);
      for (int i = 0; i < data.length; i++) {
        this.inner.add(data[i]);
      }
    }
    ensureNoItemNull();
  }

  public ReadOnlyList(Iterable<T> data) {
    if (data == null) {
      this.inner = new ArrayList();
    } else {
      this.inner = new ArrayList();
      for (T t : data) {
        this.inner.add(t);
      }
    }
    ensureNoItemNull();
  }

  public ReadOnlyList(List<T> data) {
    if (data == null) {
      this.inner = new ArrayList();
    } else {
      this.inner = new ArrayList(data);
    }
    ensureNoItemNull();
  }

  public ReadOnlyList(Set<T> data) {
    if (data == null) {
      this.inner = null;
    } else {
      this.inner = new ArrayList(data);
    }
    ensureNoItemNull();
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

  public List<T> toList() {
    return new ArrayList<T>(this.inner);
  }

  /**
   * Checks if RO-list contains null and if so, throws an exception.
   */
  private void ensureNoItemNull() {
    for (int i = 0; i < this.size(); i++) {
      if (this.get(i) == null) {
        throw new IllegalArgumentException("List cannot contain null values. Null found at index " + i + ".");
      }
    }
  }
}
