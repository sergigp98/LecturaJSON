/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturajson;

import com.google.gson.Gson;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Usuario DAM 2
 */
public class Ventana extends JFrame {

    Container container;
    JLabel poster;
    JList listaPeliculas;
    JPanel panelCentro;
    DefaultListModel modeloLista;
    JButton cargarDatos;

    public Ventana() {
        initGUI();
    }

    public void initGUI() {

        instancias();
        acciones();
        configurarContainer();
        this.setSize(new Dimension(900, 500));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void instancias() {
        container = this.getContentPane();
        poster = new JLabel();
        modeloLista = new DefaultListModel();
        listaPeliculas = new JList(modeloLista);
        panelCentro = new JPanel();
        cargarDatos = new JButton("Cargar Datos");
    }

    private void acciones() {
        cargarDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeloLista.clear();
                new MiWorker().execute();
            }
        });
        
        listaPeliculas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Pelicula selecciona = (Pelicula) modeloLista.getElementAt(listaPeliculas.getSelectedIndex());
                //https://image.tmdb.org/t/p/w500
                String link = String.format("%s%s","https://image.tmdb.org/t/p/w500",selecciona.getPoster_path());
                System.out.println(selecciona.getPoster_path());
                System.out.println(link);
                URL imagen;
                try {
                    imagen = new URL(link);
                    BufferedImage imagenInternet = ImageIO.read(imagen);
                    poster.setIcon(new ImageIcon(imagenInternet));
                    pack();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void configurarContainer() {
        this.setLayout(new BorderLayout());
        this.add(configurarPanel(), BorderLayout.CENTER);
        this.add(cargarDatos, BorderLayout.SOUTH);
    }

    private JPanel configurarPanel() {
        panelCentro.setLayout(new GridLayout(1, 2));
        panelCentro.add(new JScrollPane(listaPeliculas));
        panelCentro.add(poster);
        return panelCentro;
    }

    class MiWorker extends SwingWorker<Boolean, Void> {

        URL url;
        HttpURLConnection connection;
        BufferedReader lector;
        StringBuilder builder = new StringBuilder();

        @Override
        protected Boolean doInBackground() throws Exception {
            //TODO para leer la url
            try {
                url = new URL("https://api.themoviedb.org/3/movie/now_playing?api_key=4ef66e12cddbb8fe9d4fd03ac9632f6e&language=en-US&page=1");
                connection = (HttpURLConnection) url.openConnection();
                lector = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            String linea;
            while ((linea = lector.readLine()) != null) {
                builder.append(linea);
            }

            JSONObject jsonEntero = new JSONObject(builder.toString());
            JSONArray jsonArray = jsonEntero.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objeto = jsonArray.getJSONObject(i);
                Gson gson = new Gson();
                Pelicula pelicula = gson.fromJson(objeto.toString(), Pelicula.class);
                modeloLista.addElement(pelicula);
                Thread.sleep(100);
            }
            return true;
        }

    }

}
