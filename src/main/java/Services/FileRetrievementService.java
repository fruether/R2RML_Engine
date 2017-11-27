package Services;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**‘
 * Created by freddy on 03.07.17.
 */
public class FileRetrievementService {
	private static FileRetrievementService service;
	private String dataPath = "src/main/resources/";
	
	private FileRetrievementService() {
	
	}
	public void setDataPath(String path) {
		dataPath = path;
	}
	public static FileRetrievementService getInstance() {
		
		if(service == null) {
			service = new FileRetrievementService();
		}
		return service;
	}
	
	
	public String getContent(String uri) throws FileRetrievementServiceException{
		String content = "";
		try {
			String path = uriToPath(uri);
			byte[] encoded = Files.readAllBytes(Paths.get(getProjectRelativeResourcesPath() + path));
			content = new String(encoded);
		}
		catch (IOException ioe) {
			System.out.println("There was an IO Exception while reading the following URI " + uri);
			System.out.println(ioe.getMessage());
		}
		return content;
	}
	
	public String uriToPath(String uri) throws FileRetrievementServiceException{
		try {
			//String cleaned_uri = uri.replace("", "/");
			String cleaned_uri = URLDecoder.decode( uri, "UTF-8" );
			URL url = new URL(uri);
			return url.getPath().substring(1);
		}
		catch(Exception mue) {
			String message = "The following url is malformed: " + uri + " and cause the following error " + mue.getCause();
			throw new FileRetrievementServiceException(mue, "uriToPath", message, uri);
		}
	}
	public boolean checkContent(String uri, String content) throws FileRetrievementServiceException {
		return getContent(uri).contains(content);
	}
	public String getProjectRelativeResourcesPath() {
		return dataPath;
	}
	
	public List<int[]> getMatches(String uri, String regex) throws FileRetrievementServiceException {
		String content = getContent(uri).toUpperCase();
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		List<int[]> matchedLines = new ArrayList<>();
		
		Matcher matcher = pattern.matcher(content);
		
		while(matcher.find()) {
			int temp[] = new int[2];
			temp[0] = matcher.start();
			temp[1] = matcher.end();
			//Table name
			String query = matcher.group(0);
			String table = query.substring(query.indexOf("table".toUpperCase())  + 5, query.indexOf("("))
					.replace(" ", "")
					.replace("IFNOTEXISTS", "")
					.replace("`", "");
			matchedLines.add(temp);
			
			System.out.println(table + ": " + matcher.start() + " " + matcher.end());
		}
		
		return matchedLines;
	}
}
