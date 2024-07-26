package com.example.cheersfor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cheersfor.R;
import com.example.cheersfor.models.Company;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>{

    private List<Company> companies;
    private OnCompanyClickListener onCompanyClickListener;

    public CompanyAdapter(List<Company> companies, OnCompanyClickListener onCompanyClickListener) {
        this.companies = companies;
        this.onCompanyClickListener = onCompanyClickListener;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_item, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        Company company = companies.get(position);
        holder.companyTextView.setText(company.getName());
        holder.itemView.setOnClickListener(v -> onCompanyClickListener.onCompanyClick(company));
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public void updateCompanies(List<Company> companies) {
        this.companies = companies;
        notifyDataSetChanged();
    }

    public interface OnCompanyClickListener {
        void onCompanyClick(Company company);
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        public TextView companyTextView;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyTextView = itemView.findViewById(R.id.companyNameTextView);
        }
    }
}
