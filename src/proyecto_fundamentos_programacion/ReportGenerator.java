package proyecto_fundamentos_programacion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

	/**
	 * Generates a report containing the total amount of money collected by each
	 * salesman.
	 *
	 * The report is sorted from the highest to the lowest amount collected.
	 *
	 * @param salesmen        map containing the salesmen indexed by document number
	 * @param salesBySalesman map containing the sales grouped by salesman
	 * @param outputFilePath  path where the report will be created
	 * @throws IOException if the report cannot be created
	 */
	public void generateSalesmanReport(Map<Long, Salesman> salesmen, Map<Long, List<Sale>> salesBySalesman,
			String outputFilePath) throws IOException {

		List<SalesmanReportEntry> reportEntries = new ArrayList<SalesmanReportEntry>();

		for (Salesman salesman : salesmen.values()) {

			long documentNumber = salesman.getDocumentNumber();

			List<Sale> sales = salesBySalesman.get(documentNumber);

			double total = calculateSalesmanTotal(sales);

			reportEntries.add(new SalesmanReportEntry(salesman, total));
		}

		Collections.sort(reportEntries, new Comparator<SalesmanReportEntry>() {

			@Override
			public int compare(SalesmanReportEntry first, SalesmanReportEntry second) {

				return Double.compare(second.getTotal(), first.getTotal());
			}
		});

		writeSalesmanReport(reportEntries, outputFilePath);
	}

	/**
	 * Calculates the total amount collected by a salesman.
	 *
	 * @param sales list of sales
	 * @return total amount collected
	 */
	private double calculateSalesmanTotal(List<Sale> sales) {

		if (sales == null) {
			return 0;
		}

		double total = 0;

		for (Sale sale : sales) {
			total += sale.getTotal();
		}

		return total;
	}

	/**
	 * Writes the salesman report to a CSV file.
	 *
	 * @param reportEntries  entries to write
	 * @param outputFilePath path of the output file
	 * @throws IOException if the file cannot be written
	 */
	private void writeSalesmanReport(List<SalesmanReportEntry> reportEntries, String outputFilePath)
			throws IOException {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

			for (SalesmanReportEntry entry : reportEntries) {

				writer.write(entry.getSalesman().getFullName() + ";" + entry.getTotal());

				writer.newLine();
			}
		}
	}

	/**
	 * Generates a report containing the products sold, sorted by the quantity sold
	 * in descending order.
	 *
	 * The report contains the product name and unit price.
	 *
	 * @param salesBySalesman map containing all sales grouped by salesman
	 * @param outputFilePath  path where the report will be created
	 * @throws IOException if the report cannot be created
	 */
	public void generateProductsReport(Map<Long, List<Sale>> salesBySalesman, String outputFilePath)
			throws IOException {

		Map<Integer, Integer> quantitiesByProduct = calculateProductQuantities(salesBySalesman);

		List<ProductQuantity> productQuantities = new ArrayList<ProductQuantity>();

		for (Map.Entry<Integer, Integer> entry : quantitiesByProduct.entrySet()) {

			productQuantities.add(new ProductQuantity(entry.getKey(), entry.getValue()));
		}

		Collections.sort(productQuantities, new Comparator<ProductQuantity>() {

			@Override
			public int compare(ProductQuantity first, ProductQuantity second) {

				return Integer.compare(second.getQuantity(), first.getQuantity());
			}
		});

		writeProductsReport(productQuantities, salesBySalesman, outputFilePath);
	}

	/**
	 * Calculates the total quantity sold for each product.
	 *
	 * @param salesBySalesman map containing all sales
	 * @return map containing product IDs and their quantities sold
	 */
	private Map<Integer, Integer> calculateProductQuantities(Map<Long, List<Sale>> salesBySalesman) {

		Map<Integer, Integer> quantitiesByProduct = new HashMap<Integer, Integer>();

		for (List<Sale> sales : salesBySalesman.values()) {

			for (Sale sale : sales) {

				int productId = sale.getProduct().getId();

				int currentQuantity = quantitiesByProduct.containsKey(productId) ? quantitiesByProduct.get(productId)
						: 0;

				quantitiesByProduct.put(productId, currentQuantity + sale.getQuantity());
			}
		}

		return quantitiesByProduct;
	}

	/**
	 * Writes the products report to a CSV file.
	 *
	 * @param productQuantities products and their quantities sold
	 * @param salesBySalesman   all sales grouped by salesman
	 * @param outputFilePath    path of the output file
	 * @throws IOException if the file cannot be written
	 */
	private void writeProductsReport(List<ProductQuantity> productQuantities, Map<Long, List<Sale>> salesBySalesman,
			String outputFilePath) throws IOException {

		Map<Integer, Product> products = getProducts(salesBySalesman);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

			for (ProductQuantity productQuantity : productQuantities) {

				Product product = products.get(productQuantity.getProductId());

				writer.write(product.getName() + ";" + product.getPrice());

				writer.newLine();
			}
		}
	}

	/**
	 * Creates a map containing all products found in the sales.
	 *
	 * @param salesBySalesman all sales grouped by salesman
	 * @return map of products indexed by ID
	 */
	private Map<Integer, Product> getProducts(Map<Long, List<Sale>> salesBySalesman) {

		Map<Integer, Product> products = new HashMap<Integer, Product>();

		for (List<Sale> sales : salesBySalesman.values()) {

			for (Sale sale : sales) {

				Product product = sale.getProduct();

				products.put(product.getId(), product);
			}
		}

		return products;
	}

	/**
	 * Represents an entry in the salesman report.
	 */
	private static class SalesmanReportEntry {

		private Salesman salesman;
		private double total;

		public SalesmanReportEntry(Salesman salesman, double total) {

			this.salesman = salesman;
			this.total = total;
		}

		public Salesman getSalesman() {
			return salesman;
		}

		public double getTotal() {
			return total;
		}
	}

	/**
	 * Represents the quantity sold for a product.
	 */
	private static class ProductQuantity {

		private int productId;
		private int quantity;

		public ProductQuantity(int productId, int quantity) {

			this.productId = productId;
			this.quantity = quantity;
		}

		public int getProductId() {
			return productId;
		}

		public int getQuantity() {
			return quantity;
		}
	}
}
