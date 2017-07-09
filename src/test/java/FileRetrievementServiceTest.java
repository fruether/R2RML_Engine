import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import org.apache.jena.base.Sys;
import org.junit.Before;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileRetrievementService Tester.
 *
 * @author <Authors name>
 * @since <pre>07/08/2017</pre>
 * @version 1.0
 */
public class FileRetrievementServiceTest {
    public FileRetrievementServiceTest() {
    }
    
    private FileRetrievementService fileRetrievementService;
    @org.junit.jupiter.api.BeforeEach
    public void onSetUp()  {
        fileRetrievementService = FileRetrievementService.getInstance();
    }
    
    
    @org.junit.jupiter.api.Test
    void test_uriToPath_correct_small() throws FileRetrievementServiceException {
        String input = "http://softlang.com/input/";
        String result = "input/";
        
        try {
            assertEquals(fileRetrievementService.uriToPath(input), result);
        } catch (Throwable e){
            assertNull(e);
        }
    }
    
    @org.junit.jupiter.api.Test
    void test_uriToPath_correct_long() throws FileRetrievementServiceException {
        String input = "http://softlang.com/plugins/maven/maven-4.0.0.xsd";
        String result = "plugins/maven/maven-4.0.0.xsd";
        
        try {
            assertEquals(fileRetrievementService.uriToPath(input), result);
        } catch (Throwable e){
            assertNull(e);
        }
    }
    @org.junit.jupiter.api.Test
    void test_uriToPath_wrong() throws FileRetrievementServiceException {
        String input = "htp://softlang/plugins/maven/maven-4.0.0.xsd";
        boolean thrown = false;
        try {
            fileRetrievementService.uriToPath(input);
        } catch (Throwable e){
            thrown = true;
        }
        assertTrue(thrown);
    }
}
