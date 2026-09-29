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
	 * Generates a report containing all salesmen sorted by the total amount of
	 * money collected, from highest to lowest.
	 *
	 * @param salesmen        all registered salesmen
	 * @param salesBySalesman sales grouped by salesman
	 * @param outputFilePath  path where the report will be generated
	 * @throws IOException if the report cannot be written
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
	 * @param sales sales made by the salesman
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
	 * @param reportEntries  sorted salesman information
	 * @param outputFilePath path where the report will be generated
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
	 * Generates a report containing the products sold, sorted by total quantity
	 * sold from highest to lowest.
	 *
	 * @param products        all available products
	 * @param salesBySalesman sales grouped by salesman
	 * @param outputFilePath  path where the report will be generated
	 * @throws IOException if the report cannot be written
	 */
	public void generateProductsReport(Map<Integer, Product> products, Map<Long, List<Sale>> salesBySalesman,
			String outputFilePath) throws IOException {

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

		writeProductsReport(products, productQuantities, outputFilePath);
	}

	/**
	 * Calculates the total quantity sold for every product.
	 *
	 * @param salesBySalesman sales grouped by salesman
	 * @return quantities grouped by product ID
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
	 * Each line contains:
	 *
	 * ProductName;ProductPrice
	 *
	 * @param products          all available products
	 * @param productQuantities products sorted by quantity sold
	 * @param outputFilePath    path where the report will be generated
	 * @throws IOException if the file cannot be written
	 */
	private void writeProductsReport(Map<Integer, Product> products, List<ProductQuantity> productQuantities,
			String outputFilePath) throws IOException {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

			for (ProductQuantity productQuantity : productQuantities) {

				Product product = products.get(productQuantity.getProductId());

				if (product == null) {
					continue;
				}

				writer.write(product.getName() + ";" + product.getPrice());

				writer.newLine();
			}
		}
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
