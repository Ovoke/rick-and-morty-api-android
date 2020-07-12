package com.urban.androidhomework.view_model;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ResidentsViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private ArrayList<Integer> param;

    public ResidentsViewModelFactory(Application mApplication, ArrayList<Integer> mParam){
        application = mApplication;
        param = mParam;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ResidentsViewModel(application, param);
    }
}
