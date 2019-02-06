package com.atharv.postit.Model;

public class Posts_Model {

    String id,
           title ,
           content,
           channel_id,
           owner;

    public Posts_Model() {
    }

    public Posts_Model(String id) {
        this.id = id;
    }

    public Posts_Model(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Posts_Model(String title, String content, String channel_id, String owner) {
        this.title = title;
        this.content = content;
        this.channel_id = channel_id;
        this.owner = owner;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
