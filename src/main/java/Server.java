
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String args[]) throws IOException {
        Socket socket;
        ServerSocket serverSocket = new ServerSocket( 4999);


        while (true){
            socket = serverSocket.accept();
            System.out.println("Połączono");
            ServerThread serverThread = new ServerThread(socket);
            serverThread.start();
        }
    }
}

class ServerThread extends Thread {
    private Socket socket = null;
    private BufferedReader inputStream = null;
    private PrintWriter outputStream = null;
    private User user;
    String txt = null;
    String login = null;
    String haslo = null;
    String nick = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


        try {
            outputStream.println("Wciśnij 1 aby zalogować, Wciśnij 2 aby zarejestrować, Wciśnij 3 aby wyjść");
            outputStream.flush();
            txt = inputStream.readLine();
            while (!txt.equals("3")){
                switch (txt){
                    case "1":
                        if(user == null){
                            outputStream.println("Nie ma zarejestrowanego użytkownika zarejestruj sie.");
                            outputStream.flush();
                            txt = inputStream.readLine();
                            break;
                        }
                        outputStream.println("login:");
                        outputStream.flush();
                        login = inputStream.readLine();
                        outputStream.println("haslo:");
                        outputStream.flush();
                        haslo = inputStream.readLine();
                        while(!(user.checkLogin(login) && user.checkHaslo(haslo))) {
                            if (!user.checkHaslo(haslo)) {
                                outputStream.println("Błędne hasło. Spróbuj jeszcze raz.");
                                outputStream.flush();
                            } else if (!user.checkLogin(login)) {
                                outputStream.println("Błędny login. Spróbuj jeszcze raz.");
                                outputStream.flush();
                            }
                        }
                        outputStream.println("Zalogowano! Witaj " + user.getNick() + ".");
                        outputStream.flush();
                        txt = "3";
                        break;
                    case "2":
                        outputStream.println("login:");
                        outputStream.flush();
                        login = inputStream.readLine();
                        outputStream.println("haslo:");
                        outputStream.flush();
                        haslo = inputStream.readLine();
                        outputStream.println("nick:");
                        outputStream.flush();
                        nick = inputStream.readLine();
                        user = new User(login, haslo, nick);
                        outputStream.println("Zarejestrowano! Możesz się zalogować!");
                        outputStream.flush();
                        txt = inputStream.readLine();
                        break;
                    default:
                        outputStream.println("Przepraszam nie rozumiem spróbuj jeszcze raz! Wciśnij 1 aby zalogować, Wciśnij 2 aby zarejestrować, Wciśnij 3 aby wyjść");
                        outputStream.flush();
                        txt = inputStream.readLine();
                        break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            txt = this.getName();
            System.out.println("Client zakończył połączenie" + txt);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    System.out.println("Input Stream closed");
                }

                if (outputStream != null) {
                    outputStream.close();
                    System.out.println("Output Stream closed");
                }

                if (socket != null) {
                    socket.close();
                    System.out.println("Socket closed");
                }
            } catch (IOException e) {
                System.out.println("Socket close error!");
            }
        }
    }

    class User {
        private String login;
        private String haslo;
        private String nick;

        User(String login, String haslo, String nick){
            this.login = login;
            this.haslo = haslo;
            this.nick = nick;
        }

        public boolean checkHaslo(String hasloToCheck) {
            return haslo.equals(hasloToCheck);
        }

        public boolean checkLogin(String loginToCheck) {
            return login.equals(loginToCheck);
        }

        public String getNick() {
            return nick;
        }
    }
}



