/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturajson;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.SwingUtilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Usuario DAM 2
 */
public class Entrada {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, JSONException {
        // TODO code application logic here
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ventana v = new Ventana();
                
            }
        });

        String link = "https://api.themoviedb.org/3/movie/now_playing?api_key=4ef66e12cddbb8fe9d4fd03ac9632f6e&language=en-US&page=1";
        URL url = new URL(link);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        BufferedReader lector = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        //Es un string normal pero mas grande por si el string que voy a utilizar es muy grande
        StringBuilder builder = new StringBuilder();
        //Lo que voy a leer en cada linea
        String linea;       
        
        while ((linea = lector.readLine()) != null) {
            //Concatenar las lineas
            builder.append(linea);
            JSONObject jSONObject = new JSONObject(builder.toString());
            //Te devuelve los nombres de los aray del JSON
            //System.out.println(jSONObject.names());
            JSONArray jSONArray = jSONObject.getJSONArray("results");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject object = (JSONObject) jSONArray.get(i);//Cojo el objeto de la posicion en el array
                /*String titulo = object.getString("original_title");
                String descripcion = object.getString("overview");                
                System.out.println(String.format("Titulo: %s %nDescripcion: %s", titulo,descripcion));*/
                Gson gson = new Gson();
                Pelicula pelicula = gson.fromJson(object.toString(), Pelicula.class);
                System.out.println(pelicula.getOriginal_title());
            }
        }

    }

}
