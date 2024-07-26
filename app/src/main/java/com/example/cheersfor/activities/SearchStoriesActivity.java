package com.example.cheersfor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheersfor.R;
import com.example.cheersfor.adapters.CompanyAdapter;
import com.example.cheersfor.adapters.StoryAdapter;
import com.example.cheersfor.managers.StoryManager;
import com.example.cheersfor.managers.UserManager;
import com.example.cheersfor.models.Company;
import com.example.cheersfor.models.Story;
import com.example.cheersfor.interfaces.Callback;

import java.util.ArrayList;
import java.util.List;

public class SearchStoriesActivity extends AppCompatActivity{

    private EditText searchEditText;
    private RecyclerView companiesRecyclerView;
    private RecyclerView storiesRecyclerView;
    private TextView bonusPointsTextView;

    private List<Company> companiesList;
    private List<Story> storiesList;

    private CompanyAdapter companyAdapter;
    private StoryAdapter storyAdapter;

    private StoryManager storyManager;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stories);

        searchEditText = findViewById(R.id.searchEditText);
        companiesRecyclerView = findViewById(R.id.companiesRecyclerView);
        storiesRecyclerView = findViewById(R.id.storiesRecyclerView);
        bonusPointsTextView = findViewById(R.id.bonusPointsTextView);

        companiesList = new ArrayList<>();
        storiesList = new ArrayList<>();

        companyAdapter = new CompanyAdapter(companiesList, company -> loadStoriesForCompany(company));
        storyAdapter = new StoryAdapter(storiesList);

        companiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        companiesRecyclerView.setAdapter(companyAdapter);

        storiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        storiesRecyclerView.setAdapter(storyAdapter);

        storyManager = new StoryManager();
        userManager = new UserManager();
        loadCompanies();
        updateBonusPoints();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCompanies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Verificăm dacă compania a fost trecută ca extra
        Intent intent = getIntent();
        String companyName = intent.getStringExtra("companyName");
        if (companyName != null && !companyName.isEmpty()) {
            filterCompanies(companyName);
        }
    }

    private void loadCompanies() {
        storyManager.getCompanies(new Callback<List<Company>>() {
            @Override
            public void onResponse(List<Company> result) {
                companiesList.clear();
                companiesList.addAll(result);
                companyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure
            }
        });
    }

    private void loadStoriesForCompany(Company company) {
        storyManager.getStoriesByCompany(company.getName(), new Callback<List<Story>>() {
            @Override
            public void onResponse(List<Story> result) {
                storiesList.clear();
                storiesList.addAll(result);
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure
            }
        });
    }

    private void filterCompanies(String query) {
        List<Company> filteredCompanies = new ArrayList<>();
        for (Company company : companiesList) {
            if (company.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredCompanies.add(company);
            }
        }
        companyAdapter.updateCompanies(filteredCompanies);
    }

    private void updateBonusPoints() {
        userManager.getBonusPoints(new Callback<Integer>() {
            @Override
            public void onResponse(Integer points) {
                bonusPointsTextView.setText("Bonus Points: " + points);
            }

            @Override
            public void onFailure(Exception e) {
                bonusPointsTextView.setText("Bonus Points: 0");
            }
        });
    }
}

