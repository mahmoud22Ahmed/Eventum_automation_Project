package Utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Properties;

public class DataUtil {
    private static final String TEST_DATA_PATH = "src/test/resources/TestData/";
    //TODO: reading data from JSON file

    /*public static String getJsonData(String fileName,String field) throws FileNotFoundException  //file + field
    {
        FileReader reader = new FileReader(TEST_DATA_PATH+ fileName + ".json");
        JsonElement jsonElement = JsonParser.parseReader(reader);
        return jsonElement.getAsJsonObject().get(field).getAsString();
    }  */

    // u must make a type casting to (String) or (String[])
    public static Object getJsonData(String fileName, String field) throws FileNotFoundException {
        FileReader reader = new FileReader(TEST_DATA_PATH + fileName + ".json");
        JsonElement jsonElement = JsonParser.parseReader(reader);

        // Get the field element from the JSON object
        JsonElement fieldElement = jsonElement.getAsJsonObject().get(field);

        if (fieldElement.isJsonArray()) {
            // If the field is an array, convert it to a String[]
            JsonArray jsonArray = fieldElement.getAsJsonArray();
            String[] result = new String[jsonArray.size()];

            for (int i = 0; i < jsonArray.size(); i++) {
                result[i] = jsonArray.get(i).getAsString();
            }

            return result;  // Return the String[] array
        } else {
            // If the field is not an array, return it as a single string
            return fieldElement.getAsString();
        }
    }

    //TODO: reading data from properties file
    public static String getPropertyValue(String fileName,String key) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(TEST_DATA_PATH+ fileName + ".properties"));
        return   properties.getProperty(key);
    }



    public static String generateRandomString(int length) {
        // Characters pool to use for generating the random string
         final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&* ";
         final SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        // Generate the random string
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }
}