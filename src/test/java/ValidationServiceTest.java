import Services.ValidationService;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by freddy on 03.07.17.
 */
public class ValidationServiceTest {
	
	@Test
	public void test_validateXMLSchema_ok() {
		String xsdPath = "src/test/resources/maven-4.0.0.xsd";
		String inPath = "src/test/resources/pom.xml";
		boolean result = ValidationService.getInstance().validateXMLSchema(inPath, xsdPath);
		assertTrue(result, "The schema was validated correctly");
	}
	@Test
	public void test_validateXMLSchema_wrong() {
		String xsdPath = "src/test/resources/maven-4.0.0.xsd";
		String inPath = "src/test/resources/pom_wrong.xml";
		boolean result = ValidationService.getInstance().validateXMLSchema(inPath, xsdPath);
		assertFalse(result, "The schema was validated correctly as wrong");
	}
	
}
