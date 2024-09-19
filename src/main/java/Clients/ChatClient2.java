package Clients;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClient2 extends JFrame {
    private JPanel chatPanel;
    private JTextField messageField;
    private JButton sendButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JScrollPane scrollPane;

    public ChatClient2(String serverAddress, int serverPort) {
        // Set up the frame
        setTitle("Simz Chat Application");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Chat panel (inside scrollPane)
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS)); // Vertically align messages
        chatPanel.setBackground(Color.WHITE);

        // Scroll pane for chat panel
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // No horizontal scroll
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Panel for message input
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        messagePanel.setBackground(new Color(245, 245, 245));

        // Single-line message input
        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                new EmptyBorder(5, 5, 5, 5)));

        // Send button
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(new Color(30, 144, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add components to the message panel
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        add(messagePanel, BorderLayout.SOUTH);

        // Connect to the server
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a thread to listen for incoming messages
            new Thread(new IncomingReader()).start();
        } catch (IOException e) {
            appendSystemMessage("Error connecting to server");
            e.printStackTrace();
        }

        // Action listener for send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Press Enter to send a message
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Make the frame visible
        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            String formattedMessage = formatMessage("You", message);

            // Send the message to the server
            out.println(formattedMessage);

            // Append the message to the chat panel for the sender to see
            appendMessage(formattedMessage, true);

            // Clear the input field after sending
            messageField.setText("");
        }
    }

    // Helper method to format messages with a timestamp
    private String formatMessage(String sender, String message) {
        String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
        return String.format("[%s] %s: %s", timeStamp, sender, message);
    }

    // Helper method to append messages to the chat panel
    private void appendMessage(String message, boolean isSentByUser) {
        JPanel messageBubble = new JPanel();
        messageBubble.setLayout(new BorderLayout());
        messageBubble.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel messageLabel = new JLabel("<html><p style='width: 150px;'>" + message + "</p></html>");
        messageLabel.setOpaque(true);

        if (isSentByUser) {
            // Style sent messages (align to right)
            messageLabel.setBackground(new Color(173, 216, 230)); // Light blue for sent messages
            messageLabel.setForeground(Color.BLACK);
            messageBubble.add(messageLabel, BorderLayout.EAST);
        } else {
            // Style received messages (align to left)
            messageLabel.setBackground(new Color(240, 240, 240)); // Light gray for received messages
            messageLabel.setForeground(Color.BLACK);
            messageBubble.add(messageLabel, BorderLayout.WEST);
        }

        chatPanel.add(messageBubble);
        chatPanel.revalidate(); // Refresh the panel to show the new message
        chatPanel.repaint();

        // Scroll to the bottom to show the latest message
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    // Helper method for system messages (like errors)
    private void appendSystemMessage(String message) {
        JLabel systemMessageLabel = new JLabel(message);
        systemMessageLabel.setForeground(Color.RED); // Red for system messages

        JPanel systemMessagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        systemMessagePanel.add(systemMessageLabel);

        chatPanel.add(systemMessagePanel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    String replace = message.replace("You", "Friend");
                    // Append the message to the chat panel as a received message
                    appendMessage(replace, false);
                }
            } catch (IOException e) {
                appendSystemMessage("Error reading from server");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ChatClient1("localhost", 12345); // Connect to localhost on port 12345
    }
}
