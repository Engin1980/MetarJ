/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metartesting;

import eng.metarJava.Report;
import eng.metarJava.decoders.GenericFormat;
import eng.metarJava.decoders.IParse;
import eng.metarJava.downloaders.Downloader;
import eng.metarJava.downloaders.NoaaGovDownloader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marek Vajgl
 */
public class MetarTesting {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Downloader d = new NoaaGovDownloader();
    
    String ret = "N/A";
    try {
      ret = d.download("LKPR");
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
    System.out.println(ret);
  }
  
}
