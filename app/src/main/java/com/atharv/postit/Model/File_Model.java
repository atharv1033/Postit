package com.atharv.postit.Model;

import android.net.Uri;

public class File_Model {

    String DisplayName;
    Uri file;

    public File_Model(String displayName, Uri file) {
        DisplayName = displayName;
        this.file = file;
    }

    public File_Model() {
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public Uri getFile() {
        return file;
    }

    public void setFile(Uri file) {
        this.file = file;
    }
}
