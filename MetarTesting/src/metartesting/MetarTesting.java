/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metartesting;

import eng.metarJava.Report;
import eng.metarJava.decoders.GenericFormat;
import eng.metarJava.decoders.IParse;

/**
 *
 * @author Marek Vajgl
 */
public class MetarTesting {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    String metar = "METAR LKPR 080835Z AUTO 12112KT";
    
    IParse p = new GenericFormat();
    Report r = p.parse(metar);
  }
  
}
