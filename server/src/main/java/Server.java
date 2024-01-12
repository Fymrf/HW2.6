import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private static boolean stopChat = false;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Сервер запущен. Ожидание подключения клиента...");

            clientSocket = serverSocket.accept();
            System.out.println("Клиент подключен.");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            Timer timer = new Timer();
            timer.schedule(new CheckBooleanTask(), 0, 1000); // Проверка каждую секунду

            clientTern();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void serverTern() throws IOException {
        while (true) {
            System.out.print("Сервер: ");
            String message = readMessageFromConsole();
            out.println(message);
            if (message.equalsIgnoreCase("!end")) {
                break;
            }
            if (message.equalsIgnoreCase("#STOP")) {
                stopChat = true;
            }
        }
        clientTern();
    }

    private static void clientTern() throws IOException {
        while (true) {
            String response = in.readLine();
            System.out.println("Клиент: " + response);
            if (response.equalsIgnoreCase("!end")) {
                break;
            }
            if (response.equalsIgnoreCase("#STOP")) {
                stopChat = true;
            }
        }
        serverTern();
    }

    private static String readMessageFromConsole() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
    static class CheckBooleanTask extends TimerTask {
        public void run() {
            if (stopChat) {
                try {
                    serverSocket.close();
                    System.out.println("Сервер завершен.");
                    System.exit(-1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}