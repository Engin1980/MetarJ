/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava.downloaders;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author Marek Vajgl
 */
public abstract class Downloader {
  public abstract String download (String icao) throws MalformedURLException, IOException;
}
