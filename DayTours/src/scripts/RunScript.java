package scripts;

import java.io.*;

public class RunScript {
  public static void main(String[] args) throws IOException, InterruptedException {
      System.out.println(System.getenv("PATH"));
      System.out.println("test");
	   ProcessBuilder pb = new ProcessBuilder("scripts/test.sh", "");
	   pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
	   pb.redirectError(ProcessBuilder.Redirect.INHERIT);
	   pb.start();
  }
}
