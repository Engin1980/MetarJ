/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava.downloaders;

import eng.metarJava.downloaders.support.AsyncResult;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents generic METAR report downloading interface.
 *
 * @author Marek Vajgl
 */
public abstract class Downloader {

  /**
   * Downloads the report from the source.
   *
   * @param icao ICAO code of the required station report.
   * @return String representing METAR report
   * @throws MalformedURLException thrown when the resulting URL asking on the web does not make sence. Typically when icao parameter
   * is invalid.
   * @throws IOException thrown when any IO exception occurs during communication.
   */
  public abstract String download(String icao) throws MalformedURLException, IOException;

  /**
   * Downloads the report from the source asynchronously. When the method is called, it is non-blocking call. When a result is obtained
   * (or an error occured), the method {@linkplain eng.metarJava.downloaders.support.AsyncResult.finished} is invoked with the
   * appropriate state and result objects.
   *
   * @param icao ICAO code of the required station report.
   * @param result {@linkplain eng.metarJava.downloaders.support.AsyncResult} which method "finished(....)" will be invoked when
   * asynchronous call is finished.
   */
  public void downloadAsync(String icao, AsyncResult<String> result) {
    Thread t = new Thread(
            new Runnable() {
      @Override
      public void run() {
        downloadAsyncThreadCall(icao, result);
      }
    }
    );
    t.start();
  }

  private void downloadAsyncThreadCall(String icao, AsyncResult result) {
    String ret = null;
    try {
      ret = download(icao);
      result.finished(AsyncResult.eState.Success, ret, null);
    } catch (Throwable ex) {
      result.finished(AsyncResult.eState.Exception, null, ex);
    }
  }
}
