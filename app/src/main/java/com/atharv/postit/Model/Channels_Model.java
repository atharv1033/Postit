package com.atharv.postit.Model;

public class Channels_Model {

    private String id,
                   name,
                   subject,
                   topic,
                   owner;

    public Channels_Model() {
    }

    public Channels_Model(String subject, String topic) {
        this.subject = subject;
        this.topic = topic;
    }

    public Channels_Model(String name, String subject, String topic) {
        this.name = name;
        this.subject = subject;
        this.topic = topic;
    }

    public Channels_Model(String name, String subject, String topic, String owner) {
        this.name = name;
        this.subject = subject;
        this.topic = topic;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
