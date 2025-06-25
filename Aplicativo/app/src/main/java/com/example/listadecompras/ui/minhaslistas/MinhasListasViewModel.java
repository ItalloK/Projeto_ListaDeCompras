package com.example.listadecompras.ui.minhaslistas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MinhasListasViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MinhasListasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pagina - Minhas Listas");
    }

    public LiveData<String> getText() {
        return mText;
    }
}