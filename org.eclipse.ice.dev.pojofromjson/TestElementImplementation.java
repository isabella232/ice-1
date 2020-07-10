package testpackage;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * This is an implementation of TestElement that satisfies the dependencies of
 * the @DataElement Annotation and was auto-generated by the ICE Framework.
 */
@Data
@NoArgsConstructor
public class TestElementImplementation implements TestElement, Serializable {

	/**
	 * Logging tool
	 */
	private static final Logger logger = LoggerFactory.getLogger(TestElementImplementation.class);

	/**
	 * The private UUID of this element. This field is left out of matches().
	 */
	@NonNull
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	protected java.util.UUID privateId = java.util.UUID.randomUUID();

	/**
	 * A unique identifier for this element.
	 */
	protected long id = 0L;

	/**
	 * A simple name for the data.
	 */
	@NonNull
	protected java.lang.String name = "name";

	/**
	 * A simple description of the data
	 */
	@NonNull
	protected java.lang.String description = "description";

	/**
	 * A comment that annotates the data in a meaningful way.
	 */
	@NonNull
	protected java.lang.String comment = "no comment";

	/**
	 * The context (a tag) in which the data should be considered.
	 */
	@NonNull
	protected java.lang.String context = "default";

	/**
	 * This value is true if the element should be regarded by the client as required.
	 */
	protected boolean required = false;

	/**
	 * This value is true if the element should be regarded as a secret by the client, such as for passwords.
	 */
	protected boolean secret = false;

	/**
	 * The validator used to check the correctness of the data.
	 */
	protected org.eclipse.ice.dev.annotations.JavascriptValidator<TestElement> validator;

	/**
	 * Test docs.
	 */
	protected java.lang.String test;

	/**
	 * Get privateId by alias UUID.
	 * @return privateId
	 */
	public java.util.UUID getUUID() {
		return privateId;
	}

	/**
	 * Get test by alias another.
	 * @return test
	 */
	public java.lang.String getAnother() {
		return test;
	}

	/**
	 * Set test by alias another.
	 */
	public void setAnother( java.lang.String test) {
		this.test = test;
	}

	/**
	 * Copy constructor for TestElementImplementation.
	 * @param other Instance of TestElementImplementation to copy
	 * @throws Exception if other is null or not of type TestElementImplementation or other errors.
	 */
	public TestElementImplementation(TestElementImplementation other) throws Exception {
		if (other == null) {
			throw (new Exception("TestElementImplementation to copy cannot be null."));
		}
		if (!(other instanceof TestElementImplementation)) {
			throw (new Exception("TestElementImplementation can copy only from other instances of TestElementImplementation."));
		}
		this.privateId = other.privateId;
		this.id = other.id;
		this.name = other.name;
		this.description = other.description;
		this.comment = other.comment;
		this.context = other.context;
		this.required = other.required;
		this.secret = other.secret;
		this.validator = other.validator;
		this.test = other.test;
	}

	/**
	 * This operation clones the object. Note that it differs from the base class
	 * implementation in that it will return null if it cannot create the clone to
	 * promote fast failure. See {@link java.lang.Object#clone()};
	 */
	@Override
	public Object clone() {
		try {
			// Call the copy constructor to create the clone.
			return new TestElementImplementation(this);
		} catch (Exception e) {
			logger.error("Unable to clone TestElementImplementation!", e);
			return null;
		}
	}

	/**
	 * This function checks deep equality of DataElements to see if all members are
	 * equal ("match") with the exception of fields with match set to false (such
	 * as an automatically generated UUID). This is important for checking if two
	 * objects were generated separately but are otherwise equal.
	 *
	 * @param o the other element to compare
	 * @return true if all members of the element except excluded fields match
	 *         this element.
	 */
	public boolean matches(Object o) {
		boolean retval = false;

		// Outer check for null comparisons
		if (o != null) {
			// Next check for shallow comparison
			if (this != o) {
				if (o instanceof TestElementImplementation) {
					TestElementImplementation other = (TestElementImplementation) o;

					// Separate boolean checks used to enable better catching
					// by debuggers.

					// Matching id
					boolean idMatch =
						this.id == other.id;

					// Matching name
					boolean nameMatch =
						this.name.equals(other.name);

					// Matching description
					boolean descriptionMatch =
						this.description.equals(other.description);

					// Matching comment
					boolean commentMatch =
						this.comment.equals(other.comment);

					// Matching context
					boolean contextMatch =
						this.context.equals(other.context);

					// Matching required
					boolean requiredMatch =
						this.required == other.required;

					// Matching secret
					boolean secretMatch =
						this.secret == other.secret;

					// Matching validator
					boolean validatorMatch =
						this.validator == null ?
							this.validator == other.validator :
							this.validator.equals(other.validator);

					retval =
						idMatch && nameMatch && descriptionMatch &&
						commentMatch && contextMatch && requiredMatch &&
						secretMatch && validatorMatch;
						
				}
			} else {
				// This should be true if they are the same because the deep comparison is
				// performed otherwise.
				retval = true;
			}
		}
		return retval;
	}

	/**
	 * This operation serializes the data element to a string in verified JSON.
	 *
	 * @return a JSON string describing the element
	 */
	public String toJSON() {
		String value = null;
		// Convert to json using Jackson
		ObjectMapper mapper = new ObjectMapper();
		// Set visibility so that only methods are serialized. This removes duplication
		// otherwise produced due to the convenience methods.
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
		try {
			value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("Unable to write DataElement to string!", e);
		}

		return value;
	}

	/**
	 * Load DataElement from JsonNode.
	 * @param rootNode DataElement root as JsonNode
	 * @param mapper ObjectMapper to convert values
	 */
	private void fromJsonNode(final JsonNode rootNode, final ObjectMapper mapper) throws JsonProcessingException {
		// privateId
		JsonNode privateIdNode = rootNode.get("privateId");
		privateId = mapper.treeToValue(privateIdNode, java.util.UUID.class);

		// id
		JsonNode idNode = rootNode.get("id");
		id = mapper.treeToValue(idNode, long.class);

		// name
		JsonNode nameNode = rootNode.get("name");
		name = mapper.treeToValue(nameNode, java.lang.String.class);

		// description
		JsonNode descriptionNode = rootNode.get("description");
		description = mapper.treeToValue(descriptionNode, java.lang.String.class);

		// comment
		JsonNode commentNode = rootNode.get("comment");
		comment = mapper.treeToValue(commentNode, java.lang.String.class);

		// context
		JsonNode contextNode = rootNode.get("context");
		context = mapper.treeToValue(contextNode, java.lang.String.class);

		// required
		JsonNode requiredNode = rootNode.get("required");
		required = mapper.treeToValue(requiredNode, boolean.class);

		// secret
		JsonNode secretNode = rootNode.get("secret");
		secret = mapper.treeToValue(secretNode, boolean.class);

		// validator
		JsonNode validatorNode = rootNode.get("validator");
		if (rootNode.hasNonNull("validator")) {
			validator = mapper.treeToValue(validatorNode, validator.getClass());
		} else {
			validator = null;
		}

		// test
		JsonNode testNode = rootNode.get("test");
		if (rootNode.hasNonNull("test")) {
			test = mapper.treeToValue(testNode, test.getClass());
		} else {
			test = null;
		}

	}

	/**
	 * This operation deserializes a valid JSON string and tries to load it into the
	 * object.
	 *
	 * @param jsonDataElement the contents of this data element as JSON
	 */
	public TestElement fromJSON(final String jsonDataElement) {

		// Load the data from the string with Jackson.
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(jsonDataElement);
			fromJsonNode(rootNode, mapper);
		} catch (JsonProcessingException e) {
			logger.error("Unable to read DataElement from string!", e);
		}

		return this;
	}

	/**
	 * Load from a String-Object Map, skipping the String parsing step. Structures
	 * such as <code>org.bson.Document</code> implement Map<String, Object> and
	 * therefore do not need to be processed from raw String form.
	 *
	 * @param jsonDataElement the contents of this data element as a Map<String, Object>
	 */
	public <T extends Map<String, Object>> TestElement fromJSON(final T jsonDataElement) {

		// Load the data from the string with Jackson.
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.valueToTree(jsonDataElement);
			fromJsonNode(rootNode, mapper);
		} catch (JsonProcessingException e) {
			logger.error("Unable to read DataElement from string!", e);
		}

		return this;
	}
}