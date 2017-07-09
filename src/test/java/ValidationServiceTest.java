import Services.ValidationService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by freddy on 03.07.17.
 */
class ValidationServiceTest {
	
	@org.junit.jupiter.api.Test
	void test_validateXMLSchema_ok() {
		String xsdPath = "src/test/resources/maven-4.0.0.xsd";
		String inPath = "src/test/resources/pom.xml";
		boolean result = ValidationService.getInstance().validateXMLSchema(inPath, xsdPath);
		assertTrue(result, "The schema was validated correctly");
	}
	@org.junit.jupiter.api.Test
	void test_validateXMLSchema_wrong() {
		String xsdPath = "src/test/resources/maven-4.0.0.xsd";
		String inPath = "src/test/resources/pom_wrong.xml";
		boolean result = ValidationService.getInstance().validateXMLSchema(inPath, xsdPath);
		assertFalse(result, "The schema was validated correctly as wrong");
	}
	
}
