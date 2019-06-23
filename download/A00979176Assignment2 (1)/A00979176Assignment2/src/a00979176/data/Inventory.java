/**
 * Project: A00979176Assignment2
 * File: Inventory.java
 * Date: July 1, 2017
 */

package a00979176.data;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class Inventory {

	public static final int ATTRIBUTE_COUNT = 5;

	private String description;
	private String partNumber;
	private int price;
	private int quantity;
	private String motorcycleId;

	/**
	 * @author Rodrigo Silva, A00979176
	 *
	 */
	public static class Builder {
		private final String partNumber;

		private String description;
		private int price;
		private int quantity;
		private String motorcycleId;

		/**
		 * Builds the inventory object ensuring the required fields are met.
		 * 
		 * @param partNumber
		 *            the part number
		 */
		public Builder(String partNumber) {
			this.partNumber = partNumber;
		}

		/**
		 * @param description
		 *            the description to set
		 * @return the Inventory.Builder object to allow for method chaining.
		 */
		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		/**
		 * @param price
		 *            the price to set
		 * @return the Inventory.Builder object to allow for method chaining.
		 */
		public Builder setPrice(int price) {
			this.price = price;
			return this;
		}

		/**
		 * @param quantity
		 *            the quantity to set
		 * @return the Inventory.Builder object to allow for method chaining.
		 */
		public Builder setQuantity(int quantity) {
			this.quantity = quantity;
			return this;
		}

		/**
		 * @param motorcycleId
		 *            the motorcycleId to set
		 * @return the Inventory.Builder object to allow for method chaining.
		 */
		public Builder setMotorcycleId(String motorcycleId) {
			this.motorcycleId = motorcycleId;
			return this;
		}

		/**
		 * Builds the inventory
		 *
		 * @return the Inventory.Builder object to allow for method chaining.
		 */
		public Inventory build() {
			return new Inventory(this);
		}
	}

	/**
	 * Default Constructor
	 * 
	 * @param builder
	 *            the builder object
	 */
	public Inventory(Builder builder) {
		this.description = builder.description;
		this.partNumber = builder.partNumber;
		this.price = builder.price;
		this.quantity = builder.quantity;
		this.motorcycleId = builder.motorcycleId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}

	/**
	 * @param partNumber
	 *            the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the motorcycleId
	 */
	public String getMotorcycleId() {
		return motorcycleId;
	}

	/**
	 * @param motorcycleId
	 *            the motorcycleId to set
	 */
	public void setMotorcycleId(String motorcycleId) {
		this.motorcycleId = motorcycleId;
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
		return String.format("%s %s %d %d %s", getDescription(), getPartNumber(), getPrice(), getQuantity(),
				getMotorcycleId());

	}

}
