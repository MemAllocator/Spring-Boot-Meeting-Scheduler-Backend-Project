package com.exercise.MeetingScheduler.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "meetings")
public class MeetingRoom {
    @Id
    private int id;
    private String name;


    public MeetingRoom(int id, String name) {
        this.id = id;
        this.name = name;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "MeetingRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
