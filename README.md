This is a simple chat application built using Java Swing for the GUI and Java Sockets for networking. The application allows users to send and receive messages in real-time, with a visually appealing interface that differentiates between sent and received messages.

Features
Real-time messaging: Users can send and receive messages through Java Sockets.
UI: Sent and received messages are styled distinctly for a clean and professional chat interface.
Sent messages: Aligned to the right, light blue background.
Received messages: Aligned to the left, light gray background.
Auto-scrolling: The chat area automatically scrolls to the latest message.
Single-line message input: Users can type and send messages either by pressing the "Send" button or the "Enter" key.
System messages: Errors or important notices are displayed in red for clarity.

Technologies Used
Java Swing: For the graphical user interface (GUI).
Java Sockets: For network communication between the client and server.

Prerequisites
Java JDK 8 or later installed.
Basic understanding of how Java networking with sockets works.

How to Run:
Server
The server-side code listens for incoming connections from clients and facilitates message broadcasting between connected clients.
Compile and run the ChatServer.java file.
The server will start listening for incoming client connections on a specified port (default: 12345).

Client
The client-side code connects to the server and allows users to send and receive messages.
Compile and run the ChatClient.java file.
The client will connect to the server on localhost and port 12345. You can change the server address and port number if needed in the ChatClient constructor. Once connected, the client can send and receive messages from other connected clients in real-time.

Code Overview

Client (ChatClient.java)
Socket connection: The client connects to the server via a Socket and uses a PrintWriter to send messages and a BufferedReader to receive messages.
Message formatting: Messages are formatted with timestamps and aligned either to the left (for received messages) or right (for sent messages).

GUI components:
JTextField: Used for inputting messages.
JTextArea: Display area for chat history.
JButton: "Send" button for submitting messages.

Server (ChatServer.java)
Client handling: The server listens for multiple clients using ServerSocket and creates a new thread for each connected client to handle communication.
Broadcasting: The server broadcasts messages received from one client to all other connected clients, enabling group chat functionality.
