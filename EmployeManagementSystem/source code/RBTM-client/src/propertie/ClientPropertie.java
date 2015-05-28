package propertie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ClientPropertie extends Properties{
	
	private static ClientPropertie instance = null;
	
	public String KeyFile = null;
	
	private ClientPropertie() throws FileNotFoundException{
        //InputStream is = getClass().getResourceAsStream("./application.properties");
		FileInputStream fis = new FileInputStream("./application.properties");
        try {  
        	load(fis);
        	KeyFile = getProperty("KeyFile");   	
        } catch (Exception e) {  
            //Throw error  
        	e.printStackTrace();
            throw new IllegalStateException("Failed to load from configure file.");  
        }
	}
	
	public static ClientPropertie getInstance() throws FileNotFoundException{
		if(instance == null){
			instance = new ClientPropertie();
		}	
		return instance;
	}
		
}
