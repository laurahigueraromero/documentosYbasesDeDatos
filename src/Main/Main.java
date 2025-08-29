package Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import Dao.ArchivoDao;

public class Main {
	private static final String RUTA = "/Users/laurahigueraromero/Desktop/proyectosTochos/proyectoExceld";

	public static void main(String[] args) {
		// Crear directorio si no existe
		new File(RUTA).mkdirs();

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Ingrese el nombre del documento (sin extensión):");
			String nombre = scanner.nextLine().trim();

			System.out.println("Escriba el texto dxel documento:");
			String texto = scanner.nextLine();

			if (nombre.isEmpty()) {
				System.out.println("Error: El nombre no puede estar vacío.");
				return;
			}

			String txtFile = RUTA + "/" + nombre + ".txt";
			String pdfFile = RUTA + "/" + nombre + ".pdf";

			try {
				// Crear archivo de texto si no existe
				File file = new File(txtFile);
				if (!file.exists()) {
					file.createNewFile();
					System.out.println("Archivo creado: " + txtFile);
				}

				// Guardar el texto ingresado en el archivo
				Files.write(Paths.get(txtFile), texto.getBytes());
				System.out.println("Texto guardado en: " + txtFile);

				// Leer contenido del archivo (para generar PDF)
				String content = new String(Files.readAllBytes(Paths.get(txtFile)));

				// Crear PDF
				try (PDDocument doc = new PDDocument()) {
					PDPage page = new PDPage();
					doc.addPage(page);

					try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA, 12);
						contentStream.newLineAtOffset(50, 700);

						if (!content.isEmpty()) {
							for (String line : content.split("\r?\n")) {
								contentStream.showText(line);
								contentStream.newLineAtOffset(0, -15);
							}
						} else {
							contentStream.showText("Documento vacío");
						}

						contentStream.endText();
					}

					// Guardar PDF
					doc.save(pdfFile);
					System.out.println("PDF generado: " + pdfFile);
				}

				// Registrar en base de datos
				new ArchivoDao().registrarEnBbdd(txtFile);

			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
			}

		} catch (Exception e) {
			System.err.println("Error inesperado: " + e.getMessage());
		}
	}
}
