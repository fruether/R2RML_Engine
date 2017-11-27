import Services.FileRetrievementService;
import Services.FileRetrievementServiceException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

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
    @Before
    public void onSetUp()  {
        fileRetrievementService = FileRetrievementService.getInstance();
    }
    
    
    @Test
    public void test_uriToPath_correct_small() throws FileRetrievementServiceException {
        String input = "http://softlang.com/input/";
        String result = "input/";
        
        try {
            assertEquals(fileRetrievementService.uriToPath(input), result);
        } catch (Throwable e){
            assertNull(e);
        }
    }
    

    
    @Test
    public void test_uriToPath_correct_long() throws FileRetrievementServiceException {
        String input = "http://softlang.com/plugins/maven/maven-4.0.0.xsd";
        String result = "plugins/maven/maven-4.0.0.xsd";
        
        try {
            assertEquals(fileRetrievementService.uriToPath(input), result);
        } catch (Throwable e){
            assertNull(e);
        }
    }
    
    @Test
    public void name() throws Exception {
    }
    
    @Test
    public void test_uriToPath_wrong() throws FileRetrievementServiceException {
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

