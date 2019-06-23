/**
 * Project: A00979176Assignment2
 * File: ApplicationException.java
 * Date: July 1, 2017
 */

package a00979176;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
@SuppressWarnings("serial")
public class ApplicationException extends Exception {

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable throwable) {
		super(throwable);
	}

}
