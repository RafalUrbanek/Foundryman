package com.Raf.foundryman;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;

public class Support {

    // GENERAL VARIABLES
    static double sprueWidth = 0.0;
    static double feederMass;
    static final double WELL_HEIGHT = 0.1;
    static final double G = 9.8;
    static final double DEFAULT_SF = 20;
    static Double[] dataOutput = new Double[8];
    static double safetyFactor = 0.0;
    static double density = 2500; //     <----- to be replaced once alloy pick is in place
    static boolean[] modified = {false, false, false, false, false, false, false};

    // recycler view content in Feeder Activity
    static ArrayList<String> feederTypeName = new ArrayList<>();
    static ArrayList<Integer> amount = new ArrayList<>();
    static ArrayList<Double> diameter = new ArrayList<>();
    static ArrayList<Double> height = new ArrayList<>();
    static ArrayList<Double> mod = new ArrayList<>();
    static ArrayList<Double> mass = new ArrayList<>();
    static ArrayList<Button> removeBtn = new ArrayList<>();
    static int feederBtnCounter = 0;

    public static void removeFeederLine (int position){
        Log.d("LOG", "position: " + String.valueOf(position));
        amount.remove(position);
        feederTypeName.remove(position);
        diameter.remove(position);
        height.remove(position);
        mod.remove(position);
        mass.remove(position);
        recalculateFeederMass();
    }

    public static void recalculateFeederMass() {
        feederMass = 0;
        double massSum = 0;
        if (!mass.isEmpty()) {
            for (int i = 0; i < mass.size(); i++) {
                massSum += mass.get(i);
            }
        }

        massSum *=100;
        massSum = Math.round(massSum);
        feederMass = massSum / 100;
    }

    public static double modulus(double height, double diameter, int insulation){
        double multiplier;
        if (insulation == 0){
            multiplier = 1.0;
        } else if(insulation == 1){
            multiplier = 1.4;
        } else if(insulation == 2){
            multiplier = 1.9;
        } else{
            multiplier = 0.0;
        }

        double modulus = multiplier * ((diameter * height) / (2 * (diameter + 2 * height)));
        modulus *= 100;
        double modulusRounded = Math.round(modulus);
        return modulusRounded / 100;
    }

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
                context.startActivity(new Intent(context, RunnerActivity.class));
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

                } else if (modified[3] && !modified[0] && !modified[6]) {
                    updateData(2); // has height and bottom area
                    dataOutput[7] = 1.0;

                } else if (modified[6] && !modified[0] && !modified[3]) {
                    updateData(3); // has height and flow rate
                    dataOutput[7] = 1.0;

                } else {
                    dataOutput[7] = 0.0;
                }
            }

            // rectangular (not square) sprue
            } else {
                if (modified[2]) {
                    if (modified[0] && modified[1] && (modified[3] ^ modified[4])) {
                        updateData(1);// has height, top area, and one of bottom dimensions
                        dataOutput[7] = 1.0;
                    } else if (modified[3] && modified[4] && (modified[0] ^ modified[1])) {
                        updateData(2);// has height and bottom area and one of the top dimensions
                        dataOutput[7] = 1.0;
                    }  else dataOutput[7] = 0.0;
                }
            }
        return dataOutput;
    }

    //  Fill the remaining data if all the criteria has been fulfilled
    private static void updateData(int methodPick){

        // Assume default safety factor if no factor selected
        if (modified[5]) {
            safetyFactor = dataOutput[5];
        } else {
            safetyFactor = dataOutput[5] = DEFAULT_SF;
        }

        if (Values.getSprueTypeSelected() == 0) {
            switch (methodPick) {

                case 1: // top diameter & height
                    double bottomArea1 = getTopArea() * getTopVelocity() / getBottomVelocity() * 1000000;
                    dataOutput[3] = Math.sqrt(bottomArea1 / Math.PI) * 2;
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
                    break;
                case 2: // bottom diameter & height
                    double topArea1 = ((getBottomArea() * getBottomVelocity())/ getTopVelocity()) * 1000000;
                    dataOutput[0] = Math.sqrt(topArea1 / Math.PI) * 2;
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
                    break;

                case 3: // height and Flow rate
                    double topArea2 = (dataOutput[6] / getTopVelocity()) * 1000000;
                    double bottomArea2 = (dataOutput[6] / getBottomVelocity()) * 1000000;
                    dataOutput[0] = Math.sqrt(Math.PI / topArea2) * 2;
                    dataOutput[3] = Math.sqrt(Math.PI / bottomArea2) * 2;
                    dataOutput[7] = 1.0;
                    break;
            }

            // square sprue
        } else if (Values.getSprueTypeSelected() == 1){
            switch (methodPick) {
                case 1: // top diameter & height
                    double bottomArea = getTopArea() * getTopVelocity() / getBottomVelocity() * 1000000;
                    dataOutput[3] = Math.sqrt(bottomArea);
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
                    break;

                case 2:
                    double topArea = getBottomArea() * getBottomVelocity()/ getTopVelocity() * 1000000;
                    dataOutput[0] = Math.sqrt(topArea);
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
                    break;
                case 3:
                    double topArea2 = (dataOutput[6] / getTopVelocity()) * 1000000;
                    double bottomArea2 = (dataOutput[6] / getBottomVelocity()) * 1000000;
                    dataOutput[0] = Math.sqrt(topArea2);
                    dataOutput[3] = Math.sqrt(bottomArea2);
                    break;
            }

            // rectangular sprue
        } else {
            switch (methodPick) {
                case 1: // top dimensions & height
                    double bottomArea = getTopArea() * getTopVelocity() / getBottomVelocity() * 1000000;
                    if (modified[3]) dataOutput[4] = bottomArea / dataOutput[3];
                    else dataOutput[3] = bottomArea / dataOutput[4];
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
                    break;

                case 2: // bottom dimensions & height
                    double topArea = getBottomArea() * getBottomVelocity() / getTopVelocity() * 1000000;
                    if (modified[0]) {
                        dataOutput[1] = topArea / dataOutput[0];
                    } else {
                        dataOutput[0] = topArea / dataOutput[1];
                    }
                    //dataOutput[0] = Math.sqrt(topArea);
                    dataOutput[6] = getFlow();
                    dataOutput[7] = 1.0;
                    break;

                case 3:
                    double topArea2 = dataOutput[6] / getTopVelocity();
                    double bottomArea2 = dataOutput[6] / getBottomVelocity();
                    dataOutput[0] = Math.sqrt(topArea2);
                    dataOutput[3] = Math.sqrt(bottomArea2);
                    break;
            }
        }
    }

    private static Double getTopArea(){
        Double area;
        if (Values.getSprueTypeSelected() == 0){
            area = Math.PI * (dataOutput[0] / 2) * (dataOutput[0] / 2) / 1000000;
        } else if (Values.getSprueTypeSelected() ==1) {
            area = dataOutput[0] * dataOutput[0] / 1000000;
        } else {
            area = dataOutput[0] * dataOutput[1] / 1000000;
        }
        return area;
    }

    private static Double getBottomArea(){
        Double area;
        if (Values.getSprueTypeSelected() == 0){
            area = Math.PI * (dataOutput[3] / 2) * (dataOutput[3] / 2) / 1000000;
        } else if (Values.getSprueTypeSelected() ==1) {
            area = dataOutput[3] * dataOutput[3] / 1000000;
        } else {
            area = dataOutput[3] * dataOutput[4] / 1000000;
        }
        return area;
    }

    // returns the velocity at the bottom of the sprue based on sprue height and safety factor
    private static Double getBottomVelocity (){
        Double velocity = 0.0;
        if (modified[2]) {
            velocity = (Math.sqrt(2 * G * 0.1) + Math.sqrt(2 * G * (dataOutput[2] / 1000))) *
                    (1 + safetyFactor / 100);
        }
        return velocity;
    }
    // returns Volumetric flow rate in kg/s
    private static Double getFlow() {
        Double flowRate;
        if (modified[0]){
            flowRate = getTopArea() * getTopVelocity() * density;
        } else if (modified[2]){
            flowRate = getBottomArea() * getBottomVelocity() * density;
        } else {
            flowRate = 0.0;
            Log.d("getFlow: ", "Flowrate not calculated correctly due to method called " +
                    "despite missing data");
        }
        return flowRate;
    }

    //returns Velocity at the top of the sprue based on metal level in the well. Does not take under
    //account the drag in the sprue caused by safety factor applied
    private static Double getTopVelocity() {
        Double velocity = Math.sqrt(2 * G * WELL_HEIGHT);
        return velocity;
    }
}

