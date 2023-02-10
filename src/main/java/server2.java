import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class server2 {
    public static void main(String[] args) {
        String host = "localhost";
        Scanner scanner = new Scanner(System.in);
        try (Socket clientSocket = new Socket(host, 8989);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String input = scanner.nextLine();
            out.println(input);
            System.out.println(in.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
