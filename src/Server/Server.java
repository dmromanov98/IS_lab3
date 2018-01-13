package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

    private static ServerSocket socket;
    private static Socket socketclient;
    private static OutputStream out;
    private static InputStream in;
    private static final int PORT = 14653;
    private static ArrayList<Integer> array;
    private static WriteToFile wfile;

    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        array = new ArrayList<>();
        byte buf[] = new byte[64 * 1024];
        boolean b;
        try {
            socket = new ServerSocket(PORT);

            while (true) {
                System.out.println("Watiting for clients");
                socketclient = socket.accept();
                wfile = new WriteToFile();
                System.out.println("Client has been connected : " + socketclient.getInetAddress().getHostAddress() + ":" + socketclient.getPort());
                wfile.AddToFile("IP: " + socketclient.getInetAddress().getHostAddress() + " PORT: " + socketclient.getPort());
                wfile.AddToFile("-------------------------------------------------------------------");

                b = true;

                out = socketclient.getOutputStream();
                in = socketclient.getInputStream();

                while (b) {

                    int r = in.read(buf);

                    String messageFromClient = new String(buf, 0, r);

                    String command = messageFromClient.substring(0, messageFromClient.lastIndexOf('$')),
                            number = messageFromClient.substring(messageFromClient.lastIndexOf('$') + 1);

                    switch (command) {
                        case ("GPN"):
                            out.write(getPrimeNumbers().getBytes());
                            break;
                        case ("STS"):
                            out.write(addNumber(number).getBytes());
                            break;
                        case ("AC"):
                            out.write(clear().getBytes());
                            break;
                        case ("GN"):
                            out.write(getNumber(number).getBytes());
                            break;
                        case ("CLS"):
                            CloseConnectin();
                            break;
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("ERROR Connection interrupted!");
            wfile.close();
        } catch (IOException e) {
            System.out.println("ERROR Can't create ServerSocket!");
            wfile.close();
        }
    }

    private static void CloseConnectin() {

        try {
            socketclient.close();
        } catch (IOException e) {
            System.out.println("ERROR disconnecting client");
        }

        wfile.AddToFile("Клиент отключился!");
        System.out.println("Клиент отключился");
        wfile.close();

    }

    private static String getNumber(String pos) {
        String result;
        int n = 0;
        try {
            n = Integer.parseInt(pos);
            result = String.valueOf(array.get(n));
            wfile.AddToFile("Клиент запросил строку с номером " + (String.valueOf(n) + 1) + " Ответ сервера : " + result);
            wfile.AddToFile("-------------------------------------------------------------------");
            outArray();

        } catch (IndexOutOfBoundsException ex) {

            System.out.println("ERROR IndexOutOfBoundsException! Index of element is invalid!");
            result = "ERROR Index of element is invalid!";

            wfile.AddToFile("Клиент запросил строку с номером " + (String.valueOf(n) + 1) + " Ответ сервера : " + result);
            wfile.AddToFile("-------------------------------------------------------------------");
            outArray();

        }

        return result;
    }

    private static String getPrimeNumbers() {
        String result = "[";
        boolean prime;
        for (Integer i : array) {
            prime = true;
            if (i < 2)
                prime = false;
            for (int j = 2; j <= i / 2; j++) {
                if (i % j == 0) {
                    prime = false;
                    break;
                }
            }
            if (prime)
                result = result + " " + String.valueOf(i);
        }
        result = result + " ]";
        if (result.length() == 2)
            result = "Нет простых чисел";
        wfile.AddToFile("Клиент запросил простые числа массива. Ответ сервера : " + result);
        wfile.AddToFile("-------------------------------------------------------------------");
        return result;
    }

    private static String clear() {
        array.clear();

        wfile.AddToFile("Клиент запросил очистить массив . Ответ сервера : Массив очищен");
        wfile.AddToFile("-------------------------------------------------------------------");
        outArray();

        return "Массив очищен";
    }

    private static String addNumber(String s) {
        array.add(Integer.parseInt(s));

        wfile.AddToFile("Клиент запросил добавить число : <<" + s + ">> В массив . Ответ сервера : Число добавлено в массив");
        wfile.AddToFile("-------------------------------------------------------------------");
        outArray();
        String res = "[";
        for (Integer i : array) {
            res = res + " " + String.valueOf(i);
        }
        res = res + " ]";

        return "Число добавлено в массив. Ваш массив : " + res;
    }

    private static void outArray() {
        wfile.AddToFile("Эллементы массива : ");
        for (Integer s : array)
            wfile.AddToFile(String.valueOf(s));
        wfile.AddToFile("-------------------------------------------------------------------");
    }

}
