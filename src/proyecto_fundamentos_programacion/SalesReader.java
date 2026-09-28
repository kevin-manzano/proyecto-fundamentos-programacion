package proyecto_fundamentos_programacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesReader {

	/**
	 * Reads all sales files from a directory.
	 *
	 * Each sales file must have the following format:
	 *
	 * DocumentType;DocumentNumber ProductId;Quantity ProductId;Quantity ...
	 *
	 * @param directoryPath path of the directory containing the sales files
	 * @param products      map containing the available products
	 * @return a map containing the sales grouped by salesman document number
	 * @throws IOException if a file cannot be read
	 */
	public Map<Long, List<Sale>> readSales(String directoryPath, Map<Integer, Product> products) throws IOException {

		Map<Long, List<Sale>> salesBySalesman = new HashMap<Long, List<Sale>>();

		File directory = new File(directoryPath);

		File[] files = directory.listFiles();

		if (files == null) {
			return salesBySalesman;
		}

		for (File file : files) {

			if (!file.isFile()) {
				continue;
			}

			readSalesFile(file, products, salesBySalesman);
		}

		return salesBySalesman;
	}

	/**
	 * Reads a single sales file.
	 *
	 * @param file            sales file
	 * @param products        map containing the available products
	 * @param salesBySalesman map where sales are grouped by salesman
	 * @throws IOException if the file cannot be read
	 */
	private void readSalesFile(File file, Map<Integer, Product> products, Map<Long, List<Sale>> salesBySalesman)
			throws IOException {

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

			String salesmanLine = reader.readLine();

			if (salesmanLine == null) {
				return;
			}

			String[] salesmanData = salesmanLine.split(";");

			long salesmanId = Long.parseLong(salesmanData[1]);

			List<Sale> sales = new ArrayList<Sale>();

			String line;

			while ((line = reader.readLine()) != null) {

				if (line.trim().isEmpty()) {
					continue;
				}

				Sale sale = parseSale(line, products);

				sales.add(sale);
			}

			salesBySalesman.put(salesmanId, sales);
		}
	}

	/**
	 * Converts a line from a sales file into a Sale object.
	 *
	 * @param line     line containing product ID and quantity
	 * @param products map containing the available products
	 * @return the Sale represented by the line
	 */
	private Sale parseSale(String line, Map<Integer, Product> products) {

		String[] data = line.split(";");

		int productId = Integer.parseInt(data[0]);
		int quantity = Integer.parseInt(data[1]);

		Product product = products.get(productId);

		if (product == null) {
			throw new IllegalArgumentException("Product with ID " + productId + " does not exist.");
		}

		return new Sale(product, quantity);
	}
}
