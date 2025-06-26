package com.example.listadecompras;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.listadecompras.api.ApiService;
import com.example.listadecompras.api.RetrofitClient;
import com.example.listadecompras.models.AuthResponse;
import com.example.listadecompras.models.RefreshRequest;
import com.example.listadecompras.util.TokenManager;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.listadecompras.databinding.ActivityMainBinding;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenManager = new TokenManager(this);

        Log.d("MainActivity", "Access Token recuperado: " + tokenManager.getAccessToken());
        Log.d("MainActivity", "Refresh Token recuperado: " + tokenManager.getRefreshToken());

        iniciarUI();

        if (tokenManager.getAccessToken() != null && tokenManager.getRefreshToken() != null) {
            tentarRenovarToken();
        } else {
            navegarParaLogin();
        }
    }


    private void tentarRenovarToken() {
        ApiService api = RetrofitClient.getInstance("").create(ApiService.class);
        RefreshRequest req = new RefreshRequest(tokenManager.getRefreshToken());

        api.refresh(req).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().status) {
                    tokenManager.saveTokens(response.body().token, response.body().refreshToken);
                    // Continua normalmente, pode navegar para tela principal
                    navegarParaMinhasListas();
                } else {
                    fazerLogout();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                fazerLogout();
            }
        });
    }

    private void iniciarUI() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Não inclua o loginFragment aqui para o botão hambúrguer aparecer sempre
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_minhaslistas,
                R.id.nav_listassalvas
        ).setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Garantir drawer destravado sempre (opcional, caso tenha algo travando)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void navegarParaLogin() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.loginFragment);
    }

    private void navegarParaMinhasListas() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_minhaslistas);
    }

    private void fazerLogout() {
        tokenManager.clear();
        Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_LONG).show();
        navegarParaLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
