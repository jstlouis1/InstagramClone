package com.example.instagramclone;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("PostObject")
public class PostObject extends ParseObject {
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";
    public static final String USER = "user";
    public static final String CREATED_AT = "createdAt";

    public String getDescription(){
        return getString(DESCRIPTION);
    }
    public void setDescription(String description){
        put(DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(IMAGE);
    }
    public void setImage(ParseFile parseFile){
        put(IMAGE, parseFile);
    }

    public ParseUser getUser(){
        return getParseUser(USER);
    }
    public void setUser(ParseUser parseUser){
        put(USER, parseUser);
    }
}
