package com.example.listadecompras.ui.listassalvas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListasSalvasViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ListasSalvasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pagina - Listas Salvas");
    }

    public LiveData<String> getText() {
        return mText;
    }
}