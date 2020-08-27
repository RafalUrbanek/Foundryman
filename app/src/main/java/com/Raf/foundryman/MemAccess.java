package com.Raf.foundryman;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MemAccess extends Support{

    static ArrayList<String> values = new ArrayList<>();

    public static void importProject(Context context, String projectName){
        Values.setProjectName(projectName);
        SavesActivity.projectText.setText(projectName);
        String projectData = load(context, projectName);

        StringBuilder rawData = new StringBuilder(projectData);
        int index = 0;
        int counter = 0;
        int newIndex;

        while(true){
            newIndex = rawData.indexOf("|", index + 1);
            if (newIndex == -1){
                break;
            }
            if (counter == 1){
                counter = 0;
                if (index == newIndex -1){
                    values.add(" ");
                } else {
                    values.add(rawData.substring(index + 1, newIndex));
                }
            } else {
                counter = 1;
            }

            index = newIndex;
        }
        uploadValues(values);
    }

    public static void uploadValues(ArrayList<String> values) {
        Values.setProjectName(values.get(0));
        Support.sprues = Integer.valueOf(values.get(1));
        Support.partsPerMould = Integer.valueOf(values.get(2));
        Support.materialType = (values.get(3));
        Support.materialName = (values.get(4));
        Support.density = Double.valueOf(values.get(5));
        Support.diaDim = Boolean.valueOf(values.get(6));
        Support.initialMassFlowrate = Double.valueOf(values.get(7));
        Support.initialVolFlowrate = Double.valueOf(values.get(8));
        Support.sprueVelocity = Double.valueOf(values.get(9));
        Support.sprueHeight = Double.valueOf(values.get(10));
        Support.LshapeRad = Double.valueOf(values.get(11));
        Support.runnerHeight = Double.valueOf(values.get(12));
        Support.sprueWidth = Double.valueOf(values.get(13));
        Support.runnerArms = Integer.valueOf(values.get(14));
        Support.wells = Integer.valueOf(values.get(15));
        Support.wellType = Integer.valueOf(values.get(16));
        Support.wellFilterState = Boolean.valueOf(values.get(17));
        Support.ingateFilterSwitch = Boolean.valueOf(values.get(18));
        Support.runnerArmLength = Double.valueOf(values.get(19));
        Support.ingateCount = Integer.valueOf(values.get(20));
        Support.runnerWidth = Double.valueOf(values.get(21));
        Support.runnerStartHeight = Double.valueOf(values.get(22));
        Support.runnerEndHeight = Double.valueOf(values.get(23));
        Support.wellFilter1 = Double.valueOf(values.get(24));
        Support.wellFilter2 = Double.valueOf(values.get(25));
        Support.ingateFilter1 = Double.valueOf(values.get(26));
        Support.ingateFilter2 = Double.valueOf(values.get(27));
        Support.runnerMass = Double.valueOf(values.get(28));
        Support.ingateHeight = Double.valueOf(values.get(29));
        Support.ingateDia = Double.valueOf(values.get(30));
        Support.totalFeederMass = Double.valueOf(values.get(31));
        Support.castingMass = Double.valueOf(values.get(32));
        Support.sprueVal0 = Double.valueOf(values.get(33));
        Support.sprueVal1 = Double.valueOf(values.get(34));
        Support.sprueVal2 = Double.valueOf(values.get(35));
        Support.sprueVal3 = Double.valueOf(values.get(36));
        Support.sprueVal4 = Double.valueOf(values.get(37));
        Support.sprueVal5 = Double.valueOf(values.get(38));
        Support.sprueVal6 = Double.valueOf(values.get(39));
        Support.sprueVal7 = Double.valueOf(values.get(40));
        Support.safetyFactor = Double.valueOf(values.get(41));
    }

    public static void exportProject(Context context){

        String projectDescription = "~" + Values.getProjectName() + "|" + materialType + "|" + materialName;
        save(context, Support.projectListAddress, projectDescription);

        String projectData = "|name|" + Values.getProjectName() + "|sprues|" + sprues
                + "|partsPerMould|" + partsPerMould  + "|materialType|" + materialType
                + "|materialName|" + materialName + "|density|" + density  + "|diaDim|" + diaDim
                + "|initialMassFlowrate|" + initialMassFlowrate + "|initialVolFlowrate|" + initialVolFlowrate
                + "|sprueVelocity|" + sprueVelocity + "|sprueHeight|" + sprueHeight
                + "|LshapeRad|" + LshapeRad + "|runnerHeight|" + runnerHeight + "|sprueWidth|" + sprueWidth
                + "|runnerArms|" + runnerArms + "|wells|" + wells + "|wellType|" + wellType
                + "|wellFilterState|" + wellFilterState + "|ingateFilterSwitch|" + ingateFilterSwitch
                + "|runnerArmLength|" + runnerArmLength + "|ingateCount|" + ingateCount
                + "|runnerWidth|" + runnerWidth + "|runnerStartHeight|" + runnerStartHeight
                + "|runnerEndHeight|" + runnerEndHeight + "|wellFilter1|" + wellFilter1
                + "|wellFilter|" + wellFilter2 + "|ingateFilter1|" + ingateFilter1 + "|ingateFilter2|" + ingateFilter2
                + "|runnerMass|" + runnerMass + "|ingateHeight|" + ingateHeight  + "|ingateDia|" + ingateDia
                + "|totalFeederMass|" + totalFeederMass  + "|castingMass|" + castingMass
                + "|sprueVal0|" + sprueVal0 + "|sprueVal1|" + sprueVal1 + "|sprueVal2|" + sprueVal2
                + "|sprueVal3|" + sprueVal3 + "|sprueVal4|" + sprueVal4 + "|sprueVal5|" + sprueVal5
                + "|sprueVal6|" + sprueVal6 + "|sprueVal7|" + sprueVal7 + "|safetyFactor|" + safetyFactor + "|";

        save(context, Values.getProjectName(), projectData);
    }


    public static void save(Context context, String fileName, String value){
        String filename = fileName;
        String data = load (context, fileName) + value;

        FileOutputStream fos;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
    }

    public static String load(Context context, String fileName){
        String filename = fileName;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    context.openFileInput(filename)));
            String inputString;
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

    public static void delete(Context context, String fileName){
        context.deleteFile(fileName);
    }

    public static void removeFromProjectList(Context context, String name) {
        String projects = load(context, Support.projectListAddress);

        int start = projects.indexOf(name) -1;
        int end;
        if (projects.indexOf("~", start + 1) == -1){
            end = projects.length();
        } else {
            end = projects.indexOf("~", start + 1);
        }
        StringBuilder projectsBuilder = new StringBuilder(projects);
        projectsBuilder.delete(start, end);
        String newList = projectsBuilder.toString();
        delete(context, Support.projectListAddress);
        save(context, Support.projectListAddress, newList);
    }
}