import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserList {
    String[] parseRequirements = { "Course Name",
            "Course Year",
            "Version",
            "end"
    };

    List<String> parseList;

    ParserList(){
        parseList = new ArrayList<>();
        Collections.addAll(parseList, parseRequirements);
    }

}
