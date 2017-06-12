/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava.demoClient;

import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.Heading;
import eng.metarJava.support.Speed;

/**
 *
 * @author Marek Vajgl
 */
public class UserSpecificFormatters {
  private static String getSpecificToString (Heading heading){
    return heading.getValue() + "°";
  }
  private static String getSpecificToString(Speed speed){
    return speed.getIntValue(SpeedUnit.KT) + " KT";
  }
}
