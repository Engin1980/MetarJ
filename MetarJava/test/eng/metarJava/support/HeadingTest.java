package eng.metarJava.support;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class HeadingTest {
  
  @Test
  public void testCorrectCreation() {
    Heading h = new Heading(45);
    int expected = 45;
    int actual = h.getValue();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLower() {
    Heading h = new Heading(-60);
    int expected = 300;
    int actual = h.getValue();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testBigLower() {
    Heading h = new Heading(-800);
    int expected = 280;
    int actual = h.getValue();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGreater() {
    Heading h = new Heading(400);
    int expected = 40;
    int actual = h.getValue();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testBigGreater() {
    Heading h = new Heading(800);
    int expected = 80;
    int actual = h.getValue();
    assertEquals(expected, actual);
  }
  
}
