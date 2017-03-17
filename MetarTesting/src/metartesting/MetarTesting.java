/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metartesting;

import eng.metarJava.Report;
import eng.metarJava.decoders.EuropeFormatter;
import eng.metarJava.decoders.GenericParser;
import eng.metarJava.downloaders.Downloader;
import eng.metarJava.downloaders.NoaaGovDownloader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import eng.metarJava.decoders.Parser;

/**
 *
 * @author Marek Vajgl
 */
public class MetarTesting {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    //runFileTest();

    Downloader d = new NoaaGovDownloader();
    
    String ret = "N/A";
    try {
      ret = d.download("LKPR");
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
    System.out.println(ret);
    
    Report r = new GenericParser().parse(ret);
    ret = new EuropeFormatter().format(r);
    System.out.println(ret);
  }

  private static void runCheck(String inFile, String outFile,
          eng.metarJava.downloaders.Downloader downloader,
          eng.metarJava.decoders.Parser parser) {
    List<String> codes;
    List<String> errMetars = new ArrayList<>();
    int MAX_ERROR_COUNT = 5;
    try {
      codes = java.nio.file.Files.readAllLines(Paths.get(inFile));
    } catch (IOException ex) {
      System.out.println("Reading in-file failed. " + ex.getMessage());
      return;
    }

    String m;
    Report r;

    for (String code : codes) {
      if (errMetars.size() >= MAX_ERROR_COUNT) {
        break;
      }
      System.out.println(code);
      System.out.println("\tdownloading...");
      try {
        m = downloader.download(code);
      } catch (Throwable t) {
        System.out.println("Failed to download for code " + code + ". Reason: " + t.getMessage());
        continue;
      }
      System.out.println("\tdecoding...");
      try {
        r = parser.parse(m);
      } catch (Throwable t) {
        System.out.println("Failed to decode metar " + m + ". Reason: " + t.getMessage());
        errMetars.add(m);
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
    String inFile = "C:\\Users\\Marek Vajgl\\Documents\\NetBeansProjects\\_MetarJ\\MetarTesting\\src\\other\\ICAO_L.txt";
    String outFile = "C:\\Users\\Marek Vajgl\\Documents\\NetBeansProjects\\_MetarJ\\MetarTesting\\src\\other\\ICAO_L_fails.txt";
    Downloader d = new NoaaGovDownloader();
    Parser p = new GenericParser();
    runCheck(inFile, outFile, d, p);
  }

}
