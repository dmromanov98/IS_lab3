package Client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

    private static Socket socket;
    private static OutputStream out;
    private static InputStream in;
    private static final int PORT = 14653;
    private String IP = "127.0.0.1";
    private static String message = null;

    public void CloseSocket(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"ERROR close connection");
        }
    }

    @Override
    public void run() {
        byte buf[] = new byte[64 * 1024];
        try {

            socket = new Socket(IP, PORT);
            out = socket.getOutputStream();
            in = socket.getInputStream();

            JOptionPane.showMessageDialog(null,"You have been conneted to server!");

            while (true) {
                if (message != null) {

                    out.write(message.getBytes());
                    String answer = new String(buf, 0, in.read(buf));

                    //ответ
                    JOptionPane.showMessageDialog(null,answer);
                    message = null;

                }
                Thread.sleep(500);
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(null,"Cant connect to server");

        } catch (InterruptedException e) {

            JOptionPane.showMessageDialog(null,"Error waiting message(Thread sleep)");

        }catch (StringIndexOutOfBoundsException e){}
    }

    public static void sendMessage(String number, String command) {
        try{
            int k = Integer.parseInt(number);
            Client.message = command+"$"+number;
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Text for server - ЧИСЛО!");
        }
    }

    public static void sendCommand(String command){
        Client.message = command+"$";
    }
}
