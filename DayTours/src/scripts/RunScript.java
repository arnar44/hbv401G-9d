package scripts;

import java.io.*;

public class RunScript {
  public static void main(String[] args) throws IOException, InterruptedException {
      /**
       * Þessi klasi keyrir shellscriptu sem síðan keyrir javascript sem scrapar vefsíðu og fyllir
       * fyllir gagnagrunn (.db) skráin sem er í database pakkanum, af upplýsingum.
       * shellsciptan og javascript eru í scripts möppunni(sem er utan src möppunnar)
       * 
       */
	   ProcessBuilder pb = new ProcessBuilder("scripts/test.sh", "");
	   pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
	   pb.redirectError(ProcessBuilder.Redirect.INHERIT);
           Process process = pb.start();
           process.waitFor();
  }
}
