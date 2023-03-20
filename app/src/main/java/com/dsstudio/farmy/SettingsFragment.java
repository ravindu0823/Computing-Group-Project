package com.dsstudio.farmy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;


public class SettingsFragment extends Fragment {
    String[] fields = {"Field 1", "Field 2", "Field 3", "Field 4"};
    String[] agents = {"Agent 1", "Agent 2", "Agent 3", "Agent 4"};

    AutoCompleteTextView autoCompleteSelectField, autoCompleteSelectAgent;
    ArrayAdapter<String> adapterFields, adapterAgents;

    @Override
    public void onResume() {
        super.onResume();

        adapterFields = new ArrayAdapter<String>(getActivity(), R.layout.dropdown_item, fields);
        adapterAgents = new ArrayAdapter<String>(getActivity(), R.layout.dropdown_item, agents);

        autoCompleteSelectField.setAdapter(adapterFields);
        autoCompleteSelectAgent.setAdapter(adapterAgents);

        autoCompleteSelectField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String field = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getActivity(), "Field: " + field, Toast.LENGTH_SHORT).show();

            }
        });

        autoCompleteSelectAgent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String agent = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getActivity(), "Agent: " + agent, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        autoCompleteSelectField = v.findViewById(R.id.autoCompleteSelectField);
        autoCompleteSelectAgent = v.findViewById(R.id.autoCompleteSelectAgent);


        return v;
    }
}