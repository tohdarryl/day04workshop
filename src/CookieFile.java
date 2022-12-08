import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class CookieFile {

    public CookieFile(){}

    public String GetRandomCookieFromFile(String fileName) {
        List <String> lines;
        try {
            lines = Files.readAllLines(Paths.get(fileName)); //references all lines in file as a List of String
            Collections.shuffle(lines); //Shuffles list of strings
            return lines.get(0);//Pick cookie at index 0
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: No Cookie Found !";
        }

    }
    
}
