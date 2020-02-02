package com.company;

import java.util.Date;

public class UserMessage {/* В этом классе мы добивили те переменные которые в дальнейшем
                                                            будут присваиватся пользователю */
    String name;
    int id;
    Date date; // дата отправки сообщения
    String message; // само сообщение

    public UserMessage(String name, int id, Date date, String message) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}
