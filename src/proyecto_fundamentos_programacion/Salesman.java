package proyecto_fundamentos_programacion;

public class Salesman {

	private String documentType;
	private long documentNumber;
	private String firstName;
	private String lastName;

	public Salesman(String documentType, long documentNumber, String firstName, String lastName) {

		this.documentType = documentType;
		this.documentNumber = documentNumber;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public long getDocumentNumber() {
		return documentNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Override
	public String toString() {
		return documentType + ";" + documentNumber + ";" + firstName + ";" + lastName;
	}
}
