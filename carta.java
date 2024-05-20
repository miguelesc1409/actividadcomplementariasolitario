class Carta {

    // Atrbutos
    int valor;
    String palo;
    boolean esVisible;

    // Constructor
    public Carta(String palo,int valor) {
        this.valor = valor;
        this.palo = palo;
    }

    // Métodos
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getPalo() {
        return palo;
    }

    public void setPalo(String palo) {
        this.palo = palo;
    }

    public boolean getesVisible(){
        return esVisible;
    }

    public void setesVisible(boolean esVisible){
        this.esVisible = esVisible;
    }

    public String toString() {
        return "Carta{" + "valor=" + valor + ", palo=" + palo + '}';
    }

}