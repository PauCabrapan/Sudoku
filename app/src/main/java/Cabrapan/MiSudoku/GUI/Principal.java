package Cabrapan.MiSudoku.GUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import Cabrapan.MiSudoku.R;

public class Principal extends AppCompatActivity {
    private Button btnPlay;
    private Button btnHelp;
    private Button btnEstad;
    private String [] opciones;
    private String dificultadSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarOpciones();

        btnPlay = findViewById(R.id.btnJugar);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirOpciones();
            }
        });

        btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAyuda();
            }
        });

        btnEstad = findViewById(R.id.btnEstad);
        btnEstad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirEstadisticas();
            }
        });

        Button btnAbout = findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarInfo();
            }
        });
    }

    private void mostrarInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información");
        String info = "Autora: Cabrapan Paula Belen \n Lu: 111381 \n Año cursada: 2018";
        builder.setMessage(info);
        builder.show();
    }


    private void iniciarOpciones() {
        opciones = new String[3];
        opciones[0] = "Fácil";
        opciones[1] = "Medio";
        opciones[2] = "Dificil";
    }

    private void abrirAyuda() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.msjAyuda);
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();
    }

    private void abrirEstadisticas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.estad);

        final SharedPreferences pref = getSharedPreferences("contadores", Context.MODE_PRIVATE);
        int cGan = pref.getInt("cGanados",0);
        int cPer = pref.getInt("cPerdidos",0);
        int csAyu = pref.getInt("csAyuda",0);
        int rVict = pref.getInt("rVictorias",0);

        String cG = "Juegos ganados: "+ String.valueOf(cGan);
        String cP = "Juegos perdidos: " + String.valueOf(cPer);
        String cS = "Juegos ganados sin pistas: " + String.valueOf(csAyu);
        String rV = "Mejor racha de victorias: " + String.valueOf(rVict);
        final SharedPreferences.Editor editor = pref.edit();
        builder.setMessage(cG + "\n" + cP + "\n" + cS + "\n" + rV);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putInt("cGanados",0);
                editor.putInt("cPerdidos",0);
                editor.putInt("csAyuda",0);
                editor.putInt("rVictorias",0);
                editor.commit();
            }
        });
        builder.show();
    }

    private void abrirOpciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elegí la dificultad");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dificultadSeleccionada = opciones[which];
                abrirJuego();
            }
        });
        builder.show();
    }

    public void abrirJuego(){
        Intent intent = new Intent(this, Tablero.class);
        Bundle b = new Bundle();
        b.putString("dificultad",dificultadSeleccionada);
        intent.putExtras(b);
        startActivity(intent);
    }

}