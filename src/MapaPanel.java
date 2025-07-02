import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;

public class MapaPanel extends JPanel {
    private Grafo grafo;
    private Map<String, Image> imagenes = new HashMap<>();
    private Image fondo;

    public MapaPanel(Grafo grafo) {
        this.grafo = grafo;
        setPreferredSize(new Dimension(700, 500));
        cargarImagenes();
    }

    private void cargarImagenes() {
        try {
            imagenes.put("Tierra", ImageIO.read(getClass().getResource("/resources/tierra.png")));
            imagenes.put("Luna", ImageIO.read(getClass().getResource("/resources/luna.png")));
            imagenes.put("Marte", ImageIO.read(getClass().getResource("/resources/marte.png")));

            imagenes.put("LEO (Órbita Baja Terrestre):",ImageIO.read(getClass().getResource("/resources/LEO.jpg")));
            imagenes.put("GTO (Órbita de Transferencia Geoestacionaria):",ImageIO.read(getClass().getResource("/resources/GTO.png")));
            imagenes.put("TMI (Inyección Trans-Lunar):",ImageIO.read(getClass().getResource("/resources/ITL.png")));

            fondo = ImageIO.read(getClass().getResource("/resources/espacio.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error cargando imágenes: " + e.getMessage());
        }
    }

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
    }

    private List<Planeta> rutaOptima = new ArrayList<>();

    public void mostrarRutaOptima(List<Planeta> ruta) {
        this.rutaOptima = ruta;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // 1. Dibuja el fondo primero
        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }

        // 2. Dibuja aristas con recuadro blanco y texto negro
        for (Planeta p : grafo.obtenerPlanetas()) {
            for (Arista a : grafo.obtenerAdyacentes(p)) {
                g2.setColor(Color.GRAY);
                g2.drawLine(p.x, p.y, a.destino.x, a.destino.y);
                int mx = (p.x + a.destino.x) / 2;
                int my = (p.y + a.destino.y) / 2;
                String datos = "ΔV: " + a.deltaV + "km/s, T: " + a.tiempo + "d, % riesgo: " + a.riesgo;
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(datos);
                int textHeight = fm.getHeight();

                g2.setColor(Color.WHITE);
                g2.fillRect(mx - textWidth / 2 - 4, my - textHeight / 2, textWidth + 8, textHeight);

                g2.setColor(Color.BLACK);
                g2.drawRect(mx - textWidth / 2 - 4, my - textHeight / 2, textWidth + 8, textHeight);

                g2.setColor(Color.BLACK);
                g2.drawString(datos, mx - textWidth / 2, my + fm.getAscent() - textHeight / 2);
            }
        }

        // 3. Dibuja la ruta óptima en color especial
        if (rutaOptima != null && rutaOptima.size() > 1) {
            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.GREEN);
            for (int i = 0; i < rutaOptima.size() - 1; i++) {
                Planeta a = rutaOptima.get(i);
                Planeta b = rutaOptima.get(i + 1);
                g2.drawLine(a.x, a.y, b.x, b.y);
            }
            g2.setStroke(new BasicStroke(1));
        }

        // 4. Dibuja nodos con imágenes y nombres centrados
        for (Planeta p : grafo.obtenerPlanetas()) {
            Image img = imagenes.get(p.nombre);
            if (img != null) {
                g2.drawImage(img, p.x - 25, p.y - 25, 50, 50, this);
            } else {
                g2.setColor(Color.BLUE);
                g2.fillOval(p.x - 25, p.y - 25, 50, 50);
            }
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(p.nombre);
            int textHeight = fm.getAscent();
            g2.drawString(p.nombre, p.x - textWidth / 2, p.y + 35);
        }

        // 5. Mostrar el resultado en un cuadro blanco con letras negras en la parte inferior derecha
        if (rutaOptima != null && !rutaOptima.isEmpty()) {
            StringBuilder sb = new StringBuilder("Ruta: ");
            for (int i = 0; i < rutaOptima.size(); i++) {
                sb.append(rutaOptima.get(i).nombre);
                if (i < rutaOptima.size() - 1) sb.append(" → ");
            }
            String rutaTexto = sb.toString();

            int padding = 12;
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(rutaTexto);
            int textHeight = fm.getHeight();

            int x = getWidth() - textWidth - padding * 2 - 10;
            int y = getHeight() - textHeight - padding * 2 - 10;

            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, textWidth + padding * 2, textHeight + padding * 2);

            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, textWidth + padding * 2, textHeight + padding * 2);

            g2.setColor(Color.BLACK);
            g2.drawString(rutaTexto, x + padding, y + padding + fm.getAscent());
        }
    }
}