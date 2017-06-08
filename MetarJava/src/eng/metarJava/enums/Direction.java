package eng.metarJava.enums;

/**
 * Represents direction using cardinal points - north, south, east, west.
 * @author Marek Vajgl
 */
public enum Direction {
  north,
  south,
  west,
  east;

  /**
   * Converts one-character N/S/E/W representation into an enum value.
   * @param direction N/S/E/W character
   * @return enum representation
   */
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
  
  /**
   * Converts enum representation into N/S/E/W uppercase char value.
   * @return N/S/E/W uppercase char value as string
   */
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
