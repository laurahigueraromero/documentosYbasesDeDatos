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

public class Texto {
    private static final String RUTA = "/Users/laurahigueraromero/Desktop/proyectosTochos/proyectoExceld";

    public void texto() {
        // Verificar y crear directorio si no existe
        File directorio = new File(RUTA);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el nombre del documento (sin extensión):");
            String nombreDocumento = scanner.nextLine().trim();

            if (nombreDocumento.isEmpty()) {
                System.out.println("Error: El nombre del documento no puede estar vacío.");
                return;
            }

            String documentoTxt = RUTA + "/" + nombreDocumento + ".txt";
            String documentoPdf = RUTA + "/" + nombreDocumento + ".pdf";

            try {
                // Crear TXT si no existe
                File archivoTxt = new File(documentoTxt);
                boolean archivoNuevo = !archivoTxt.exists();
                
                if (archivoNuevo) {
                    archivoTxt.createNewFile();
                    System.out.println("Archivo de texto creado en: " + documentoTxt);
                }

                // Registrar en BBDD
                ArchivoDao ad = new ArchivoDao();
                ad.registrarEnBbdd(documentoTxt);

                // Leer contenido del TXT
                String content = "";
                if (!archivoNuevo) {
                    content = Files.readString(Paths.get(documentoTxt));
                    System.out.println("Contenido leído del archivo de texto.");
                }

                // Convertir a PDF
                try (PDDocument pdfDoc = new PDDocument()) {
                    PDPage page = new PDPage();
                    pdfDoc.addPage(page);

                    try (PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page)) {
                        contentStream.beginText();
                     // Para PDFBox 2.x
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.setLeading(14.5f);
                        contentStream.newLineAtOffset(25, 700);
                        
                        if (!content.isEmpty()) {
                            // Dividir el texto en líneas para evitar desbordamiento
                            String[] lines = content.split("\r?\n");
                            for (String line : lines) {
                                contentStream.showText(line);
                                contentStream.newLine();
                            }
                        } else {
                            contentStream.showText("Documento vacío o recién creado.");
                        }
                        
                        contentStream.endText();
                    }

                    // Guardar el PDF
                    pdfDoc.save(documentoPdf);
                    System.out.println("Archivo PDF generado en: " + documentoPdf);
                }

            } catch (IOException e) {
                System.err.println("Error al procesar los archivos: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
