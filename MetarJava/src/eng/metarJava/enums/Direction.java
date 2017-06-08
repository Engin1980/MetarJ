package eng.metarJava.enums;

/**
 *
 * @author Marek Vajgl
 */
public enum Direction {
  north,
  south,
  west,
  east;

  public static Direction parse(char direction) {
    switch (direction) {
      case 'N':
        return Direction.north;
      case 'S':
        return Direction.south;
      case 'E':
        return Direction.east;
      case 'W':
        return Direction.west;
      default:
        throw new UnsupportedOperationException("Unsupported value [" + direction + "] for direction. Is value uppercase?");
    }
  }
  
  @Override
  public String toString(){
    switch (this){
      case east:
        return "E";
      case north:
        return "N";
      case south:
        return "S";
      case west:
        return "W";
      default:
        throw new UnsupportedOperationException();
    }
  }
}
