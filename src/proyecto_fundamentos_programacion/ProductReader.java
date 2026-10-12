package proyecto_fundamentos_programacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads and validates product information from a text file.
 */
public class ProductReader {

	/**
	 * Reads products from a file using the format: ID;ProductName;UnitPrice
	 *
	 * @param filePath path of the products file
	 * @return products indexed by their IDs
	 * @throws IOException              if the file cannot be read
	 * @throws IllegalArgumentException if the data is invalid
	 */
	public Map<Integer, Product> readProducts(String filePath) throws IOException {

		File file = new File(filePath);

		if (!file.exists() || !file.isFile()) {
			throw new IOException("The products file does not exist: " + filePath);
		}

		Map<Integer, Product> products = new HashMap<Integer, Product>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

			String line;
			int lineNumber = 0;

			while ((line = reader.readLine()) != null) {
				lineNumber++;

				if (line.trim().isEmpty()) {
					continue;
				}

				String[] data = line.split(";", -1);

				if (data.length != 3) {
					throw invalidLine(filePath, lineNumber, "Expected 3 fields: ID;Name;Price.");
				}

				int id;
				double price;

				try {
					id = Integer.parseInt(data[0].trim());
				} catch (NumberFormatException e) {
					throw invalidLine(filePath, lineNumber, "Product ID must be an integer.");
				}

				if (id <= 0) {
					throw invalidLine(filePath, lineNumber, "Product ID must be greater than zero.");
				}

				String name = data[1].trim();

				if (name.isEmpty()) {
					throw invalidLine(filePath, lineNumber, "Product name cannot be empty.");
				}

				try {
					price = Double.parseDouble(data[2].trim());
				} catch (NumberFormatException e) {
					throw invalidLine(filePath, lineNumber, "Product price must be numeric.");
				}

				if (!Double.isFinite(price) || price < 0) {
					throw invalidLine(filePath, lineNumber, "Product price must be finite and non-negative.");
				}

				if (products.containsKey(id)) {
					throw invalidLine(filePath, lineNumber, "Duplicate product ID: " + id);
				}

				products.put(id, new Product(id, name, price));
			}
		}

		return products;
	}

	private IllegalArgumentException invalidLine(String filePath, int lineNumber, String message) {

		return new IllegalArgumentException("File '" + filePath + "', line " + lineNumber + ": " + message);
	}
}
