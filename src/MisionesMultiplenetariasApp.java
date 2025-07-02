import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MisionesMultiplenetariasApp {
    public static void main(String[] args) {
        Grafo grafoArtemis = crearGrafoArtemis();
        Grafo grafoStarship = crearGrafoStarship();

        JFrame frame = new JFrame("Misiones Multiplanetarias");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MapaPanel panel = new MapaPanel(grafoArtemis);

        JButton btnCambiar = new JButton("Cambiar Misión");
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCambiar);

        JButton btnCaminoCorto = new JButton("Camino Más Corto");
        panelBoton.add(btnCaminoCorto);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(panelBoton, BorderLayout.SOUTH);
        frame.setMinimumSize(new Dimension(850,400));


        final Grafo[] grafoActual = { grafoArtemis }; // Usar array para modificar dentro de listeners

        btnCambiar.addActionListener(new ActionListener() {
            private boolean artemis = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                artemis = !artemis;
                grafoActual[0] = artemis ? grafoArtemis : grafoStarship;
                panel.setGrafo(grafoActual[0]);
                panel.mostrarRutaOptima(null); // Limpia la ruta al cambiar de misión
            }
        });

        btnCaminoCorto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambia los nombres según el grafo actual
                if (grafoActual[0] == grafoArtemis) {
                    Planeta inicio = grafoActual[0].obtenerPlanetaPorNombre("Tierra");
                    Planeta destino = grafoActual[0].obtenerPlanetaPorNombre("Marte");
                    List<Planeta> rutaOptima = grafoActual[0].caminoMasCortoDijkstra(inicio, destino);
                    panel.mostrarRutaOptima(rutaOptima);
                } else {
                    Planeta inicio = grafoActual[0].obtenerPlanetaPorNombre("LEO (Órbita Baja Terrestre):");
                    Planeta destino = grafoActual[0].obtenerPlanetaPorNombre("Marte");
                    List<Planeta> rutaOptima = grafoActual[0].caminoMasCortoDijkstra(inicio, destino);
                    panel.mostrarRutaOptima(rutaOptima);
                }
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    private static Grafo crearGrafoArtemis() {
        Grafo grafo = new Grafo();
        Planeta tierra = new Planeta("Tierra", 150, 400);
        Planeta luna = new Planeta("Luna", 400, 150);
        Planeta marte = new Planeta("Marte", 700, 400);

        grafo.agregarPlaneta(tierra);
        grafo.agregarPlaneta(luna);
        grafo.agregarPlaneta(marte);

        grafo.agregarArista(tierra, luna, 3, 10.5, 10);
        grafo.agregarArista(luna, marte, 273.75, 25.0, 30);
        grafo.agregarArista(tierra, marte, 304.16, 30.0, 50);

        return grafo;
    }

    private static Grafo crearGrafoStarship() {
        Grafo grafo = new Grafo();
        Planeta leo = new Planeta("LEO (Órbita Baja Terrestre):", 100, 400);
        Planeta gto = new Planeta("GTO (Órbita de Transferencia Geoestacionaria):", 300, 200);
        Planeta tmi = new Planeta("TMI (Inyección Trans-Lunar):", 500, 100);
        Planeta mars = new Planeta("Marte", 700, 400);

        grafo.agregarPlaneta(leo);
        grafo.agregarPlaneta(gto);
        grafo.agregarPlaneta(tmi);
        grafo.agregarPlaneta(mars);

        grafo.agregarArista(leo, gto,  2.5, 1.5, 5);
        grafo.agregarArista(gto, tmi, 2.2, 2.0, 8);
        grafo.agregarArista(tmi, mars, 0.6, 210, 20);
        grafo.agregarArista(leo, tmi, 4.5, 3.0, 10);
        grafo.agregarArista(gto, mars, 2.8, 220, 25);

        return grafo;
    }
}