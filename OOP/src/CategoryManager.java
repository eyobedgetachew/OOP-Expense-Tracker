
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


class CategoryManager {
   
    private static final List<String> CATEGORIES = Collections.unmodifiableList(Arrays.asList(
        "FOOD", "TRANSPORT", "UTILITIES", "ENTERTAINMENT", "HOUSING", "HEALTH", "EDUCATION", "OTHER"
    ));

  
    public List<String> getAllCategories() {
        return CATEGORIES;
    }
}
