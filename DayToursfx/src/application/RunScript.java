package application;

import java.io.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class RunScript {
  public static void main(String[] args) throws IOException, InterruptedException {
	  System.out.println(System.getenv("PATH"));
	   ProcessBuilder pb = new ProcessBuilder("scripts/test.sh", "");
	   pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
	   pb.redirectError(ProcessBuilder.Redirect.INHERIT);
	   
  }
}
