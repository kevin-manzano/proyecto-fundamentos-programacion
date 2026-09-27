package proyecto_fundamentos_programacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductReader {

	/**
	 * Reads the products from a text file.
	 *
	 * Each line of the file must have the following format:
	 *
	 * ID;ProductName;UnitPrice
	 *
	 * @param filePath path of the products file
	 * @return a map containing the products indexed by their ID
	 * @throws IOException if the file cannot be read
	 */
	public Map<Integer, Product> readProducts(String filePath) throws IOException {

		Map<Integer, Product> products = new HashMap<Integer, Product>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

			String line;

			while ((line = reader.readLine()) != null) {

				if (line.trim().isEmpty()) {
					continue;
				}

				Product product = parseProduct(line);

				products.put(product.getId(), product);
			}
		}

		return products;
	}

	/**
	 * Converts a line from the products file into a Product object.
	 *
	 * @param line line containing product information
	 * @return the Product represented by the line
	 */
	private Product parseProduct(String line) {

		String[] data = line.split(";");

		int id = Integer.parseInt(data[0]);
		String name = data[1];
		double price = Double.parseDouble(data[2]);

		return new Product(id, name, price);
	}
}
