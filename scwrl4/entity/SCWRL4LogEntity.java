/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scwrl4.entity;

import java.util.ArrayList;

/**
 *
 * @author administrator
 */
public class SCWRL4LogEntity {

    private ArrayList<String> timingTags = new ArrayList<String>();
    private ArrayList<String> timingDurations = new ArrayList<String>();
    private double totalTime = 0;

    public void addLine(String line) {
        if (line.startsWith("<Timing tag = \"")) {
            String[] cols = line.split("\"");
            timingTags.add(cols[1]);
            timingDurations.add(cols[3]);
            totalTime += Double.parseDouble(cols[3]);
        }
    }

    public double getTime(String tag) {
        double result = 0;
        for (int i = 0; i < timingTags.size(); i++) {
            if (timingTags.get(i).equalsIgnoreCase(tag)) {
                result += Double.parseDouble(timingDurations.get(i));
            }
        }
        return result;
    }

    /**
     * @return the totalTime
     */
    public double getTotalTime() {
        return totalTime;
    }
}
