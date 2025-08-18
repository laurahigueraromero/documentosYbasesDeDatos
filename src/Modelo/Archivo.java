package Modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import Dao.ArchivoDao;

public class Archivo {
    
    public void insertarArchivo(String ruta, String documento) throws Exception {
        // Asegurarse de que la ruta termine con /
        if (!ruta.endsWith(File.separator)) {
            ruta += File.separator;
        }
        
        // Si el documento no tiene extensión, agregar .txt
        if (!documento.endsWith(".txt")) {
            documento += ".txt";
        }
        
        String rutaCompleta = ruta + documento;
        
        try (FileWriter fw = new FileWriter(rutaCompleta)) {
            // Escribir algo en el archivo
            fw.write("Archivo creado: " + documento + "\n");
            System.out.println("Archivo creado exitosamente en: " + rutaCompleta);
            
            // Registrar en la base de datos
            ArchivoDao archivoDao = new ArchivoDao();
            archivoDao.registrarEnBbdd(documento);
            
        } catch (IOException e) {
            throw new Exception("Error al crear el archivo: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error al registrar en la base de datos: " + e.getMessage());
        }
    }
}
