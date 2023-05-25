import java.io.*;
import java.net.*;

public class Client {
  private static final String SERVER_IP = "127.0.0.1";
  private static final int PORT = 12345;

  public static void main(String[] args) {
    try (Socket socket = new Socket(SERVER_IP, PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

      String message;
      while ((message = reader.readLine()) != null) {
        System.out.println(message);

        if (message.startsWith("Escolha par")) {
          int escolha = Integer.parseInt(consoleReader.readLine());
          writer.println(escolha);
        } else if (message.startsWith("Entre com um número")) {
          int numero = Integer.parseInt(consoleReader.readLine());
          writer.println(numero);
        } else if (message.startsWith("Deseja continuar jogando")) {
          String resposta = consoleReader.readLine();
          writer.println(resposta);
        } else if (message.startsWith("Fim do jogo")) {
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
