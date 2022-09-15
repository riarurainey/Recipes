package recipes.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public Long saveRecipe(Recipe recipe, User user) {
        recipe.setUser(user);
        recipeRepository.save(recipe);
        return recipe.getId();
    }

    public void updateRecipe(Long id, Recipe recipe, User user) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            Recipe recipeToUpdate = optionalRecipe.get();
            recipeToUpdate.setName(recipe.getName());
            recipeToUpdate.setDescription(recipe.getDescription());
            recipeToUpdate.setDirections(recipe.getDirections());
            recipeToUpdate.setCategory(recipe.getCategory());
            recipeToUpdate.setIngredients(recipe.getIngredients());
            recipeToUpdate.setUser(user);
            recipeRepository.save(recipeToUpdate);
        }
    }

    public void deleteById(Long id, User user) {
        recipeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return recipeRepository.existsById(id);
    }

    public boolean isAuthor(Long id, User user) {
        Recipe recipe = recipeRepository.findById(id).get();
        return recipe.getUser().getId().equals(user.getId());
    }

    public List<Recipe> getRecipesByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> getRecipesByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }
}





