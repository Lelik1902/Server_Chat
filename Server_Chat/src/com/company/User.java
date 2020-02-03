package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.List;

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
        while (true) {

            try { /* Делаем так что бы пользователь с помощью команды
                           /changename  сменить имя и оно было бы видимо всем */
                String temp = in.readLine();
                if (temp.charAt(0) == '/'){
                    String cmd = temp.split(":")[0].trim();
                    if (cmd.equalsIgnoreCase("/changeName")){
                        String name;
                        name = temp.split(":")[1].trim();
                        System.out.print(login);
                        setLogin(name);
                        System.out.println(" changed login to " + name);
                        continue;
                    }
                    if (cmd.equalsIgnoreCase("/q")){
                        sendMessage("Server: Вы вышли из сервера! \uD83D\uDE00");
                        break;
                    }

                }
                synchronized (this) { /* с помошью synchronized не даём права запускать след.
                     поток перед тем как не выполним этот */
                    Main.messages.add(new UserMessage(login, id, new Date(), temp));
                }
            } catch (SocketException e){
                e.printStackTrace();
                System.out.println("Connection with user " + login + " was lost!");// При выходи пользователя сообщается всем
                Main.users.remove(this);
                break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

