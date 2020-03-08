package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class User extends Thread {

    private String login;
    private Socket socket;
    private int id;
    PrintWriter out;
    BufferedReader in;

    public User(Socket socket, String login, int id) throws IOException {
        this.socket = socket;
        this.login = login;
        this.id = id;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.start();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getUserId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public void run() {
//        sendMessage("Server: Hello! Input the /help command if you want to search all the commands.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {

            try { /* Делаем так что бы пользователь с помощью команды
                           /changename  сменить имя и оно было бы видимо всем */
                String temp = in.readLine();
                if (temp.isEmpty()) {
                    sendMessage(getLogin() + ": ");
                    continue;
                }
                if (temp.charAt(0) == '/') {
                    String cmd = temp.split(":")[0].trim();
                    if (cmd.equalsIgnoreCase("/changeName")) {
                        String name;
                        name = temp.split(":")[1].trim();
                        String old_name = getLogin();
                        setLogin(name);
                        sendMessage(old_name + ": changed name to " + name);
                        continue;
                    }
                    if (cmd.equalsIgnoreCase("/help")) {
                        sendMessage("Server: /changeName - if you want to change your name on the server");
                        Thread.sleep(500);
                        sendMessage("Server: /q - if you want to go out");
                        Thread.sleep(500);
                        sendMessage("Server: /color - if you want to change your name color");
                        continue;
                    }
                    if (cmd.equalsIgnoreCase("/q")) {
                        sendMessage("Server: Вы вышли из сервера! \uD83D\uDE2D");
                        break;
                    }
                }
                synchronized (this) { /* с помошью synchronized не даём права запускать след.
                     поток перед тем как не выполним этот */
                    Main.messages.add(new UserMessage(login, id, new Date(), temp));
                }
            } catch (SocketException e) {
                e.printStackTrace();
                sendMessage(login+"left from the chat.");// При выходи пользователя сообщается всем
                Main.users.remove(this);
                break;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

