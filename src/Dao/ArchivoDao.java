package Dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArchivoDao {

	public void registrarEnBbdd(String nombreDocumento) {
		DataBaseConnection db = new DataBaseConnection();
		Connection conn = null;

		try {
			// calcular hash del documento antes de insertar
			String hash = generateHash(nombreDocumento, "SHA-256");

			conn = db.getConnection();

			String sql = "INSERT INTO Archivo (ruta, hash) VALUES (?, ?)";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, nombreDocumento); // guarda el nombre
				ps.setString(2, hash); // guarda el hash
				int filasAfectadas = ps.executeUpdate();
				System.out.println("Documento insertado correctamente: " + nombreDocumento);
				System.out.println("Hash: " + hash);
				System.out.println("Filas afectadas: " + filasAfectadas);
			}
		} catch (IOException e) {
			System.err.println("Error leyendo el archivo: " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Error en la base de datos: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error generando hash: " + e.getMessage());
		} finally {
			db.closeConnection(conn);
		}
	}

	
	
	// Método para calcular hash de un archivo
	private String generateHash(String filePath, String algorithm) throws Exception {
		byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
		byte[] hashBytes = MessageDigest.getInstance(algorithm).digest(fileBytes);

		StringBuilder sb = new StringBuilder();
		for (byte b : hashBytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

}
