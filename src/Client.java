
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
    private int clientCount;


    private JTextArea debugTextArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton connectButton;
    private boolean connected;

    public Client(Socket socket, String clientname){



        //visiualization
        debugTextArea = new JTextArea();
        inputField = new JTextField();
        sendButton = new JButton("Send");
        connectButton = new JButton("Connect");




        JPanel panel = new JPanel();
        panel.add(connectButton);
        panel.add(inputField);
        panel.add(sendButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(debugTextArea), BorderLayout.CENTER);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        //visiualization






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
                bufferedWriter.write(clientname + " : "+messageToSend);
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
                        debugTextArea.append(messagefromgroup +"\n");
                    }catch(IOException e) {


                    }

                }

            }

        }).start();
    }

    public static void main(String []args) throws UnknownHostException, IOException {
        Scanner myObj = new Scanner(System.in);
        // Create a Scanner object
        System.out.println("Enter username");
        String clientname = myObj.nextLine();
        Socket socket = new Socket("localhost",9898);
        Client client = new Client(socket,clientname);
        System.out.println(client.clientCount);
        client.Listen();
        client.sendMessage();

    }




}