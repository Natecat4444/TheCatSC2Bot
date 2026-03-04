package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionUtil {
    public static String getVersion() {
        try (InputStream strem = VersionUtil.class.getResourceAsStream("/version.properties")) {
            Properties properties = new Properties();
            properties.load(strem);
            return properties.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        } catch (NullPointerException e){
            System.out.println("WARNING, PROPS FILE NOT FOUND");
            return "error";
        }
    }
}
