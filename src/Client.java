
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client  extends JFrame {

    private Socket socket;
    //each socket for each client
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    //used for read and write
    private String clientname;


    private JTextArea debugTextArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton connectButton;
    private boolean connected;

    public Client(Socket socket, String clientname){

        try {
            this.socket = socket;
            this.bufferedWriter =  new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientname = clientname;
            //read client username

        }catch(IOException e) {

        }
    }

    public void sendMessage() {

        try {
            bufferedWriter.write(clientname);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(clientname + ":"+messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }
        }catch(IOException e) {

        }
    }

    public void Listen() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String messagefromgroup;
                while (socket.isConnected()) {
                    try {
                        messagefromgroup = bufferedReader.readLine();
                        System.out.println(messagefromgroup);
                    }catch(IOException e) {


                    }

                }

            }

        }).start();
    }

    public static void main(String []args) throws UnknownHostException, IOException {

        Scanner scanner = new Scanner (System.in);
        System.out.println("Client name: ");
        String clientname = scanner.nextLine();
        Socket socket = new Socket("localhost",9898);
        Client client = new Client(socket,clientname);
        client.Listen();
        client.sendMessage();

    }




}






































