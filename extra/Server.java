import java.io.*;
import java.net.*;

public class Server {
  private static final int PORT = 12345;
  private static int playerScore1 = 0;
  private static int playerScore2 = 0;

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Servidor iniciado. Aguardando conexões...");

      while (true) {
        Socket client1 = serverSocket.accept();
        Socket client2 = serverSocket.accept();
        System.out.println("Dois jogadores conectados.");

        new Thread(new GameThread(client1, client2)).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class GameThread implements Runnable {
    private Socket client1;
    private Socket client2;

    public GameThread(Socket client1, Socket client2) {
      this.client1 = client1;
      this.client2 = client2;
    }

    @Override
    public void run() {
      try {
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
        PrintWriter writer1 = new PrintWriter(client1.getOutputStream(), true);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
        PrintWriter writer2 = new PrintWriter(client2.getOutputStream(), true);

        writer1.println("Bem-vindo ao jogo do Par ou Ímpar! Você é o Jogador 1.");
        writer2.println("Bem-vindo ao jogo do Par ou Ímpar! Você é o Jogador 2.");

        boolean continuar = true;
        while (continuar) {
          writer1.println("Escolha par (0) ou ímpar (1):");
          writer2.println("Aguardando a escolha do Jogador 1...");

          int playerChoice1 = Integer.parseInt(reader1.readLine());
          int playerChoice2 = (playerChoice1 == 0) ? 1 : 0;

          writer1.println("Sua escolha: " + (playerChoice1 == 0 ? "par" : "ímpar"));
          writer2.println("Sua escolha: " + (playerChoice2 == 0 ? "par" : "ímpar"));

          writer1.println("Entre com um número entre 0 e 5:");
          writer2.println("Entre com um número entre 0 e 5:");

          int numeroJogador1 = Integer.parseInt(reader1.readLine());
          int numeroJogador2 = Integer.parseInt(reader2.readLine());

          writer1.println("Jogador 2 escolheu o número: " + numeroJogador2);
          writer2.println("Jogador 1 escolheu o número: " + numeroJogador1);

          int soma = numeroJogador1 + numeroJogador2;
          writer1.println("Soma dos números: " + soma);
          writer2.println("Soma dos números: " + soma);

          if ((soma % 2 == 0 && playerChoice1 == 0) || (soma % 2 == 1 && playerChoice1 == 1)) {
            writer1.println("Você venceu!");
            writer2.println("Você perdeu!");
            playerScore1++;
          } else {
            writer1.println("Você perdeu!");
            writer2.println("Você venceu!");
            playerScore2++;
          }

          writer1.println("Placar: Jogador 1: " + playerScore1 + ", Jogador 2: " + playerScore2);
          writer2.println("Placar: Jogador 1: " + playerScore1 + ", Jogador 2: " + playerScore2);

          writer1.println("Deseja continuar jogando? (S/N)");
          writer2.println("Deseja continuar jogando? (S/N)");

          String playerResponse1 = reader1.readLine();
          String playerResponse2 = reader2.readLine();
          continuar = playerResponse1.equalsIgnoreCase("S") && playerResponse2.equalsIgnoreCase("S");
        }

        writer1.println("Fim do jogo. Obrigado por jogar!");
        writer2.println("Fim do jogo. Obrigado por jogar!");

        client1.close();
        client2.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
