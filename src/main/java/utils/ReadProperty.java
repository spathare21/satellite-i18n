package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by spathare on 05/14/2019.
 */
public class ReadProperty {

    public static String configFilePath = "./src/test/resources/satellite-config.properties";


    public static String getConfig(String key) throws IOException {

        Properties prop=new Properties();
        InputStream file=new FileInputStream(configFilePath);
        prop.load(file);
        String Value=prop.getProperty(key);
        return Value;
    }

}
