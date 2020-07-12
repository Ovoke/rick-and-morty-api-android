package com.urban.androidhomework.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.urban.androidhomework.view_model.CharacterDetailViewModel;
import com.urban.androidhomework.view_model.CharacterDetailViewModelFactory;
import com.urban.androidhomework.R;
import com.urban.androidhomework.utils.Utils;
import com.urban.androidhomework.databinding.FragmentCharacterDetailsBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class CharacterDetailsFragment extends Fragment {

    private FragmentCharacterDetailsBinding binding;
    private CharacterDetailViewModel viewModel;
    private Snackbar snackbar;
    private int characterID;

    private NavController navController;

    private View.OnClickListener retryClickListener = (v) -> reLoad();
    private View.OnClickListener residentsButtonClickListener = (v) -> {
        if(navController == null) return;
        Bundle bundle = new Bundle();
        ArrayList<String> residents = binding.getCharacterDetails().getLocation().getResidents();
        bundle.putStringArray(getString(R.string.residentsID), Utils.Companion.getArrayFromArrayList(residents));

        navController.navigate(
                R.id.action_characterDetailsFragment_to_residentsFragment,
                bundle
        );
    };
    private View.OnClickListener backClickListener = (v) -> navController.navigateUp();

    public CharacterDetailsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_character_details, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        if(args != null) {
            characterID = args.getInt(getString(R.string.characterIDKey));
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        // Create ViewModel and set up LiveData Observers
        viewModel = ViewModelProviders
                .of(this, new CharacterDetailViewModelFactory(getActivity().getApplication(), characterID))
                .get(CharacterDetailViewModel.class);

        viewModel.getLiveData().observe(this, characterDetails -> {
                binding.setCharacterDetails(characterDetails);
                ImageView imageView = view.findViewById(R.id.character_image);
                Glide.with(this).load(binding.getCharacterDetails().getCharacter().getImage()).into(imageView);
            });
        viewModel.getThrowableLiveData().observe(this, throwable -> {
                    // If initial Data loaded successfully
                    if(throwable == null) {
                        onLoadSuccess();
                    } else {
                        onLoadFailure(throwable.getMessage());
                    }
                });

        snackbar = Snackbar.make(binding.getRoot(), "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.retry, retryClickListener);
        binding.residentsButton.setOnClickListener(residentsButtonClickListener);
        binding.toolbarBackButton.setOnClickListener(backClickListener);
    }

    private void onLoadSuccess() {
        binding.residentsButton.setVisibility(View.VISIBLE);

        binding.loading.setVisibility(View.GONE);
        binding.mainContent.setVisibility(View.VISIBLE);
        if(snackbar != null && snackbar.isShown())
            snackbar.dismiss();
    }

    private void onLoadFailure(String message) {
        binding.loading.setVisibility(View.GONE);
        binding.mainContent.setVisibility(View.GONE);
        if(snackbar != null && !snackbar.isShown()) {
            snackbar.setText(message);
            snackbar.show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(snackbar != null && snackbar.isShown())
            snackbar.dismiss();
    }

    private void reLoad(){
        binding.loading.setVisibility(View.VISIBLE);
        binding.mainContent.setVisibility(View.GONE);

        if(snackbar != null && snackbar.isShown())
            snackbar.dismiss();
        viewModel.loadCharacterDetails(characterID);
    }
}
