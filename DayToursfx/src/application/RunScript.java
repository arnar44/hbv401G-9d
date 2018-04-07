package application;

import java.io.*;

public class RunScript {
  public static void main(String[] args) throws IOException, InterruptedException {
	   ProcessBuilder pb = new ProcessBuilder("scripts/test.sh", "");
	   pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
	   pb.redirectError(ProcessBuilder.Redirect.INHERIT);
	   
  }
}
