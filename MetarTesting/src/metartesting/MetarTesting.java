/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metartesting;

import eng.metarJava.Report;
import eng.metarJava.decoders.EUFormatter;
import eng.metarJava.decoders.EUParser;
import eng.metarJava.decoders.Formatter;
import eng.metarJava.decoders.GenericParser;
import eng.metarJava.downloaders.Downloader;
import eng.metarJava.downloaders.NoaaGovDownloader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import eng.metarJava.decoders.Parser;
import eng.metarJava.downloaders.support.AsyncResult;

/**
 *
 * @author Marek Vajgl
 */
public class MetarTesting {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    asyncCallTest();
    
    //runFileTest();
    //runSimpleTest();
    
  }
  
  private static void runSimpleTest(){
    Downloader d = new NoaaGovDownloader();
    
    String ret = "N/A";
    try {
      ret = d.download("LKPR");
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
    System.out.println(ret);
    
    Report r = new GenericParser().parse(ret);
    ret = new EUFormatter().format(r);
    System.out.println(ret);
  }

  private static void runCheck(String inFile, String outFile,
          eng.metarJava.downloaders.Downloader downloader,
          eng.metarJava.decoders.Parser parser,
          eng.metarJava.decoders.Formatter formatter, int maxErrorCount) {
    List<String> codes;
    List<String> errMetars = new ArrayList<>();
    try {
      codes = java.nio.file.Files.readAllLines(Paths.get(inFile));
    } catch (IOException ex) {
      System.out.println("Reading in-file failed. " + ex.getMessage());
      return;
    }

    String m;
    Report r;
    String e;

    for (String code : codes) {
      r = null;
      e = null;
      if (errMetars.size() >= maxErrorCount) {
        break;
      }
      System.out.println(code);
      System.out.println("\tdownloading...");
      try {
        m = downloader.download(code);
      } catch (Throwable t) {
        System.out.println("Failed to download for code " + code + ". Reason: " + t.getMessage());
        errMetars.add("Decode " + code);
        continue;
      }
      System.out.println("\tdecoding...");
      try {
        r = parser.parse(m);
      } catch (Throwable t) {
        System.out.println("Failed to decode metar " + m + ". Reason: " + t.getMessage());
        errMetars.add("Decode " + m);
      }
      if (formatter !=null && r != null){
        System.out.println("\tencoding...");
        try{
          e = formatter.format(r);
        }catch (Throwable t){
          System.out.println("Failed to encode metar " + m + ". Reason: " + t.getMessage());
          errMetars.add("Encode " + m);
        }
        
        if (e != null && e.equals(m) == false){
          errMetars.add("Compare " + m + " -> " + e);
        }
      }
    }

    try {
      java.nio.file.Files.write(Paths.get(outFile), errMetars);
    } catch (IOException ex) {
      System.out.println("Failed to save errors to file " + outFile + ". Reason: " + ex.getMessage());
    }

    System.out.println("Operation completed.");
  }

  private static void runFileTest() {
    String inFile = "C:\\Users\\Marek Vajgl\\Documents\\NetBeansProjects\\_MetarJ\\MetarTesting\\src\\other\\ICAO_E.txt";
    String outFile = "C:\\Users\\Marek Vajgl\\Documents\\NetBeansProjects\\_MetarJ\\MetarTesting\\src\\other\\ICAO_E_fails.txt";
    Downloader d = new NoaaGovDownloader();
    Parser p = new EUParser();
    Formatter f = new EUFormatter(); //new EUFormatter();
    runCheck(inFile, outFile, d, p, f, 10);
  }

  private static void asyncCallTest() {
    Downloader d = new NoaaGovDownloader();
    String icao = "LKKKT";
    System.out.println("Async starting");
    d.downloadAsync(icao, new AsyncResult<String>() {
      @Override
      public void finished(AsyncResult.eState state, String value, Throwable exception) {
        System.out.println("Async finished");
        System.out.println("State: " + state);
        System.out.println("Value: " + value);
        System.out.println("Excep: " + exception);
      }
    });
    System.out.println("Async started");
  }

}
