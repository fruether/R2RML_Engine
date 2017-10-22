import util.Package;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by freddy on 21.10.17.
 */
public class PackageTest {
	
	private Package aPackage;
	
	@Test
	public void equals_test_correct_same_name()  {
		aPackage = new Package("javax.persistence.%2a");
		Package bPackage = new Package("javax.persistence.*");
		
		boolean result = aPackage.equals(bPackage);
		
		assertTrue(result);
	}
	
	@Test
	public void equals_test_correct_star_in_src()  {
		aPackage = new Package("javax.persistence.Table");
		Package bPackage = new Package("javax.persistence.*");
		
		boolean result = aPackage.equals(bPackage);
		
		assertTrue(result);
	}
	@Test
	public void equals_test_correct_star_in_dest()  {
		aPackage = new Package("javax.persistence.*");
		Package bPackage = new Package("javax.persistence.Table");
		
		boolean result = aPackage.equals(bPackage);
		
		assertTrue(result);
	}
	
	@Test
	public void equals_test_incorrect_typo()  {
		aPackage = new Package("javax.persistence.*");
		Package bPackage = new Package("javax.persistene.Table");
		
		boolean result = aPackage.equals(bPackage);
		
		assertFalse(result);
	}
	
	@Test
	public void equals_test_incorrect_wrong_sub_package()  {
		aPackage = new Package("javax.persistence.Peter.*");
		Package bPackage = new Package("javax.persistence.Table");
		
		boolean result = aPackage.equals(bPackage);
		
		assertFalse(result);
	}
	
	@Test
	public void equals_test_incorrect_wrong_sub_package_asyn()  {
		aPackage = new Package("javax.persistence.Peter.*");
		Package bPackage = new Package("javax.persistence.Table");
		
		boolean result = bPackage.equals(aPackage);
		
		assertFalse(result);
	}
}
