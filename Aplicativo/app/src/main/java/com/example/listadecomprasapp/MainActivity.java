package com.example.listadecomprasapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.listasfragment.CriarListaFragment;
import com.example.listasfragment.ListasImportadasFragment;
import com.example.listasfragment.MeuPerfilFragment;
import com.example.listasfragment.MinhasListasFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabMain;
    private LinearLayout fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabMain = findViewById(R.id.fab_main);
        fabMenu = findViewById(R.id.fab_menu);

        Button btnMinhasListas = findViewById(R.id.btnMinhasListas);
        Button btnMeuPerfil = findViewById(R.id.btnMeuPerfil);
        Button btnCriarLista = findViewById(R.id.btnCriarLista);
        Button btnListasImportadas = findViewById(R.id.btnListasImportadas);

        // Alterna menu visível/invisível
        fabMain.setOnClickListener(v -> {
            if (fabMenu.getVisibility() == View.GONE) {
                fabMenu.setVisibility(View.VISIBLE);
            } else {
                fabMenu.setVisibility(View.GONE);
            }
        });

        btnMinhasListas.setOnClickListener(v -> openFragment(new MinhasListasFragment()));
        btnMeuPerfil.setOnClickListener(v -> openFragment(new MeuPerfilFragment()));
        btnCriarLista.setOnClickListener(v -> openFragment(new CriarListaFragment()));
        btnListasImportadas.setOnClickListener(v -> openFragment(new ListasImportadasFragment()));

        // Carrega o fragment inicial
        openFragment(new MinhasListasFragment());
    }

    private void openFragment(Fragment fragment) {
        fabMenu.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
