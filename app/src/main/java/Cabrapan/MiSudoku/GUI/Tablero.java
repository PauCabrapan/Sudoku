package Cabrapan.MiSudoku.GUI;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import Cabrapan.MiSudoku.Juego;
import Cabrapan.MiSudoku.R;

public class Tablero extends AppCompatActivity {
    final static int size = 9; //Tamaño del tablero
    private Context context;
    private Button btnPista;
    private TextView [][] ivCeldas = new TextView[size][size];
    private Drawable[] drawCelda = new Drawable[6]; //Estados de la celda
    private int xSeleccionado;
    private int ySeleccionado;
    private Juego juego;
    private int errores;
    private boolean usoPista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Bundle b = getIntent().getExtras();
        String dificultad = b.getString("dificultad");
        errores = 0;
        usoPista = false;
        xSeleccionado = 10; //Valor invalido
        ySeleccionado = 10; //Valor invalido
        context = this;
        cargarRecursos();
        diseñarTablero();
        setListenKeyboard();
        juego = new Juego(dificultad);
        int [][] newTablero = juego.iniciarJuego();
        llenarTablero(newTablero);
        btnPista = findViewById(R.id.btnPista);
        btnPista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iluminar(juego.getPista());
            }
        });
    }

    private void iluminar(int[] posicion){
        usoPista = true;
        ivCeldas[posicion[0]][posicion[1]].setBackground(drawCelda[5]);
        setNro(String.valueOf(posicion[2]),posicion[0],posicion[1]);
        juego.jugada(String.valueOf(posicion[2]),posicion[0],posicion[1]);
        if(juego.esFin())
            endGame();
    }

    private void llenarTablero(int[][] newTablero) {
        String aux;
        for(int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                switch (newTablero[i][j]){
                    case 1:
                        ivCeldas[i][j].setText("1");
                        break;
                    case 2:
                        ivCeldas[i][j].setText("2");
                        break;
                    case 3:
                        ivCeldas[i][j].setText("3");
                        break;
                    case 4:
                        ivCeldas[i][j].setText("4");
                        break;
                    case 5:
                        ivCeldas[i][j].setText("5");
                        break;
                    case 6:
                        ivCeldas[i][j].setText("6");
                        break;
                    case 7:
                        ivCeldas[i][j].setText("7");
                        break;
                    case 8:
                        ivCeldas[i][j].setText("8");
                        break;
                    case 9:
                        ivCeldas[i][j].setText("9");
                        break;
                    case 0:
                        ivCeldas[i][j].setText(" ");
                        break;
                }
                ivCeldas[i][j].setGravity(Gravity.CENTER);
            }
        }
    }

    private void setListenKeyboard() {
        TableLayout keyboard = findViewById(R.id.numeros);
        ArrayList<View> touchables = keyboard.getTouchables();
        for(View t : touchables){
            final View tFinal = t;
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((juego.isEnabled(xSeleccionado, ySeleccionado))) {
                        int a = v.getId();
                        Button b = findViewById(a);
                        CharSequence text = b.getText();
                        String nro = (text.length() == 0) ? "0" : String.valueOf(text);
                        boolean esValido = juego.jugada(nro, xSeleccionado, ySeleccionado);
                        setNro(text, xSeleccionado,ySeleccionado);
                        if(esValido){
                            ivCeldas[xSeleccionado][ySeleccionado].setBackground(drawCelda[1]);
                            if (juego.esFin())
                               endGame();
                        }
                        else {
                            aumentarErrores();
                            if(errores>5)
                                endGame();
                            ivCeldas[xSeleccionado][ySeleccionado].setBackground(drawCelda[2]);
                            Toast.makeText(context, "Movimiento inválido", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    private void aumentarErrores() {
        errores++;
        TextView tvError = findViewById(R.id.tvError);
        tvError.setText(String.valueOf(errores));
        if(errores == 5){
            endGame();
        }
    }


    private void endGame() {

        SharedPreferences pref = getSharedPreferences("contadores",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int cant;
        String title;
        if (errores > 4){ //Juego perdido
            title = "¡Perdiste!";
            //Sumo perdidos
            cant = pref.getInt("cPerdidos",0);
            editor.putInt("cPerdidos",cant + 1);

            //Reinicio racha
            editor.putInt("rVictorias",0);
        }
        else{
            title = "¡Ganaste!";
            //Sumo ganados
            cant = pref.getInt("cGanados",0);
            editor.putInt("cGanados",cant + 1);

            //Sumo racha
            cant = pref.getInt("rVictorias",0);
            editor.putInt("rVictorias",cant + 1);

            //Si no uso pistas suma sinPista
            if(!usoPista){
                cant = pref.getInt("csAyuda",0);
                editor.putInt("csAyuda",cant+1);
            }
        }
        editor.apply();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setPositiveButton("Volver al menú", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context,Principal.class);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void setNro(CharSequence nro, int xSeleccionado, int ySeleccionado) {
        if(nro.length() != 0)
            ivCeldas[xSeleccionado][ySeleccionado].setText(nro);
        else
            ivCeldas[xSeleccionado][ySeleccionado].setText("");
        ivCeldas[xSeleccionado][ySeleccionado].setGravity(Gravity.CENTER);
    }

    protected void cargarRecursos(){
        drawCelda[0] = context.getDrawable(R.drawable.celdas);
        drawCelda[1] = context.getDrawable(R.drawable.celda_selected);
        drawCelda[2] = context.getDrawable(R.drawable.celda_error_selected);
        drawCelda[3] = context.getDrawable(R.drawable.celda_error);
        drawCelda[4] = context.getDrawable(R.drawable.grupo_selected);
        drawCelda[5] = context.getDrawable(R.drawable.pista);
    }

    protected void diseñarTablero(){
        int sizeCelda = Math.round(ScreenWidth()/size);
        LinearLayout.LayoutParams lpFila = new LinearLayout.LayoutParams(sizeCelda*size,sizeCelda);
        LinearLayout.LayoutParams lpCel = new LinearLayout.LayoutParams(sizeCelda,sizeCelda);
        LinearLayout linTablero = findViewById(R.id.tablero);

        for (int i = 0; i < size; i++){
            LinearLayout linFila = new LinearLayout(context);
            for (int j = 0; j < size; j++){
                ivCeldas[i][j] = new TextView(context);
                ivCeldas[i][j].setBackground(drawCelda[0]);
                final int finalI = i;
                final int finalJ = j;
                final TextView selected = ivCeldas[i][j];
                ivCeldas[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(xSeleccionado != 10 && ySeleccionado != 10) {
                            unSelectGrupo();
                            if (ivCeldas[xSeleccionado][ySeleccionado].getBackground() != drawCelda[2])
                                ivCeldas[xSeleccionado][ySeleccionado].setBackground(drawCelda[0]);
                            else
                                ivCeldas[xSeleccionado][ySeleccionado].setBackground(drawCelda[3]);
                        }
                        xSeleccionado = finalI;
                        ySeleccionado = finalJ;
                        selectGrupo();
                        if(selected.getBackground() == drawCelda[3])
                            selected.setBackground(drawCelda[2]);
                        else
                            selected.setBackground(drawCelda[1]);

                    }
                });
                linFila.addView(ivCeldas[i][j],lpCel);
            }
            linTablero.addView(linFila,lpFila);
        }
    }

    private void unSelectGrupo() {
        HashMap<String,Integer> map = juego.getGrupo(xSeleccionado,ySeleccionado);
        int minCol = (map.get("minCol") == null)? 0 : map.get("minCol");
        int maxCol = (map.get("maxCol") == null)? 0 : map.get("maxCol");
        int minFila = (map.get("minFila") == null)? 0 : map.get("minFila");
        int maxFila = (map.get("maxFila") == null)? 0 : map.get("maxFila");
        for(int i = minFila; i<=maxFila;i++){
            for(int j = minCol;j<=maxCol;j++){
                if(i != xSeleccionado || j != ySeleccionado)
                    ivCeldas[i][j].setBackground(drawCelda[0]);
            }
        }
    }

    private void selectGrupo() {
        HashMap<String,Integer> map = juego.getGrupo(xSeleccionado,ySeleccionado);
        int minCol = (map.get("minCol") == null)? 0 : map.get("minCol");
        int maxCol = (map.get("maxCol") == null)? 0 : map.get("maxCol");
        int minFila = (map.get("minFila") == null)? 0 : map.get("minFila");
        int maxFila = (map.get("maxFila") == null)? 0 : map.get("maxFila");
        for(int i = minFila; i<=maxFila;i++){
            for(int j = minCol;j<=maxCol;j++){
                if(i != xSeleccionado || j != ySeleccionado)
                    ivCeldas[i][j].setBackground(drawCelda[4]);
            }
        }
    }

    private float ScreenWidth() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }
}
