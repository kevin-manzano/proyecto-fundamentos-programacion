package proyecto_fundamentos_programacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads and validates salesman information from a text file.
 */
public class SalesmanReader {

	/**
	 * Reads salesmen using the format:
	 * DocumentType;DocumentNumber;FirstName;LastName
	 *
	 * @param filePath path of the salesmen file
	 * @return salesmen indexed by document number
	 * @throws IOException              if the file cannot be read
	 * @throws IllegalArgumentException if the data is invalid
	 */
	public Map<Long, Salesman> readSalesmen(String filePath) throws IOException {

		File file = new File(filePath);

		if (!file.exists() || !file.isFile()) {
			throw new IOException("The salesmen file does not exist: " + filePath);
		}

		Map<Long, Salesman> salesmen = new HashMap<Long, Salesman>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

			String line;
			int lineNumber = 0;

			while ((line = reader.readLine()) != null) {
				lineNumber++;

				if (line.trim().isEmpty()) {
					continue;
				}

				String[] data = line.split(";", -1);

				if (data.length != 4) {
					throw invalidLine(filePath, lineNumber,
							"Expected 4 fields: DocumentType;" + "DocumentNumber;FirstName;LastName.");
				}

				String documentType = data[0].trim();
				String firstName = data[2].trim();
				String lastName = data[3].trim();

				if (documentType.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {

					throw invalidLine(filePath, lineNumber,
							"Document type, first name and last name " + "cannot be empty.");
				}

				long documentNumber;

				try {
					documentNumber = Long.parseLong(data[1].trim());
				} catch (NumberFormatException e) {
					throw invalidLine(filePath, lineNumber, "Document number must be an integer.");
				}

				if (documentNumber <= 0) {
					throw invalidLine(filePath, lineNumber, "Document number must be greater than zero.");
				}

				if (salesmen.containsKey(documentNumber)) {
					throw invalidLine(filePath, lineNumber, "Duplicate document number: " + documentNumber);
				}

				Salesman salesman = new Salesman(documentType, documentNumber, firstName, lastName);

				salesmen.put(documentNumber, salesman);
			}
		}

		return salesmen;
	}

	private IllegalArgumentException invalidLine(String filePath, int lineNumber, String message) {

		return new IllegalArgumentException("File '" + filePath + "', line " + lineNumber + ": " + message);
	}
}
