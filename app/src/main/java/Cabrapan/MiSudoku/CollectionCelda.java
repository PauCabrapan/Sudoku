package Cabrapan.MiSudoku;

import android.util.Log;

class CollectionCelda {
    private static final int size = 9;
    private Celda[][] celdas;

    public CollectionCelda(){
        celdas =  new Celda[9][9];
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                celdas[i][j] = new Celda();
                celdas[i][j].setGrupo(getNumeroGrupo(i,j));
            }
        }
    }

    public boolean isEnabled(int x, int y){
        if(x == 10 || y == 10)
            return false;
        else
            return celdas[x][y].isEnabled();
    }

    public void llenar(int[][] newTablero) {
        for(int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                celdas[i][j].setValor(newTablero[i][j]);
                if (newTablero[i][j] != 0)
                    celdas[i][j].setEnable(false);
            }
        }
    }

    public boolean jugadaValida(String id, int x, int y) {
        int nro = getNro(id);
        celdas[x][y].setValor(0);
        if(nro == 0){
            celdas[x][y].setValor(0);
            return true;
        }
        else{
            boolean es = esValidoenFila(celdas,nro,x,y) && esValidoenColumna(celdas, nro,x,y) && esValidoenGrupo(celdas, nro,x,y);
            celdas[x][y].setValor(nro);
            return es;
        }
    }

    public boolean jugadaValidaAux(Celda[][] tablero, int nro, int x, int y) {
        tablero[x][y].setValor(0);
        if(nro == 0){
            return true;
        }
        else{
            boolean es = esValidoenFila(tablero,nro,x,y) && esValidoenColumna(tablero,nro,x,y) && esValidoenGrupo(tablero,nro,x,y);
            return es;
        }
    }

    private boolean esValidoenGrupo(Celda [][] tab, int nro, int fila, int col) {
        boolean esValido = true;
        int grupo = getNumeroGrupo(fila,col);
        for (Celda[] ce: tab) {
            for (Celda c: ce) {
                if(esValido && c.getGrupo() == grupo){
                    esValido = c.valor != nro;
                }
            }
        }
        return esValido;
    }

    private boolean esValidoenColumna(Celda[][] tab,int nro, int fila, int col) {
        boolean esValido = true;
        int i = 0;
        while (esValido && i<size){
            esValido = tab[i][col].valor != nro;
            i++;
        }
        return esValido;
    }

    private boolean esValidoenFila(Celda[][] tab,int nro, int fila, int col) {
        boolean esValido = true;
        int i = 0;
        while (esValido && i<size){
            esValido = tab[fila][i].valor != nro;
            i++;
        }
        return esValido;
    }

    private int getNro(String id) {
        switch (id){
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "0":
                return 0;
        }
        return 0;
    }

    public int getNumeroGrupo(int fila, int columna){
        if (fila <= 2) {                            //   0 <= fila <= 2
            if (columna <= 2) // 0 <= columna <= 2
                return 1;
            else
            if (columna <= 5) // 2 <= columna <= 5
                return 2;
            else
                return 3; // 6 <= columna <= 8
        }
        else
        if (fila <=5){                          //   3 <= fila <= 5
            if (columna <= 2) // 0 <= columna <= 2
                return 4;
            else
            if (columna <= 5) // 2 <= columna <= 5
                return 5;
            else
                return 6; // 6 <= columna <= 8
        }
        else {                                  //   6 <= fila <= 8
            if (columna <= 2) // 0 <= columna <= 2
                return 7;
            else if (columna <= 5) // 2 <= columna <= 5
                return 8;
            else
                return 9; // 6 <= columna <= 8
        }
    }

    public boolean isFull() {
        boolean todasValor = true;
        for (Celda[] ce: celdas) {
            for (Celda c: ce) {
                todasValor = todasValor && c.valor != 0;

            }
        }
        return todasValor;
    }

    public int[] resolverTablero() {
        int[] toRet = new int [3];
        Celda [][] aux = copy(celdas);
        if(solve(aux)){
            Log.d("ES","S");
            boolean fin = false;
            int f = 0;
            int c = 0;
            while (!fin && f<size){
                c = 0;
                while (!fin && c <size){
                    fin = celdas[f][c].valor == 0;
                    c++;
                }
                f++;
            }
            Log.d("Fin",String.valueOf(fin));
            if(fin) {
                toRet[0] = f - 1;
                toRet[1] = c -1;
                toRet[2] = aux[f-1][c-1].valor;
            }
        }
        return toRet;
    }

    private Celda[][] copy(Celda[][] celdas) {
        Celda[][] toRet = new Celda[size][size];
        for(int i = 0 ; i<size; i++){
            for (int j = 0; j<size; j++){
                toRet[i][j] = new Celda();
                toRet[i][j].setValor(celdas[i][j].valor);
            }
        }
        return toRet;
    }

    private boolean solve(Celda[][] board){
        int row = -1;
        int col = -1;
        boolean isEmpty = true;
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (board[i][j].valor == 0){
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty){
                break;
            }
        }
        if (isEmpty){
            return true;
        }
        for (int num = 1; num <= 9; num++){
            if (jugadaValidaAux(board, num, row, col)){
                board[row][col].setValor(num);
                if (solve(board)){
                    return true;
                }
                else{
                    board[row][col].setValor(0);
                }
            }
        }
        return false;
    }
}
