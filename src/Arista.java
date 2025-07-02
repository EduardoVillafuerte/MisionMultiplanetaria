public class Arista {
    public Planeta destino;
    public double deltaV, tiempo, riesgo;

    public Arista(Planeta destino, double deltaV, double tiempo, double riesgo) {
        this.destino = destino;
        this.deltaV = deltaV;
        this.tiempo = tiempo;
        this.riesgo = riesgo;
    }
}