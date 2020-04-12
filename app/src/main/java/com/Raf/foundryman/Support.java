package com.Raf.foundryman;

import android.content.Context;
import android.content.Intent;

public class Support {
    static final double WELL_HEIGHT = 0.1;
    static final double G = 9.8;
    static final double DEFAULT_SF = 20;
    static Double TOP_SPRUE_VEL = Math.sqrt(2 * G * WELL_HEIGHT);
    static Double[] dataOutput = new Double[8];
    static double safetyFactor = 0.0;

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
        if (Values.getSprueTypeSelected() == 0) {
            if (dataOutput[2] != null) {
                if (dataOutput[0] != null) {
                    updateData(1); // has height and top area

                } else if (dataOutput[3] != null) {
                    // has height and bottom area
                    dataOutput[7] = 1.0;
                } else if (dataOutput[6] != null) {
                    // has height and
                    dataOutput[7] = 1.0;
                }
            }
        } else if (Values.getSprueTypeSelected() == 1) {
            if (dataOutput[2] != null) {
                if (dataOutput[0] != null) {
                    // has height and top area
                    dataOutput[7] = 1.0;
                } else if (dataOutput[3] != null) {
                    // has height and bottom area
                    dataOutput[7] = 1.0;
                } else if (dataOutput[6] != null) {
                    // has height and
                    dataOutput[7] = 1.0;
                }
            }
            } else {
                if (dataOutput[2] != null) {
                    double ratio;
                    if (dataOutput[0] != null && dataOutput[1] != null) {
                        ratio = dataOutput[0] / dataOutput[1];
                        // has height and top area
                        dataOutput[7] = 1.0;
                    } else if (dataOutput[3] != null && dataOutput[4] != null) {
                        ratio = dataOutput[3] / dataOutput[4];
                        // has height and bottom area
                        dataOutput[7] = 1.0;
                    }
                }
            }
        return dataOutput;
    }

    //  Fill the remaining data if all the criteria has been fulfilled
    private static void updateData(int methodPick){

        // Assume default safety factor if no factor selected
        if (dataOutput[5] == null) {
            dataOutput[5] = DEFAULT_SF;
            safetyFactor = dataOutput[5];
        }

        if (Values.getSprueTypeSelected() == 0) {
            switch (methodPick) {
                case 1: // top diameter & height
                    dataOutput[7] = 1.0;
                    break;
                case 2:
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
        } else if (Values.getSprueTypeSelected() == 1){
            // square sprue
        } else {
            // rectangular sprue
        }
    }

    private static Double getTopArea (){
        Double area;
        if (Values.getSprueTypeSelected() == 0){
            area = Math.PI * (dataOutput[0] / 2) * (dataOutput[0] / 2);
        } else if (Values.getSprueTypeSelected() ==1) {
            area = dataOutput[0] * dataOutput[0];
        } else {
            area = dataOutput[0] * dataOutput[1];
        }
        return area;
    }

    private static Double getBottomArea (){
        Double area;
        if (Values.getSprueTypeSelected() == 0){
            area = Math.PI * (dataOutput[3] / 2) * (dataOutput[3] / 2);
        } else if (Values.getSprueTypeSelected() ==1) {
            area = dataOutput[3] * dataOutput[3];
        } else {
            area = dataOutput[3] * dataOutput[4];
        }
        return area;
    }

    // returns the velocity at the bottom of the sprue based on sprue height and safety factor
    private static Double getBottomVelocity (){
        Double velocity = Math.sqrt(2 * G * dataOutput[2]) * (1 + safetyFactor/100);
        return velocity;
        
    }
}

