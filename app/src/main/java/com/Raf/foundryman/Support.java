package com.Raf.foundryman;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Support {
    static final double WELL_HEIGHT = 0.1;
    static final double G = 9.8;
    static final double DEFAULT_SF = 20;
    static Double[] dataOutput = new Double[8];
    static double safetyFactor = 0.0;
    static double densityPlaceholder = 2.5; //     <----- to be replaced once alloy pick is in place
    static boolean[] modified = {false, false, false, false, false, false, false};

    // select correct activity based on the spinner selection
    public static void spinnerNavigator(Context context, int position) {

        switch (position) {
            case 0:
                context.startActivity(new Intent(context, MainActivity.class));
                break;
            case 1:
                context.startActivity(new Intent(context, SprueActivity.class));
                break;
            case 2:
                context.startActivity(new Intent(context, LShapeActivity.class));
                break;
            case 3:
                context.startActivity(new Intent(context, RatiosActivity.class));
                break;
            case 4:
                context.startActivity(new Intent(context, CastingActivity.class));
                break;
            case 5:
                context.startActivity(new Intent(context, FeedersActivity.class));
                break;
            case 6:
                context.startActivity(new Intent(context, ToolsActivity.class));
                break;
        }
    }

    // tests the inputData. If enough data is present, calculates remaining values and returns
    // dataOutput[7] equal to 1.0 which marks that the data can be computed
    public static Double[] computeSprue(Double[] dataInput) {
        dataOutput = dataInput;

        // circular sprue
        if (Values.getSprueTypeSelected() == 0 || Values.getSprueTypeSelected() == 1) {
            if (modified[2]) {
                if (modified[0] && !modified[3] && !modified[6]) {
                    updateData(1); // has height and top area
                    Log.d("TAG 1", "passed");

                } else if (modified[3] && !modified[0] && !modified[6]) {
                    updateData(2); // has height and bottom area
                    dataOutput[7] = 1.0;
                    Log.d("TAG 2", "passed");

                } else if (modified[6] && !modified[0] && !modified[3]) {
                    // has height and flow rate
                    dataOutput[7] = 1.0;
                    Log.d("TAG 3", "passed");

                } else {
                    dataOutput[7] = 0.0;
                    Log.d("TAG 4", "passed");
                    Log.d("dataInput[0]", String.valueOf(dataInput[0]));
                    Log.d("dataInput[3]", String.valueOf(dataInput[3]));
                    Log.d("dataInput[6]", String.valueOf(dataInput[6]));
                }
            }

            // rectangular (not square) sprue
            } else {
                if (dataOutput[2] != null) {
                    if (dataOutput[0] != null && dataOutput[1] != null && ((dataOutput[3] != null) ^
                            (dataOutput[4] != null))) {
                        updateData(1);// has height, top area, and one of bottom dimensions
                        dataOutput[7] = 1.0;
                    } else if (dataOutput[3] != null && dataOutput[4] != null) {
                        updateData(2);// has height and bottom area and one of the top dimensions
                        dataOutput[7] = 1.0;
                    }  else dataOutput[7] = 0.0;
                }
            }
        return dataOutput;
    }

    //  Fill the remaining data if all the criteria has been fulfilled
    private static void updateData(int methodPick){

        Log.d("LOOK AT ME! ------->", "METHOD PICKED: " + methodPick);
        // Assume default safety factor if no factor selected
        if (dataOutput[5] == null) {
            dataOutput[5] = DEFAULT_SF;
            safetyFactor = dataOutput[5];
        }

        if (Values.getSprueTypeSelected() == 0) {
            switch (methodPick) {
                case 1: // top diameter & height
                    double bottomArea = (getTopArea() * getTopVelocity()) / getBottomVelocity();
                    dataOutput[3] = Math.sqrt(Math.PI / bottomArea) * 2;
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
                    break;
                case 2: // bottom diameter & height
                    double topArea = (getBottomArea() * getBottomVelocity())/ getTopVelocity();
                    dataOutput[0] = Math.sqrt(Math.PI / topArea) * 2;
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;

                    dataOutput[7] = 1.0;
                    break;
                case 3:
                    dataOutput[7] = 1.0;
                    break;
                case 4:
                    dataOutput[7] = 1.0;
                    break;
                case 5:
                    dataOutput[7] = 1.0;
                    break;
                case 6:
                    dataOutput[7] = 1.0;
                    break;
            }

            // square sprue
        } else if (Values.getSprueTypeSelected() == 1){
            switch (methodPick) {
                case 1: // top diameter & height
                    double bottomArea = (getTopArea() * getTopVelocity()) / getBottomVelocity();
                    dataOutput[3] = Math.sqrt(bottomArea);
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
                    break;

                case 2:
                    double topArea = (getBottomArea() * getBottomVelocity())/ getTopVelocity();
                    dataOutput[0] = Math.sqrt(topArea);
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
            }

            // rectangular sprue
        } else {
            switch (methodPick) {
                case 1: // top diameter & height
                    double bottomArea = (getTopArea() * getTopVelocity()) / getBottomVelocity();
                    if (dataOutput[3] != null) dataOutput[4] = bottomArea / dataOutput[3];
                    else dataOutput[3] = bottomArea / dataOutput[4];
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
                    break;

                case 2:
                    double topArea = (getBottomArea() * getBottomVelocity())/ getTopVelocity();
                    if (dataOutput[0] != null) dataOutput[1] = topArea / dataOutput[0];
                    else dataOutput[0] = topArea / dataOutput[1];
                    dataOutput[0] = Math.sqrt(topArea);
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
            }
        }
    }

    private static Double getTopArea(){
        Double area;
        if (Values.getSprueTypeSelected() == 0){
            area = Math.PI * ((dataOutput[0]/1000) / 2) * ((dataOutput[0] / 1000) / 2);
        } else if (Values.getSprueTypeSelected() ==1) {
            area = dataOutput[0] * dataOutput[0] / 1000000;
        } else {
            area = dataOutput[0] * dataOutput[1] / 1000000;
        }
        Log.d("getTopArea: ", String.valueOf(area));
        return area;
    }

    private static Double getBottomArea(){
        Double area;
        if (Values.getSprueTypeSelected() == 0){
            area = Math.PI * ((dataOutput[3] / 1000) / 2) * ((dataOutput[3] /1000) / 2);
        } else if (Values.getSprueTypeSelected() ==1) {
            area = dataOutput[3] * dataOutput[3] / 1000000;
        } else {
            area = dataOutput[3] * dataOutput[4] / 1000000;
        }
        Log.d("getBottomArea: ", String.valueOf(area));
        return area;
    }

    // returns the velocity at the bottom of the sprue based on sprue height and safety factor
    private static Double getBottomVelocity (){
        Double velocity = 0.0;
        if (dataOutput[2] != null) {
            velocity = (Math.sqrt(2 * G * 0.1) + Math.sqrt(2 * G * (dataOutput[2] / 1000))) *
                    (1 + safetyFactor / 100);
            Log.d("getBottomVelocity: ", String.valueOf(velocity));
        }
        return velocity;
    }
    // returns Volumetric flow rate in kg/s
    private static Double getFlow() {
        Double flowRate;
        if (dataOutput[0] != null){
            flowRate = getTopArea() * getTopVelocity() * densityPlaceholder;
        } else if (dataOutput[2] != null){
            flowRate = getBottomArea() * getBottomVelocity() * densityPlaceholder;
        } else {
            flowRate = 0.0;
            Log.d("getFlow: ", "Flowrate not calculated correctly due to method called " +
                    "despite missing data");
        }
        Log.d("getFlow: ", String.valueOf(flowRate));
        return flowRate;
    }

    //returns Velocity at the top of the sprue based on metal level in the well. Does not take under
    //account the drag in the sprue caused by safety factor applied
    private static Double getTopVelocity() {
        Double velocity = Math.sqrt(2 * G * WELL_HEIGHT);
        Log.d("getTopVelocity: ", String.valueOf(velocity));
        return velocity;
    }
}

