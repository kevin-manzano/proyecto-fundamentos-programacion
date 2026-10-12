package proyecto_fundamentos_programacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads and validates sales files.
 */
public class SalesReader {

	/**
	 * Reads sales files from a directory.
	 *
	 * Each file must start with: DocumentType;DocumentNumber
	 *
	 * Following lines must contain: ProductID;Quantity
	 *
	 * @param directoryPath path of the sales directory
	 * @param products      available products indexed by ID
	 * @return sales grouped by salesman document number
	 * @throws IOException              if files cannot be read
	 * @throws IllegalArgumentException if the data is invalid
	 */
	public Map<Long, List<Sale>> readSales(String directoryPath, Map<Integer, Product> products) throws IOException {

		File directory = new File(directoryPath);

		if (!directory.exists() || !directory.isDirectory()) {
			throw new IOException("The sales directory does not exist: " + directoryPath);
		}

		File[] files = directory.listFiles();

		if (files == null) {
			throw new IOException("Unable to access the sales directory: " + directoryPath);
		}

		Map<Long, List<Sale>> salesBySalesman = new HashMap<Long, List<Sale>>();

		for (File file : files) {
			if (file.isDirectory()) {
				continue;
			}

			if (!file.isFile()) {
				continue;
			}

			readSalesFile(file, products, salesBySalesman);
		}

		return salesBySalesman;
	}

	private void readSalesFile(File file, Map<Integer, Product> products, Map<Long, List<Sale>> salesBySalesman)
			throws IOException {

		String filePath = file.getPath();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

			String header = reader.readLine();

			if (header == null) {
				throw invalidLine(filePath, 1, "The sales file cannot be empty.");
			}

			String[] salesmanData = header.split(";", -1);

			if (salesmanData.length != 2 || salesmanData[0].trim().isEmpty()) {

				throw invalidLine(filePath, 1, "Expected header: DocumentType;DocumentNumber.");
			}

			long salesmanId;

			try {
				salesmanId = Long.parseLong(salesmanData[1].trim());
			} catch (NumberFormatException e) {
				throw invalidLine(filePath, 1, "Salesman document number must be an integer.");
			}

			if (salesmanId <= 0) {
				throw invalidLine(filePath, 1, "Salesman document number must be greater than zero.");
			}

			List<Sale> sales = new ArrayList<Sale>();

			String line;
			int lineNumber = 1;

			while ((line = reader.readLine()) != null) {
				lineNumber++;

				if (line.trim().isEmpty()) {
					continue;
				}

				String[] data = line.split(";", -1);

				if (data.length != 2) {
					throw invalidLine(filePath, lineNumber, "Expected 2 fields: ProductID;Quantity.");
				}

				int productId;
				int quantity;

				try {
					productId = Integer.parseInt(data[0].trim());
				} catch (NumberFormatException e) {
					throw invalidLine(filePath, lineNumber, "Product ID must be an integer.");
				}

				try {
					quantity = Integer.parseInt(data[1].trim());
				} catch (NumberFormatException e) {
					throw invalidLine(filePath, lineNumber, "Quantity must be an integer.");
				}

				if (productId <= 0) {
					throw invalidLine(filePath, lineNumber, "Product ID must be greater than zero.");
				}

				if (quantity <= 0) {
					throw invalidLine(filePath, lineNumber, "Quantity must be greater than zero.");
				}

				Product product = products.get(productId);

				if (product == null) {
					throw invalidLine(filePath, lineNumber, "Product with ID " + productId + " does not exist.");
				}

				sales.add(new Sale(product, quantity));
			}

			if (salesBySalesman.containsKey(salesmanId)) {
				throw invalidLine(filePath, 1,
						"More than one sales file was found for " + "salesman document number " + salesmanId + ".");
			}

			salesBySalesman.put(salesmanId, sales);
		}
	}

	private IllegalArgumentException invalidLine(String filePath, int lineNumber, String message) {

		return new IllegalArgumentException("File '" + filePath + "', line " + lineNumber + ": " + message);
	}
}
