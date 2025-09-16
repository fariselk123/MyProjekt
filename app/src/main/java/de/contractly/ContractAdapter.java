package de.contractly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractViewHolder> {

    private List<Contract> contracts;

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contract_card, parent, false);
        return new ContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractViewHolder holder, int position) {
        Contract currentContract = contracts.get(position);
        holder.contractName.setText(currentContract.contractName);
        holder.provider.setText(currentContract.provider);
        holder.monthlyCost.setText(String.format("%.2f€/Monat", currentContract.monthlyCost));
    }

    @Override
    public int getItemCount() {
        return contracts != null ? contracts.size() : 0;
    }

    static class ContractViewHolder extends RecyclerView.ViewHolder {
        private final TextView contractName;
        private final TextView provider;
        private final TextView monthlyCost;

        ContractViewHolder(View view) {
            super(view);
            contractName = view.findViewById(R.id.textViewContractName);
            provider = view.findViewById(R.id.textViewProvider);
            monthlyCost = view.findViewById(R.id.textViewMonthlyCost);
        }
    }
}