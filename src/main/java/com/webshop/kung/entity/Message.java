package com.webshop.kung.entity;

import javax.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="item_id")
    private Long id;

    @Column(name="message_id")
    private Long messageId;

    @Column(name="key")
    private String key;

    @Column(name="contents")
    private String contents;

    public Message() {}

    public Message(Long messageId, String key, String contents){
        setMessageId(messageId);
        setKey(key);
        setContents(contents);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
