package com.example.listadecompras.ui.loginregistro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginRegistroViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LoginRegistroViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pagina - Login / Registro");
    }

    public LiveData<String> getText() {
        return mText;
    }
}