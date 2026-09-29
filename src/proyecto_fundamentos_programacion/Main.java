package proyecto_fundamentos_programacion;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Main class responsible for processing the generated input files and creating
 * the required reports.
 */
public class Main {

	private static final String PRODUCTS_FILE = "data/products.txt";

	private static final String SALESMEN_FILE = "data/salesmen.txt";

	private static final String SALES_DIRECTORY = "data/sales";

	private static final String SALESMAN_REPORT = "data/salesman_report.csv";

	private static final String PRODUCTS_REPORT = "data/products_report.csv";

	/**
	 * Application entry point.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {

		System.out.println("Directorio de ejecución: " + System.getProperty("user.dir"));
		System.out.println("Ruta de productos: " + new java.io.File(PRODUCTS_FILE).getAbsolutePath());

		try {

			// Read products.
			ProductReader productReader = new ProductReader();

			Map<Integer, Product> products = productReader.readProducts(PRODUCTS_FILE);

			// Read salesmen.
			SalesmanReader salesmanReader = new SalesmanReader();

			Map<Long, Salesman> salesmen = salesmanReader.readSalesmen(SALESMEN_FILE);

			// Read sales.
			SalesReader salesReader = new SalesReader();

			Map<Long, List<Sale>> salesBySalesman = salesReader.readSales(SALES_DIRECTORY, products);

			// Generate reports.
			ReportGenerator reportGenerator = new ReportGenerator();

			reportGenerator.generateSalesmanReport(salesmen, salesBySalesman, SALESMAN_REPORT);

			reportGenerator.generateProductsReport(products, salesBySalesman, PRODUCTS_REPORT);

			System.out.println("Los reportes fueron generados correctamente.");

		} catch (IOException e) {

			System.out.println("Error al leer o escribir los archivos: " + e.getMessage());

		} catch (IllegalArgumentException e) {

			System.out.println("Error en los datos de entrada: " + e.getMessage());

		} catch (Exception e) {

			System.out.println("Ocurrió un error inesperado: " + e.getMessage());
		}
	}
}
