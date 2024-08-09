package oracle.todo.service;

import jakarta.enterprise.context.ApplicationScoped;
import oracle.todo.model.Category;
import oracle.todo.model.json.CategoryJSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryService {

    public List<CategoryJSON> getAllCategories() {
        List<CategoryJSON> categoryJSONS = new ArrayList<>();

        List<Category> categories = Category.listAll();

        for (Category c : categories) {
            CategoryJSON categoryJSON = new CategoryJSON();
            categoryJSON.setId(c.id);
            categoryJSON.setName(c.getName());
            categoryJSONS.add(categoryJSON);
        }

        return categoryJSONS;
    }

    public CategoryJSON getCategory(Long id) throws Exception {
        Optional<Category> optionalCategory = Category.findByIdOptional(id);
        if (optionalCategory.isEmpty()) throw new Exception("Category does not exist.");

        CategoryJSON categoryJSON = new CategoryJSON();
        categoryJSON.setId(optionalCategory.get().id);
        categoryJSON.setName(optionalCategory.get().getName());

        return categoryJSON;
    }
}
