package pl.speednet.drools.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.speednet.drools.exception.FactValueConversionNotPossibleException;

public class SimpleFact implements ISimpleFact, Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = -5953377506034639599L;
	
	/** The constant representing minimum fact name lenght */
	private static Integer MINIMUM_NAME_LENGHT = 3;
	
	/** The fact name */
	private String name;

	/** The fact additional values (used for error facts) */
	private List<String> factValues = new ArrayList<String>();

	/** The fact Boolean value */
	private Boolean booleanValue; 

	/** The fact Integer value */
	private Integer integerValue; 

	/** The fact String value */
	private String stringValue;
	
	/**
	 * Private constructor, fact must have name and value.
	 */
	@SuppressWarnings("unused")
	private SimpleFact() {
		
	}

	/**
	 * Constructor without value.
	 * @param name the fact name
	 */
	public SimpleFact(String name) {
		setName(name);
	}

	/**
	 * Creates simple fact with String value. Integer and Kwota (based on BigDecimal) values are also created if conversion
	 * is possible.
	 * 
	 * @param name the fact name
	 * @param value the String value
	 */
	public SimpleFact(String name, String value) {
		setName(name);
		if (value == null) {
			throw new IllegalArgumentException("Value can't be null");
		}
		this.stringValue = value.toString();
		value = value.replaceAll(",", ".");
		try {
			this.integerValue = new Integer(value);
		} catch (NumberFormatException e) {
			// Ignore
		}
	}

	/**
	 * Additional constructor for errors with parameters. Additional list of string values is being stored.
	 * 
	 * @param name the fact name (must be equal to ERROR_FACT_NAME constant
	 * @param value the error key
	 * @param errorKey the error key
	 * @param errorValues the error values
	 */
	public SimpleFact(String name, String errorKey, String... errorValues) {
		setName(name);
		if (errorKey == null) {
			throw new IllegalArgumentException("Error Key can't be null");
		}
		if (ERROR_FACTS.contains(name)) {
			this.stringValue = errorKey;
			this.factValues.addAll(Arrays.asList(errorValues));
		} else {
			throw new IllegalArgumentException("Constructor only valid for error facts");
		}
	}

	/**
	 * Creates simple fact with Boolean value. String value is also created.
	 * 
	 * @param name the fact name
	 * @param value the Boolean value
	 */
	public SimpleFact(String name, Boolean value) {
		setName(name);
		if (value == null) {
			throw new IllegalArgumentException("Value can't be null");
		}
		this.stringValue = value.toString();
		this.booleanValue = value;
	}

	/**
	 * Creates simple fact with Integer value. String and BigDecimal values are also created.
	 * 
	 * @param name the fact name
	 * @param value the Boolean value
	 */
	public SimpleFact(String name, Integer value) {
		setName(name);
		if (value == null) {
			throw new IllegalArgumentException("Value can't be null");
		}
		this.stringValue = value.toString();
		this.integerValue = value;		
	}

	/**
	 * Creates simple fact with BigDecimal value. String value is also created.
	 * @param name the fact name
	 * @param value the Kwota value
	 */
	public SimpleFact(String name, BigDecimal value) {
		this.name = name;
		if (value == null) {
			throw new IllegalArgumentException("Value can't be null");
		}
	}

	public String getName() {
		if ((name == null) || (name.length() < 3)) {
			throw new IllegalArgumentException();
		}
		return name;
	}

	public String getStringValue() {
		return stringValue;
	}

	public Boolean getBooleanValue() throws FactValueConversionNotPossibleException {
		if (booleanValue == null) {
			throw new FactValueConversionNotPossibleException(name, "Boolean");
		}
		return booleanValue;
	}

	public Integer getIntegerValue() throws FactValueConversionNotPossibleException {
		if (integerValue == null) {
			throw new FactValueConversionNotPossibleException(name, "Integer");
		}
		return integerValue;
	}

	/**
	 * Helper setter method, checks if passed name is not null or too short.
	 */
	private void setName(String name) {
		if ((name == null) || (name.length() < MINIMUM_NAME_LENGHT)) {
			throw new IllegalArgumentException();
		} 
		this.name = name;
	}

	@Override
	public String toString() {
		return new StringBuilder()
		.append("SimpleFact (name : '").append(name)
		.append("', stringValue : '").append(stringValue)
		.append("', booleanValue : '").append(booleanValue)
		.append("', integerValue : '").append(integerValue)
		.append("' )").toString();
	}

	public List<String> getErrorValues() throws IllegalStateException {
		if (ERROR_FACTS.contains(this.name)) {
			return factValues;
		} else {
			throw new IllegalStateException("Invocation valid only for error facts");
		}
	}
	
}
