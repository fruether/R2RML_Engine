package util;

/**
 * Created by freddy on 21.10.17.
 */


public class Package {
	private String packageName;
	
	public Package(String name) {
		packageName = name.replace("%2a", "*");
	}
	
	@Override
	public boolean equals(Object package2) {
		if(package2 instanceof  Package) {
			String packageName2 = ((Package) package2).packageName;
			int lengthPackage1 = packageName.length();
			int lengthPackage2 = packageName2.length();
			
			if(packageName2.equals(packageName)) return true;
			
			packageName2 = packageName2.replace("*", "");
			String cleanedPackageName1 = packageName.replace("*", "");
			if(lengthPackage1 < lengthPackage2) {
				return packageName2.contains(cleanedPackageName1);
			}
			else {
				return cleanedPackageName1.contains(packageName2);
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return packageName;
	}
}
