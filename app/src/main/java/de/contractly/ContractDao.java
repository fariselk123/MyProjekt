package de.contractly;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContractDao {

    @Insert
    void insertContract(Contract contract);

    @Query("SELECT * FROM contracts")
    List<Contract> getAllContracts();

    @Query("SELECT SUM(monthly_cost) FROM contracts")
    double getTotalMonthlyCost();
}