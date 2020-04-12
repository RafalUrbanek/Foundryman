package com.Raf.foundryman;

class Values {

    private static int sprueTypeSelected = 0;
    private static String projectName = "New Project";

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String newName){
        projectName = newName;
    }

    public static int getSprueTypeSelected() { return sprueTypeSelected; }

    public static void setSprueTypeSelected(int sprueTypeSelected) { Values.sprueTypeSelected = sprueTypeSelected; }
}
