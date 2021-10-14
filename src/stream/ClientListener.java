package stream;

import java.io.*;

public class ClientListener extends Thread {

  private BufferedReader SocIn;

  public ClientListener(BufferedReader SocIn) {
    this.SocIn = SocIn;
  }

  public void run() {
    try {
      while (true) {
        String line = SocIn.readLine();
        System.out.println(line);
      }
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }

}
