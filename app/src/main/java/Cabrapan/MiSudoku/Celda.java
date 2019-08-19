package Cabrapan.MiSudoku;

public class Celda {
    int valor;
    boolean enable;
    int grupo;

    public Celda(){
        enable = true;
    }
    public void setValor(int i){
        valor = i;
    }
    public void setEnable(boolean e){
        enable = e;
    }
    public void setGrupo(int g){ grupo = g;}
    public boolean isEnabled(){
        return enable;
    }

    public int getGrupo() {
        return grupo;
    }
}
