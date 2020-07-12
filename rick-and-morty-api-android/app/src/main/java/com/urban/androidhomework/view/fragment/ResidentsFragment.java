package com.urban.androidhomework.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.urban.androidhomework.R;
import com.urban.androidhomework.view.adapter.ResidentsAdapter;
import com.urban.androidhomework.view_model.ResidentsViewModel;
import com.urban.androidhomework.view_model.ResidentsViewModelFactory;
import com.urban.androidhomework.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ResidentsFragment extends Fragment {
    private ArrayList<Integer> residents;

    private ResidentsViewModel viewModel;
    private View errorLayout, loadingLayout;
    private RecyclerView recyclerView;
    private Button retryButton;
    private TextView errorTextView;
    private NavController navController;

    private ResidentsAdapter adapter;

    private View.OnClickListener backClickListener = (v) -> navController.navigateUp();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_residents, container, false);

        Bundle args = getArguments();
        if(args != null) {
            String[] s = args.getStringArray(getString(R.string.residentsID));
            residents = Utils.Companion.getIDsFromArrayUrls(s);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        errorLayout = view.findViewById(R.id.errorLayout);
        loadingLayout = view.findViewById(R.id.loadingLayout);
        errorTextView = view.findViewById(R.id.errorTextView);
        retryButton = view.findViewById(R.id.retryButton);
        recyclerView = view.findViewById(R.id.charactersRecyclerView);

        retryButton.setOnClickListener((v) -> reLoad());
        adapter = new ResidentsAdapter(getContext());
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        view.findViewById(R.id.toolbar_back_button).setOnClickListener(backClickListener);

        // Create ViewModel and set up LiveData Observers
        viewModel = ViewModelProviders
                .of(this, new ResidentsViewModelFactory(getActivity().getApplication(), residents))
                .get(ResidentsViewModel.class);

        viewModel.getLiveData().observe(this, characters -> adapter.addList(Utils.Companion.getName(characters)));
        viewModel.getThrowableLiveData().observe(this, throwable -> {
            // If initial Data loaded successfully
            if(throwable == null) {
                onLoadSuccess();
            } else {
                onLoadFailure(throwable.getMessage());
            }
        });
    }

    private void onLoadSuccess() {
        recyclerView.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
    }

    private void onLoadFailure(String message) {
        recyclerView.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);

        errorTextView.setText(message);
    }

    private void reLoad(){
        recyclerView.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        viewModel.loadResidents(residents);
    }
}
