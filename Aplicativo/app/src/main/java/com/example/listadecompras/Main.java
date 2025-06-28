package com.example.listadecompras;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.listadecompras.databinding.ActivityMainBinding;
import com.example.listadecompras.util.TokenManager;
import com.google.android.material.navigation.NavigationView;

public class Main extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TokenManager tokenManager;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenManager = new TokenManager(this);

        iniciarUI();

        controlarItensMenu();

        verificarAutenticacao();
    }

    private void iniciarUI() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_minhaslistas,
                R.id.nav_listassalvas
        ).setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        atualizarHeaderUsuario();
    }

    private void controlarItensMenu() {
        Menu menu = navigationView.getMenu();

        MenuItem loginItem = menu.findItem(R.id.loginFragment);
        MenuItem logoutItem = menu.findItem(R.id.menu_logout);

        boolean loggedIn = tokenManager.getAccessToken() != null && !tokenManager.getAccessToken().isEmpty();

        loginItem.setVisible(!loggedIn);
        logoutItem.setVisible(loggedIn);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_logout) {
                // Logout: limpa tokens
                tokenManager.clearTokens();

                // Atualiza menu e header
                loginItem.setVisible(true);
                logoutItem.setVisible(false);
                atualizarHeaderUsuarioVazio();

                // Navega para tela de login
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.loginFragment);

                // Fecha o drawer
                binding.drawerLayout.closeDrawers();

                return true;
            }

            // Para os outros itens, navegação padrão
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                binding.drawerLayout.closeDrawers();
            }
            return handled;
        });
    }

    private void verificarAutenticacao() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        String accessToken = tokenManager.getAccessToken();

        if (accessToken != null && !accessToken.isEmpty()) {
            // Usuário já está autenticado, vai para "Minhas Listas"
            navController.navigate(R.id.nav_minhaslistas);
        } else {
            // Sem token -> tela de login
            navController.navigate(R.id.loginFragment);
        }
    }

    private void atualizarHeaderUsuario() {
        View headerView = navigationView.getHeaderView(0);

        TextView nomeTextView = headerView.findViewById(R.id.textViewNome);
        TextView emailTextView = headerView.findViewById(R.id.textViewEmail);

        String nome = tokenManager.getUserName();
        String email = tokenManager.getUserEmail();

        if (nome != null && !nome.isEmpty()) {
            nomeTextView.setText(nome);
        } else {
            nomeTextView.setText("Nome do Usuário");
        }

        if (email != null && !email.isEmpty()) {
            emailTextView.setText(email);
        } else {
            emailTextView.setText("email@exemplo.com");
        }
    }

    private void atualizarHeaderUsuarioVazio() {
        View headerView = navigationView.getHeaderView(0);

        TextView nomeTextView = headerView.findViewById(R.id.textViewNome);
        TextView emailTextView = headerView.findViewById(R.id.textViewEmail);

        nomeTextView.setText("Nome do Usuário");
        emailTextView.setText("email@exemplo.com");
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
