package proyecto_fundamentos_programacion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {

	private static final String OUTPUT_DIRECTORY = "data";
	private static final String SALES_DIRECTORY = OUTPUT_DIRECTORY + File.separator + "sales";

	private static final String SALESMEN_FILE = OUTPUT_DIRECTORY + File.separator + "salesmen.txt";

	private static final String PRODUCTS_FILE = OUTPUT_DIRECTORY + File.separator + "products.txt";

	private static final Random RANDOM = new Random();

	private static final String[] FIRST_NAMES = { "Juan", "Carlos", "Andres", "David", "Daniel", "Laura", "Maria",
			"Camila", "Sofia", "Valentina" };

	private static final String[] LAST_NAMES = { "Gomez", "Rodriguez", "Martinez", "Lopez", "Garcia", "Perez",
			"Hernandez", "Ramirez", "Torres", "Vargas" };

	public static void main(String[] args) {

		try {
			int salesmanCount = 10;
			int productsCount = 20;
			int randomSalesCount = 15;

			createDirectory(OUTPUT_DIRECTORY);
			createDirectory(SALES_DIRECTORY);

			createProductsFile(productsCount);
			createSalesManInfoFile(salesmanCount);

			for (int i = 1; i <= salesmanCount; i++) {
				String name = FIRST_NAMES[(i - 1) % FIRST_NAMES.length];
				long id = 1000000 + i;

				createSalesMenFile(randomSalesCount, name, id);
			}

			System.out.println("Los archivos de prueba fueron generados correctamente.");

		} catch (IOException e) {
			System.out.println("Ocurrió un error al generar los archivos: " + e.getMessage());
		}
	}

	/**
	 * Crea un archivo con las ventas de un vendedor.
	 *
	 * @param randomSalesCount cantidad de ventas a generar
	 * @param name             nombre del vendedor
	 * @param id               documento del vendedor
	 * @throws IOException si ocurre un error al escribir el archivo
	 */
	public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {

		String fileName = SALES_DIRECTORY + File.separator + "salesman_" + id + ".txt";

		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

		writer.write("CC;" + id);
		writer.newLine();

		for (int i = 0; i < randomSalesCount; i++) {

			int productId = 1 + RANDOM.nextInt(20);
			int quantity = 1 + RANDOM.nextInt(10);

			writer.write(productId + ";" + quantity);
			writer.newLine();
		}

		writer.close();
	}

	/**
	 * Crea un archivo con información pseudoaleatoria de productos.
	 *
	 * @param productsCount cantidad de productos a generar
	 * @throws IOException si ocurre un error al escribir el archivo
	 */
	public static void createProductsFile(int productsCount) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE));

		for (int i = 1; i <= productsCount; i++) {

			String productName = "Producto" + i;
			int price = 5000 + RANDOM.nextInt(95000);

			writer.write(i + ";" + productName + ";" + price);

			writer.newLine();
		}

		writer.close();
	}

	/**
	 * Crea un archivo con información de los vendedores.
	 *
	 * @param salesmanCount cantidad de vendedores a generar
	 * @throws IOException si ocurre un error al escribir el archivo
	 */
	public static void createSalesManInfoFile(int salesmanCount) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(SALESMEN_FILE));

		for (int i = 1; i <= salesmanCount; i++) {

			String firstName = FIRST_NAMES[(i - 1) % FIRST_NAMES.length];

			String lastName = LAST_NAMES[(i - 1) % LAST_NAMES.length];

			long id = 1000000 + i;

			writer.write("CC;" + id + ";" + firstName + ";" + lastName);

			writer.newLine();
		}

		writer.close();
	}

	/**
	 * Crea un directorio si no existe.
	 *
	 * @param directoryName nombre del directorio
	 */
	private static void createDirectory(String directoryName) {

		File directory = new File(directoryName);

		if (!directory.exists()) {
			directory.mkdirs();
		}
	}
}
