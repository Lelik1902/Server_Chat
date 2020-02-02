package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<UserMessage> messages = new ArrayList<>();
    public static List<User> users = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    static class Server extends Thread {
        ServerSocket serverSocket;

        Server() throws IOException {
            serverSocket = new ServerSocket(9000);
        }

        @Override
        public void run() {
            runWriteMessage();
            try {
                users.add(new User(serverSocket.accept(), "UserName", users.size())); /*С самого нчала ожидаем
                подключения пользователя, после присваиваем ему его логин и его ид(с помощью размера листа) */
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void writeMessage() {
            while (true) {
                synchronized (this) { /* с помошью synchronized не даём права запускать след.
                    поток перед тем как не выполним этот */
                    if (!messages.isEmpty()) {
                        String temp = messages.get(messages.size() - 1).message;
                        for (User user : users) {
                            user.sendMessage(messages.get(messages.size() - 1).name +
                                    ": " + messages.get(messages.size() - 1).message);
                        }
                        System.out.println(messages.get(messages.size() - 1).name +
                                ": " + messages.get(messages.size() - 1).message);
                        messages.remove(messages.size() - 1);
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

        public void runWriteMessage() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    writeMessage();
                }
            }).start();
        }
    }
}
