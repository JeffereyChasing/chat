
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    // record all clients
    private Socket socket;
    //each socket for each client
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    //used for read and write
    private String clientname;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter =  new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientname = bufferedReader.readLine();
            //read client username
            clientHandlers.add(this);
            //add this to the arraylist of clienthandlers
            broadcast("Server: " + clientname + "has joined");

        }catch(IOException e) {

        }
    }

    @Override
    public void run() {

        String messageFromClient;

        while (socket.isConnected()) {

            try {
                messageFromClient = bufferedReader.readLine();
                broadcast(messageFromClient);
            }catch(IOException e) {
                break;
            }

        }


    }


    public void broadcast(String messageToSend) {
        for (ClientHandler ch: clientHandlers) {
            try {
                if(!ch.clientname.equals(clientname)) {
                    ch.bufferedWriter.write(messageToSend);
                    ch.bufferedWriter.newLine();
                    ch.bufferedWriter.flush();
                }
            }catch(IOException e) {
                break;
            }
        }
    }

    public void disconnect() {
        clientHandlers.remove(this);
        // take out this client
        broadcast("Server:" + this.clientname + "has disconnected");
    }



}
