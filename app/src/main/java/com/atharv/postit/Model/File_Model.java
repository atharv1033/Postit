package com.atharv.postit.Model;

import android.net.Uri;

public class File_Model {

    String name;
    Uri uri;
    String reference;

    public File_Model(String name, String reference) {
        this.name = name;
        this.reference = reference;
    }

    public File_Model(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public File_Model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        name = Name;
    }
}
