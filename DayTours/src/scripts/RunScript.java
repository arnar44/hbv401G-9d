package scripts;

import java.io.*;

public class RunScript {
  public static void main(String[] args) throws IOException, InterruptedException {
      System.out.println(System.getProperty("user.dir"));
	   ProcessBuilder pb = new ProcessBuilder("scripts/test.sh", "");
	   pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
	   pb.redirectError(ProcessBuilder.Redirect.INHERIT);
           Process process = pb.start();
           process.waitFor();
           System.out.println(process.isAlive());
  }
}
