package com.example.crisp.mperaltauf2;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Card {
    public String id;
    public String title;
    public String description;
    public String img;

    public Card(){}

    public Card(String id, String title_string, String context_string, String img) {
        this.title = title_string;
        this.description = context_string;
        this.id = id;
        this.img = img;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("descr", description);
        result.put("img", img);
        return result;
    }
}