
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame {

    private ServerSocket serverSocket;
    public JTextArea debugTextArea;
    private int clientCount;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        try {

            debugTextArea = new JTextArea();
            add(new JScrollPane(debugTextArea));
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            debugTextArea.append("Chat Server started at " + new Timestamp(System.currentTimeMillis())+ "\n");

            clientCount = 0;

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                //stop until connected
                System.out.println("New Client");
                clientCount++;

                ClientHandler clientHandler = new ClientHandler(socket);
                debugTextArea.append("Starting thread for Client " + clientCount + " at "+ new Timestamp(System.currentTimeMillis()) +"\n");
                debugTextArea.append("Client " + clientCount + "'s host name is localhost" + "\n");
                debugTextArea.append("Client " + clientCount + "'s IP Address is " + socket.getInetAddress() + "\n");



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