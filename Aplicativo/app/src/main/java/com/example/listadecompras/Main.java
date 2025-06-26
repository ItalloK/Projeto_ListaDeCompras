package com.example.listadecompras;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listadecompras.databinding.ActivityMainBinding;

public class Main extends AppCompatActivity {

    // Configuração da AppBar para o Navigation Drawer
    private AppBarConfiguration mAppBarConfiguration;

    // Objeto de binding para acessar os elementos do layout via View Binding
    private ActivityMainBinding binding;

    // Método chamado quando a Activity é criada
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa o View Binding para o layout da activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Define o conteúdo da activity como a raiz do layout inflado
        setContentView(binding.getRoot());

        // Configura a Toolbar como a barra de ação (action bar) da activity
        setSupportActionBar(binding.appBarMain.toolbar);

        // Referência ao DrawerLayout do layout (menu lateral)
        DrawerLayout drawer = binding.drawerLayout;

        // Referência ao NavigationView (menu lateral com itens de navegação)
        NavigationView navigationView = binding.navView;

        // Define quais destinos são considerados "top-level", ou seja,
        // destinos principais que não mostram botão "voltar"
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.loginFragment,        // Tela de login/registro
                R.id.nav_minhaslistas,         // Tela com listas do usuário
                R.id.nav_listassalvas          // Tela com listas salvas
        )
                .setOpenableLayout(drawer) // Conecta o DrawerLayout ao Navigation Component
                .build();

        // Controlador de navegação que gerencia as trocas de fragmentos na tela
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Configura a ActionBar para funcionar com o controlador de navegação
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Faz o NavigationView (menu lateral) funcionar com o controlador de navegação
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    // Método que cria o menu de opções da ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla o menu (adiciona itens à ActionBar se ela estiver presente)
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Método chamado quando o usuário pressiona o botão "voltar" da ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        // Obtém o controlador de navegação atual
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Tenta navegar para cima (voltar para o destino anterior)
        // Se não for possível, executa o comportamento padrão
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
