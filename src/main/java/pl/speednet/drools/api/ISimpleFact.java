package pl.speednet.drools.api;

import java.util.Arrays;
import java.util.List;

import pl.speednet.drools.exception.FactValueConversionNotPossibleException;

/**
  * Interface representing simple rule fact, fact have name (always) and can have value.
  * If fact has value, it can be always accessed with getStringValue() method. Value can 
  * also be accessed as different type if conversion is possible, if conversion is not possible
  * exception FactValueConversionNotPossibleException is thrown
*/
public interface ISimpleFact {
	
	public static final List<String> ERROR_FACTS = Arrays.asList(ISimpleFact.ERROR_FACT_NAME, 
			ISimpleFact.WARNING_FACT_NAME);

	/**
	 * Name of the fact reserved for error facts.
	 */
	public static final String ERROR_FACT_NAME = "ErrorFact";
	
	/**
	 * Name of the fact reserved for warning facts.
	 */
	public static final String WARNING_FACT_NAME = "WarningFact";
	
	/**
	 * Returns the name of the fact
	 * @return the fact name
	 */
	public String getName();

	/**
	 * Returns the string representation of the fact value
	 * @return the fact value
	 * @throws FactValueConversionNotPossibleException if fact value is not set
	 */
	public String getStringValue() throws FactValueConversionNotPossibleException;

	/**
	 * Returns the boolean representation of the fact value
	 * @return the fact value
	 * @throws FactValueConversionNotPossibleException if value can't be converted to boolean
	 */
	public Boolean getBooleanValue() throws FactValueConversionNotPossibleException;
	
	/**
	 * Returns the integer representation of the fact value
	 * @return the fact value
	 * @throws FactValueConversionNotPossibleException if value can't be converted to integer
	 */
	public Integer getIntegerValue() throws FactValueConversionNotPossibleException;
	
	/**
	 * Returns the additional error values (Valid only for error facts).
	 * @return the list of additional error values
	 * @throws IllegalStateException if fact is not error fact.
	 */
	public List<String> getErrorValues() throws IllegalStateException;

}
