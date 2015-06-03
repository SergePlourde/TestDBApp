package com.unitedforhealth_softwaredevelopment.testdbapp;

/**
 * Created by Serge on 04/04/2015.
 */
public class ActivityCombo {
    /*
    activityComboId INTEGER PRIMARY KEY, " +
                "activityId INTEGER, " +
                "activityGroupId
     */

    private int activityComboId;
    private int activityId;
    private int activityGroupId;
    private String activityName;
    private String activityGroupName;

    public ActivityCombo(int activityComboId, int activityId, int activityGroupId, String activityName, String activityGroupName) {
        this.activityComboId = activityComboId;
        this.activityId = activityId;
        this.activityGroupId = activityGroupId;
        this.activityName = activityName;
        this.activityGroupName = activityGroupName;
    }


    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityGroupName() {
        return activityGroupName;
    }

    public void setActivityGroupName(String activityGroupName) {
        this.activityGroupName = activityGroupName;
    }


    public int getActivityComboId() {
        return activityComboId;
    }

    public void setActivityComboId(int activityComboId) {
        this.activityComboId = activityComboId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getActivityGroupId() {
        return activityGroupId;
    }

    public void setActivityGroupId(int activityGroupId) {
        this.activityGroupId = activityGroupId;
    }

    @Override
    public String toString() {
        return getActivityName() + " / " + getActivityGroupName();
    }
}
