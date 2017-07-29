/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava.downloaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Downloads METAR report from noaa.gov server.
 * @author Marek Vajgl
 */
public class NoaaGovDownloader implements Downloader {

  private final static String URL = "ftp://tgftp.nws.noaa.gov/data/observations/metar/stations/%S.TXT";

  @Override
  public String download(String icao) throws MalformedURLException, IOException {
    String urlString = String.format(URL, icao);
    String ret;

    URL url;
    url = new URL(urlString);

    try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
      ret = in.readLine(); // first line is date, not interesting
      ret = "METAR " + in.readLine();  // second line is METAR without METAR prefix
    } catch (IOException ex) {
      throw ex;
    }

    return ret;
  }
}
