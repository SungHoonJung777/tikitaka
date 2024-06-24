package org.fullstack4.tikitaka.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageLine {
    private String message;
    private String nickname;
    private Date date;

    MessageLine(){
        date = new Date();
    }
}