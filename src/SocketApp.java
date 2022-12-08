import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketApp{

    public static void main(String[] args) {
        
        String usage = """
        Usage: Server 
        =============
        <program><server><port><cookie-file.text>
        Usage: Client
        =============
        <program><client><host><port>
        """;

        //javac -sourcepath src -d classes src/*.java | to compile
        //java -cp classes SocketApp server 12000 cookie_file.txt | to run server
        //java -cp classes SocketApp client localhost 12000 | to run client

      
        
        if(args.length != 3) { //as both server and client requires 3 arguments
            System.out.println("Incorrect Inputs. Please check the following usage");
            System.out.println(usage);
            return;

        }

        String type = args[0];
        if (type.equalsIgnoreCase("server")){ //if 1st argument is "server"
            int port = Integer.parseInt(args[1]); //port = 2nd argument; convert from String to Integer
            String fileName = args[2];//filename = 3rd argument
            StartServer(port, fileName); //call for StartServer method (with inputs in arguments)
        
        }   else if (type.equalsIgnoreCase("client")) { //if 1st argument is "client"
            String hostName = args[1]; //hostName = 2nd argument
            int port = Integer.parseInt(args[2]); //port = 3rd argument; convert from String to Integer
            StartClient(hostName,port); //call for StartClient method (with inputs in arguments)

        }   else if (type.equalsIgnoreCase("thread.server")) {
            int port = Integer.parseInt(args[1]);
            String fileName = args[2];
            StartMultiThreaderServer(port, fileName);//call for StartMultiThreaderServer (with inputs in arguments)

       
        } else {
            System.out.println("Incorrect arguments");
        }

     



    }

    public static void StartServer(int port, String fileName) {
        System.out.println("SERVER SOCKET");
        
        try{
        ServerSocket server = new ServerSocket(port); //creating server with port
        Socket socket = server.accept(); //accepts connection from Client       

        // IN (to receive Scanner input from Client)
        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        // OUT (Purpose: to send Client a random cookie from the file)
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        while (true) { //when to use true vs !stop
            String fromClient = dis.readUTF(); //read Client input as a String 

            if (fromClient.equalsIgnoreCase("exit")){
                //exit
                break;
            }
            System.out.println("Log: msg from client: " + fromClient);//inputs from Client other than exit and get-cookie

            if (fromClient.equalsIgnoreCase("get-cookie")){
                //send a random cookie from the file
                //dos.writeUTF("Dummy cookie.. ");
                //implement this class
                dos.writeUTF(new CookieFile().GetRandomCookieFromFile("src/cookie_file.txt"));//calls CookieFile Class
                dos.flush();
            } else {
                //Send a msg, "Invalid command from server"
                dos.writeUTF("From server: Invalid Command");
                dos.flush();
            
            }
        }
        socket.close();
        server.close();
        
       

        } catch (IOException e){
            e.printStackTrace();

        }
    }
    

    public static void StartClient(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            // IN (to receive a cookie from Server)
             DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // OUT (to send a Scanner Input to Server, normal for Client)
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            
            Scanner sc = new Scanner(System.in); //To take user input 
            boolean stop = false;

            while (!stop) {
                String line= sc.nextLine(); //references user input as a String
                if(line.equalsIgnoreCase("exit")) {
                    dos.writeUTF("exit");
                    dos.flush();
                    stop = true;
                break;
                }

                if(line.equalsIgnoreCase("get-cookie")) {
                //Send request to cserver for a cookie
                dos.writeUTF("get-cookie");
                dos.flush();
                } else {
                    System.out.println("Invalid Command: " + line);
                }

                String fromServer = dis.readUTF(); //receives random cookie from server and converts into a String
                System.out.println("Resp from server !" + fromServer);

            }

            sc.close();
            socket.close();


        } catch (UnknownHostException e){
            e.printStackTrace();

        } catch (IOException e){
            e.printStackTrace();


        }
    }

    public static void StartMultiThreaderServer(int port, String fileName) {
        ServerSocket server;
        try {
            server = new ServerSocket(port);

            while (true) {
                Socket socket = server.accept();//server accept connection is now in while loop
                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                Thread tsh = new ThreadSocketHandler(socket, dis, dos); //Calls for ThreadSocketHandler Class
                tsh.start(); //start thread
            }

        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
 




}