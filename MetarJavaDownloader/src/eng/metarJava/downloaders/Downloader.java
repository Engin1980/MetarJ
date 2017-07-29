/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava.downloaders;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Represents generic METAR report downloading interface.
 * @author Marek Vajgl
 */
public interface Downloader {
  
  /**
   * Downloads the report from the source.
   * @param icao ICAO code of the required station report.
   * @return String representing METAR report
   * @throws MalformedURLException thrown when the resulting URL asking on the web does not make sence. Typically when icao parameter is invalid.
   * @throws IOException thrown when any IO exception occurs during communication.
   */
  public abstract String download (String icao) throws MalformedURLException, IOException;
}
