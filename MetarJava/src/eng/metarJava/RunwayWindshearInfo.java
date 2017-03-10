package eng.metarJava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class RunwayWindshearInfo implements Iterable<String> {

  private final List<String> runwayDesignators = new ArrayList();
  private boolean allRunways = false;

  public RunwayWindshearInfo() {
    this.allRunways = false;
  }

  public RunwayWindshearInfo(boolean isWindshearAllRunways) {
    this.allRunways = isWindshearAllRunways;
  }

  public boolean isAllRunways() {
    return allRunways;
  }

  public void setAllRunways(boolean allRunways) {
    this.allRunways = allRunways;
  }

  @Override
  public Iterator<String> iterator() {
    return this.runwayDesignators.iterator();
  }

  public void add(String runwayDesignator) {
    if (runwayDesignator == null || runwayDesignator.isEmpty()) {
      throw new IllegalArgumentException("[runwayDesignator] must have nonempty value.");
    }
    if (this.runwayDesignators.contains(runwayDesignator)) {
      throw new IllegalArgumentException("[runwayDesignator] already exists in this list.");
    }

    this.setAllRunways(false);
    this.runwayDesignators.add(runwayDesignator);
  }

  public boolean contains(String runwayDesignator) {
    return this.runwayDesignators.contains(runwayDesignator);
  }

  public void add(int index, String runwayDesignator) {
    if (runwayDesignator == null || runwayDesignator.isEmpty()) {
      throw new IllegalArgumentException("[runwayDesignator] must have nonempty value.");
    }
    if (this.runwayDesignators.contains(runwayDesignator)) {
      throw new IllegalArgumentException("[runwayDesignator] already exists in this list.");
    }

    this.setAllRunways(false);
    this.runwayDesignators.add(index, runwayDesignator);
  }

  public void addAll(Iterable<String> runwayDesignators) {
    for (String runwayDesignator : runwayDesignators) {
      this.add(runwayDesignator);
    }
  }

  public void clear() {
    this.runwayDesignators.clear();
  }

  public void remove(String runwayDesignator) {
    if (this.runwayDesignators.contains(runwayDesignator)) {
      this.runwayDesignators.remove(runwayDesignator);
    }
  }

  public void remove(int index) {
    this.runwayDesignators.remove(index);
  }

  public void removeAll(Iterable<String> runwayDesignators) {
    for (String runwayDesignator : runwayDesignators) {
      this.remove(runwayDesignator);
    }
  }

  public int size(){
    return this.runwayDesignators.size();
  }
  
  public List<String> getToList(){
    List<String> ret = new ArrayList(this.runwayDesignators);
    return ret;
  }
  
  public void setFromList(Iterable<String> runwayDesignators){
    this.clear();
    this.addAll(runwayDesignators);
  }
  
  public String get(int index){
    return this.runwayDesignators.get(index);
  }
}
