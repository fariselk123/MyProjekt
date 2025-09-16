package de.contractly;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contracts")
public class Contract {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "contract_name")
    public String contractName;

    @ColumnInfo(name = "provider")
    public String provider;

    @ColumnInfo(name = "monthly_cost")
    public double monthlyCost;

    @ColumnInfo(name = "start_date")
    public String startDate;

    @ColumnInfo(name = "end_date")
    public String endDate;

    @ColumnInfo(name = "notes")
    public String notes;
}