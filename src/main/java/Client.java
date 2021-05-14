import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String args[]) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        Socket userSocket = null;
        String txt = null;
        String response = null;
        PrintWriter outputStream = null;
        BufferedReader inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            userSocket = new Socket(address, 4999);
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            inputStream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
            outputStream = new PrintWriter(userSocket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Wciśnij 3 aby zakończyć działanie.");
        response = inputStream.readLine();
        System.out.println("Server response: " + response);
        try {
            txt = bufferedReader.readLine();
            while(!txt.equals("3")) {
                outputStream.println(txt);
                outputStream.flush();
                response = inputStream.readLine();
                System.out.println("Server response: " + response);
                txt = bufferedReader.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            outputStream.close();
            bufferedReader.close();
            userSocket.close();
            System.out.println("Połączenie zakończone.");
        }
    }

}
