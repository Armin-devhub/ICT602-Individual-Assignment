package com.example.electricitybillestimator;

import java.io.Serializable;

public class Bill implements Serializable {
    private int id;
    private String month;
    private String year;
    private int unitUsed;
    private double totalCharges;
    private double rebate;
    private double finalCost;

    public Bill(int id, String month, String year, int unitUsed, double totalCharges, double rebate, double finalCost) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.unitUsed = unitUsed;
        this.totalCharges = totalCharges;
        this.rebate = rebate;
        this.finalCost = finalCost;
    }

    public int getId() { return id; }
    public String getMonth() { return month; }
    public String getYear() { return year; }
    public int getUnitUsed() { return unitUsed; }
    public double getTotalCharges() { return totalCharges; }
    public double getRebate() { return rebate; }
    public double getFinalCost() { return finalCost; }
}
