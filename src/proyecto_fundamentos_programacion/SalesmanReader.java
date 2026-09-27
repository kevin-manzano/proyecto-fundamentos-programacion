package proyecto_fundamentos_programacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SalesmanReader {

	/**
	 * Reads the salesmen information from a text file.
	 *
	 * Each line of the file must have the following format:
	 *
	 * DocumentType;DocumentNumber;FirstName;LastName
	 *
	 * @param filePath path of the salesmen file
	 * @return a map containing the salesmen indexed by their document number
	 * @throws IOException if the file cannot be read
	 */
	public Map<Long, Salesman> readSalesmen(String filePath) throws IOException {

		Map<Long, Salesman> salesmen = new HashMap<Long, Salesman>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

			String line;

			while ((line = reader.readLine()) != null) {

				if (line.trim().isEmpty()) {
					continue;
				}

				Salesman salesman = parseSalesman(line);

				salesmen.put(salesman.getDocumentNumber(), salesman);
			}
		}

		return salesmen;
	}

	/**
	 * Converts a line from the salesmen file into a Salesman object.
	 *
	 * @param line line containing salesman information
	 * @return the Salesman represented by the line
	 */
	private Salesman parseSalesman(String line) {

		String[] data = line.split(";");

		String documentType = data[0];
		long documentNumber = Long.parseLong(data[1]);
		String firstName = data[2];
		String lastName = data[3];

		return new Salesman(documentType, documentNumber, firstName, lastName);
	}
}
