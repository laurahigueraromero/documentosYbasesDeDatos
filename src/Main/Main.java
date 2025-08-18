package Main;

import java.io.File;
import java.io.IOException;

import Dao.ArchivoDao;
import Modelo.Archivo;

public class main {
	private static final String RUTA = "/Users/laurahigueraromero/Desktop/proyectosTochos/proyectoExceld";

	public static void main(String[] args) throws IOException {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		System.out.println("Ingrese el nombre del documento:");
		String nombreDocumento = scanner.nextLine();

		String documento = RUTA + "/" + nombreDocumento + ".txt";

		File archivo = new File(documento);
		if (!archivo.exists()) {
			archivo.createNewFile();
			System.out.println("Archivo creado exitosamente en: " + documento);
		}
		ArchivoDao ad = new ArchivoDao();

		ad.registrarEnBbdd(documento);

	}
}
