package Cabrapan.MiSudoku;

import java.util.HashMap;

public class Juego {
    private CollectionCelda celdas;
    private int dificultad;
    private SudokuGenerator generator;

    public Juego(String dif){
        int cantAusentes = 0;
        celdas = new CollectionCelda();
        switch (dif){
            case "Fácil":
                dificultad = 1;
                cantAusentes = 3;
                break;
            case "Medio":
                dificultad = 2;
                cantAusentes = 15;
                break;
            case "Dificil":
                dificultad = 3;
                cantAusentes = 30;
                break;
        }
        generator = new SudokuGenerator(9,cantAusentes);
    }

    public int[][] iniciarJuego(){
        int[][] newTablero = generator.getMat();
        celdas.llenar(newTablero);
        return newTablero;
    }

    public boolean isEnabled(int x,int y){
        return celdas.isEnabled(x,y);
    }

    public int getDificultad() {
        return dificultad;
    }

    public boolean jugada(String nro, int x, int y){
        return celdas.jugadaValida(nro, x, y);
    }
    public boolean esFin(){
        return celdas.isFull();
    }

    public HashMap<String, Integer> getGrupo(int fila, int col) {
        int grupo = celdas.getNumeroGrupo(fila,col);
        HashMap<String, Integer> toRet = new HashMap<>();
        int minFila = 0,maxFila = 0,minCol = 0,maxCol = 0;
        if(grupo >=1 && grupo <=3){
            minFila = 0;
            maxFila = 2;
        }
        else {
            if (grupo < 7) {
                minFila = 3;
                maxFila = 5;
            } else {
                minFila = 6;
                maxFila = 8;
            }
        }

        switch (grupo){
            case 1:
            case 4:
            case 7:
                minCol = 0;
                maxCol = 2;
                break;
            case 2:
            case 5:
            case 8:
                minCol = 3;
                maxCol = 5;
                break;
            case 3:
            case 6:
            case 9:
                minCol = 6;
                maxCol = 8;
        }
        toRet.put("minCol", minCol);
        toRet.put("maxCol",maxCol);
        toRet.put("minFila",minFila);
        toRet.put("maxFila",maxFila);
        return toRet;
    }

    public int[] getPista() {
        return celdas.resolverTablero();
    }

}
