import junit.extensions.ActiveTestSuite;

import java.util.LinkedHashMap;
import java.util.List;
import java.math.sqrt;
import java.lang.*;

@Table(name="Peter")
public class SampleClassDeclarations {
	@Column
	private String test;
	private Object hallo;
	private LinkedHashMap linkedHashMap;
	
	public void doStuff() {
		int x = 12;
		String a = "Hallo";
		System.out.println(a);
		double result = Math.sqrt(x);
		
	}
	
	public void doStuff3() {
			Person x = new Person();
		
	}
	public StringBuilder doStuff3() {
		Person x = new Person();
		
	}
	
	public StringBuilder doStuff5(Peter x) {
		Person x = new Person();
		
	}
}
