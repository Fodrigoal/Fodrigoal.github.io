/**
 * Project: A00979176Assignment2
 * File: Motorcycle.java
 * Date: July 1, 2017
 */

package a00979176.data;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class Motorcycle {

	public static final int ATTRIBUTE_COUNT = 7;

	private long id;
	private String make;
	private String model;
	private int year;
	private String serialNumber;
	private int mileage;
	private long customerId;

	/**
	 * @author Rodrigo Silva, A00979176
	 *
	 */
	public static class Builder {
		private long id;

		private String make;
		private String model;
		private int year;
		private String serialNumber;
		private int mileage;
		private long customerId;

		/**
		 * Builds the customer object ensuring the required fields are met.
		 * 
		 * @param id
		 *            the motorcycle ID number
		 */
		public Builder(long id) {
			this.id = id;
		}

		/**
		 * @param make
		 *            the make to set
		 * @return the Motorcycle.Builder object to allow for method chaining.
		 */
		public Builder setId(Long id) {
			this.id = id;
			return this;
		}
		
		/**
		 * @param make
		 *            the make to set
		 * @return the Motorcycle.Builder object to allow for method chaining.
		 */
		public Builder setMake(String make) {
			this.make = make.substring(0, 1).toUpperCase() + make.substring(1).toLowerCase();
			return this;
		}

		/**
		 * @param model
		 *            the model to set
		 * @return the Motorcycle.Builder object to allow for method chaining.
		 */
		public Builder setModel(String model) {
			this.model = model.substring(0, 1).toUpperCase() + model.substring(1).toLowerCase();
			return this;
		}

		/**
		 * @param year
		 *            the year to set
		 * @return the Motorcycle.Builder object to allow for method chaining.
		 */
		public Builder setYear(int year) {
			this.year = year;
			return this;
		}

		/**
		 * @param serialNumber
		 *            the serialNumber to set
		 * @return the Motorcycle.Builder object to allow for method chaining.
		 */
		public Builder setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber.toUpperCase();
			return this;
		}

		/**
		 * @param mileage
		 *            the mileage to set
		 * @return the Motorcycle.Builder object to allow for method chaining.
		 */
		public Builder setMileage(int mileage) {
			this.mileage = mileage;
			return this;
		}

		/**
		 * @param customerId
		 *            the customerId to set
		 * @return the Motorcycle.Builder object to allow for method chaining.
		 */
		public Builder setCustomerId(long customerId) {
			this.customerId = customerId;
			return this;
		}

		/**
		 * Builds the motorcycle object
		 *
		 * @return the Motorcycle.Builder object to allow for method chaining.
		 */
		public Motorcycle build() {
			return new Motorcycle(this);
		}

	}

	/**
	 * Default constructor
	 * 
	 * @param builder
	 *            the builder object
	 */
	private Motorcycle(Builder builder) {
		this.id = builder.id;
		this.make = builder.make;
		this.model = builder.model;
		this.year = builder.year;
		this.serialNumber = builder.serialNumber;
		this.mileage = builder.mileage;
		this.customerId = builder.customerId;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * @param make
	 *            the make to set
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * Get the attribute count used for input validation.
	 *
	 * @return the attribute count
	 */
	public static int getAttributeCount() {
		return ATTRIBUTE_COUNT;
	}

	@Override
	public String toString() {
		return String.format("%-7s %-29s %-16s %21s %18s", getYear(), getMake(), getModel(), getMileage(), 
				getCustomerId());
	}
}
