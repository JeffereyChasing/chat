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

public class Client extends JFrame {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientname;

    private JTextArea debugTextArea;
    private JTextField inputField;

    public Client(Socket socket, String clientname) {
        this.socket = socket;
        this.clientname = clientname;

        debugTextArea = new JTextArea();
        inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
        add(new JScrollPane(debugTextArea), BorderLayout.CENTER);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToServer(inputField.getText());
                inputField.setText("");
            }
        });

        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send the client name to the server
            bufferedWriter.write(clientname);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToServer(String message) {
        try {
            bufferedWriter.write(clientname + " : " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromServer;
                try {
                    while ((messageFromServer = bufferedReader.readLine()) != null) {
                        debugTextArea.append(messageFromServer + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        // Get the client's username
        String clientname = "YourClientName";  // Set a default client name
        // Create a Scanner object
        System.out.println("Enter username");
        Scanner myObj = new Scanner(System.in);
        clientname = myObj.nextLine();

        Socket socket = new Socket("localhost", 9898);
        Client client = new Client(socket, clientname);
        client.Listen();
    }
}
