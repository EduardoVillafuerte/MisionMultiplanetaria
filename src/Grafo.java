import java.util.*;

public class Grafo {
    public Map<Planeta, List<Arista>> adyacencias = new HashMap<>();

    public void agregarPlaneta(Planeta p) {
        adyacencias.putIfAbsent(p, new ArrayList<>());
    }

    public void agregarArista(Planeta origen, Planeta destino, double tiempo, double deltaV, double riesgo) {
        adyacencias.get(origen).add(new Arista(destino, tiempo, deltaV, riesgo));
    }

    public List<Planeta> obtenerPlanetas() {
        return new ArrayList<>(adyacencias.keySet());
    }

    public List<Arista> obtenerAdyacentes(Planeta p) {
        return adyacencias.get(p);
    }

    public List<Planeta> caminoMasCortoDijkstra(Planeta inicio, Planeta destino) {
        Map<Planeta, Double> dist = new HashMap<>();
        Map<Planeta, Planeta> prev = new HashMap<>();
        PriorityQueue<Planeta> cola = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        for (Planeta p : obtenerPlanetas()) {
            dist.put(p, Double.POSITIVE_INFINITY);
            prev.put(p, null);
        }
        dist.put(inicio, 0.0);
        cola.add(inicio);

        while (!cola.isEmpty()) {
            Planeta actual = cola.poll();
            if (actual.equals(destino)) break;
            for (Arista a : obtenerAdyacentes(actual)) {
                // cambaiar a.tiempo por deltaV o riesgo según lo que se requiera en el camino de dijkstra
                double alt = dist.get(actual) + a.deltaV;
                if (alt < dist.get(a.destino)) {
                    dist.put(a.destino, alt);
                    prev.put(a.destino, actual);
                    cola.add(a.destino);
                }
            }
        }

        List<Planeta> camino = new ArrayList<>();
        for (Planeta at = destino; at != null; at = prev.get(at)) {
            camino.add(0, at);
        }
        if (camino.isEmpty() || !camino.get(0).equals(inicio)) return Collections.emptyList();
        return camino;
    }

    public Planeta obtenerPlanetaPorNombre(String nombre) {
        for (Planeta p : obtenerPlanetas()) {
            if (p.nombre.equals(nombre)) {
                return p;
            }
        }
        return null;
    }

}