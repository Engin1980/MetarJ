package eng.metarJava;

import eng.metarJava.enums.Direction;

/**
 * Represents variability of visibility in most significant direction.
 * @author Marek Vajgl
 */
public class VisibilityVariability {
  private final int visibilityInMeters;
  private final Direction direction;
  
  public static VisibilityVariability create(int visibilityInMeters, Direction direction){
    VisibilityVariability ret  = new VisibilityVariability(visibilityInMeters, direction);
    return ret;
  }

  protected VisibilityVariability(int visibilityInMeters, Direction direction) {
    if (visibilityInMeters < 0)
      throw new IllegalArgumentException("[visibilityInMeters] must be zero or positive.");
    this.visibilityInMeters = visibilityInMeters;
    this.direction = direction;
  }

  public int getVisibilityInMeters() {
    return visibilityInMeters;
  }

  public Direction getDirection() {
    return direction;
  }
}
