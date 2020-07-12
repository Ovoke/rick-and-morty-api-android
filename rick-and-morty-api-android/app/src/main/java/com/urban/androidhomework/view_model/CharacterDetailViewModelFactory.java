package com.urban.androidhomework.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CharacterDetailViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private int param;

    public CharacterDetailViewModelFactory(Application mApplication, int mParam){
        application = mApplication;
        param = mParam;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CharacterDetailViewModel(application, param);
    }
}
