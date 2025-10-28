package shodrone.core.domain.common;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;
import shodrone.dsl_plugin.DSLProcessor;


import java.io.File;

/**
 * Represents a Domain Specific Language (DSL)
 * for defining DSLs used in the system.
 */
@Embeddable
public class DSL implements ValueObject{

	private static final long serialVersionUID = 1L;

	/**
	 * The version of the DSL.
	 */
	@Getter
	private final String dslVersion;
	
	/**
	 * The syntax of the DSL.
	 * This field can be stored as a BLOB in the database using JPA annotations.
	 */
	@Getter
	@Lob
	private final String dslSyntax;

	/**
	 * Default constructor for ORM.
	 */
	protected DSL(){
		this.dslVersion = null;
		this.dslSyntax = null;
	}

	/**
	 * Constructor to create a DSL instance with specified version and syntax.
	 * @param dslVersion the version of the DSL
	 * @param dslSyntax the syntax of the DSL
	 */
	protected DSL(final String dslVersion, final String dslSyntax) {
		this.dslVersion = dslVersion;
		this.dslSyntax = dslSyntax;
	}

	/**
	 * Factory method to create a DSL instance.
	 * @param dslVersion
	 * @param dslSyntax
	 * @return a DSL instance with the specified version and syntax
	 */
	public static DSL valueOf(final String dslVersion, final String dslSyntax) {
		return tryValueOf(dslVersion, dslSyntax);
	}

	/**
	 * Factory method to test the creation of a DSL instance.
	 * @param dslVersion
	 * @param dslSyntax
	 * @return a DSL instance with the specified version and syntax
	 */
	protected static DSL tryValueOf(final String dslVersion, final String dslSyntax) {
		Preconditions.nonNull(dslVersion, "DSL version must not be null");
		Preconditions.nonNull(dslSyntax, "DSL syntax must not be null");
		Preconditions.nonEmpty(dslVersion, "DSL version must not be empty");
		Preconditions.nonEmpty(dslSyntax, "DSL syntax must not be empty");
		return new DSL(dslVersion, dslSyntax);
	}

	public static boolean validate(File file){
	return DSLProcessor.validateSyntax(file);
	}
}
