package com.Raf.foundryman;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import java.util.ArrayList;

public class Support {

    // INTERNAL VARIABLES
    static final String projectListAddress = "projectList.data";

    // GENERAL VARIABLES
    // static double totalWeight = 0.0; --> calculated in summary

    // from casting activity
    static int sprues = 1;
    static int partsPerMould = 1;
    static String materialName = "";
    static String materialType = "Aluminium";
    static double density = 2400;

    // from l-shape activity
    static boolean diaDim = true;
    static double initialMassFlowrate = 0.0;
    static double initialVolFlowrate = 0.0;
    static double sprueVelocity = 0.0;
    static double sprueHeight = 0.0;
    static double LshapeRad = 0.0;
    static double runnerHeight = 0.0;
    static double sprueWidth = 0.0;

    // from runner bar activity
    static int runnerArms = 1;
    static int wells = 1;
    static int wellType = 0;
    static boolean wellFilterState = false;
    static boolean ingateFilterSwitch = false;
    static double runnerArmLength;
    static int ingateCount;
    static double runnerWidth;
    static double runnerStartHeight;
    static double runnerEndHeight;
    static double wellFilter1;
    static double wellFilter2;
    static double ingateFilter1;
    static double ingateFilter2;
    static double runnerMass;

    // from sprue activity
    static Double[] sprueValArray = new Double[8];
    static double ingateHeight = 0.0;
    static double ingateDia = 0.0;
    static double totalFeederMass;
    static double castingMass;
    static final double WELL_HEIGHT = 0.1;
    static final double G = 9.8;
    static final double DEFAULT_SF = 20;
    static Double[] dataOutput = new Double[8];
    static double safetyFactor = 0.0;

        // created for save/load purposes
    static double sprueVal0;
    static double sprueVal1;
    static double sprueVal2;
    static double sprueVal3;
    static double sprueVal4;
    static double sprueVal5;
    static double sprueVal6;
    static double sprueVal7;

    static boolean[] modified = {false, false, false, false, false, false, false};

    // recycler view content in Feeder Activity
    static ArrayList<Integer> feederIndex = new ArrayList<>();
    static ArrayList<String> feederTypeName = new ArrayList<>();
    static ArrayList<Integer> feederAmount = new ArrayList<>();
    static ArrayList<Double> feederDiameter = new ArrayList<>();
    static ArrayList<Double> feederHeight = new ArrayList<>();
    static ArrayList<Double> feederMod = new ArrayList<>();
    static ArrayList<Double> feederMass = new ArrayList<>();
    static ArrayList<Button> removeBtn = new ArrayList<>();
    static int feederBtnCounter = 0;

    // recycler view content in Save Activity
    static ArrayList<String> saveName = new ArrayList<>();
    static ArrayList<String> saveMatType = new ArrayList<>();
    static ArrayList<String> saveMatName = new ArrayList<>();

    static ArrayList<Integer> projectIndex = new ArrayList<>();

    public static void removeFeederLine (int position){
        feederIndex.remove(position);
        feederAmount.remove(position);
        feederTypeName.remove(position);
        feederDiameter.remove(position);
        feederHeight.remove(position);
        feederMod.remove(position);
        feederMass.remove(position);
        recalculateFeederMass();
    }

    public static void removeSaveLine(int position) {
        projectIndex.remove(position);
        saveName.remove(position);
        saveMatType.remove(position);
        saveMatName.remove(position);
    }

    public static int saveLinePosition(int position){
        int index = projectIndex.indexOf(position);
        return index;
    }

    public static int recLinePosition(int position){
        int index = feederIndex.indexOf(position);
        return index;
    }

    public static void recalculateFeederMass() {
        totalFeederMass = 0;
        double massSum = 0;
        if (!feederMass.isEmpty()) {
            for (int i = 0; i < feederMass.size(); i++) {
                massSum += feederMass.get(i);
            }
        }

        massSum *=100;
        massSum = Math.round(massSum);
        totalFeederMass = massSum / 100;
    }

    public static double modulus(double height, double diameter, int insulation){
        double multiplier;
        double radius = diameter / 2;
        if (insulation == 0){
            multiplier = 1.0;
        } else if(insulation == 1){
            multiplier = 1.4;
        } else if(insulation == 2){
            multiplier = 1.9;
        } else{
            multiplier = 0.0;
        }

        double modulus = multiplier * ((Math.PI* radius * radius * height) /
                (Math.PI * radius * radius + (2 * Math.PI * radius * height)));

        return round(modulus, 2);
    }

    // rounds double value to n number of places
    public static double round(double number, int decPlace){
        String multiplier = "1";
        if (decPlace >= 0){
            for (int i=0; i<decPlace; i++){
                multiplier += "0";
            }
        }
        decPlace = Integer.valueOf(multiplier);
        number *= decPlace;
        number = Math.round(number);
        number /= decPlace;
        return number;
    }

    // select correct activity based on the spinner selection
    public static void spinnerNavigator(Context context, int position) {

        switch (position) {
            case 0:
                context.startActivity(new Intent(context, CastingActivity.class));
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
                context.startActivity(new Intent(context, FeedersActivity.class));
                break;
            case 5:
                context.startActivity(new Intent(context, SummaryActivity.class));
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
            velocity = Math.sqrt(2 * G * (0.1 +(dataOutput[2] / 1000))) *
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