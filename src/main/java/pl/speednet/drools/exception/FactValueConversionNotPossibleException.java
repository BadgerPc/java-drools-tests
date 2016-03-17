package pl.speednet.drools.exception;

/**
 * Exception representing situation when it is illegal to return value as requested type
 */
public class FactValueConversionNotPossibleException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8288706497464113609L;

	/**
	 * Instantiates a new conversion not possible exception.
	 * 
	 * @param dictionaryType
	 *            the dictionary type
	 * @param classType
	 *            the class type
	 */
	public FactValueConversionNotPossibleException(String factValue, String requestedType) {
		super(new StringBuilder()
			.append("cannot return fact value '")
			.append(factValue)
			.append("' as type  '")
			.append(requestedType)
			.append("'")
			.toString());
	}
	
}

