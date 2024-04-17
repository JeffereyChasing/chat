
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame {

    private ServerSocket serverSocket;
    public JTextArea debugTextArea;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                //stop until connected
                System.out.println("New Client");

                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

                // objects created from a class implementing runnable would run on a new thread
            };
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    public void closeServerSocket() {
        try {
            if(serverSocket!=null) {
                serverSocket.close();
                //close if not null
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main (String []args) throws IOException{

        ServerSocket serverSocket = new ServerSocket(9898);
        // server will listen for client connecting to 9898
        Server server = new Server(serverSocket);
        server.start();
    }




}
