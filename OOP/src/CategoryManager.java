
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


class CategoryManager {
   
    private static final List<String> CATEGORIES = Collections.unmodifiableList(Arrays.asList(
        "FOOD", "TRANSPORT", "UTILITIES", "ENTERTAINMENT", "HOUSING", "HEALTH", "EDUCATION", "OTHER"
    ));

    /**
     * Returns an unmodifiable list of all predefined categories.
     * @return A List of category names (Strings).
     */
    public List<String> getAllCategories() {
        return CATEGORIES;
    }
}
