package outlook.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
	static Properties prop = new Properties();
	static String input = null;
	static InputStream in = null;
	
	public static String readConfig(String key) {
		String value=null;
		try {
			
			input = new File("src/test/resources/config.properties").getAbsolutePath();
			if(input.contains("target"))
			{
				input = handleTarget(input);
			}
			
			in = new FileInputStream(input);
			prop.load(in);
			System.out.println(prop.getProperty(key));
			value=prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static String handleTarget(String Test)
	{
		String[] test2 = Test.split("target");
		test2[0] = test2[0].substring(0, test2[0].length()-1);
		//System.out.println(test2[0]+test2[1]);
		Test = test2[0]+test2[1];
		return Test;
	}

}
