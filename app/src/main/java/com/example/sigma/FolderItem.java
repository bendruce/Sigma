// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR THE FOLDERS
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;
public class FolderItem {
    private String folderName;
    private int numberOfWorkouts;

    public FolderItem(String folderName, int numberOfWorkouts) {
        this.folderName = folderName;
        this.numberOfWorkouts = numberOfWorkouts;
    }

    //getters and setters
    public String getFolderName() {
        return folderName;
    }

    public int getNumberOfWorkouts() {
        return numberOfWorkouts;
    }
}

