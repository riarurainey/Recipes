package recipes.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.service.RecipeService;
import recipes.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final UserService userService;

    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id " + id + "does not exist"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipe, @AuthenticationPrincipal User currentUser) {
        if (recipeService.existsById(id)) {
            if (recipeService.isAuthor(id, currentUser)) {
                recipeService.updateRecipe(id, recipe, currentUser);
                return new ResponseEntity<>("Recipe was updated", HttpStatus.NO_CONTENT);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to update this recipe");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id " + id + "does not exist");
        }
    }


    @PostMapping("/new")
    public Map<String, Long> saveRecipe(@Valid @RequestBody Recipe newRecipe, @AuthenticationPrincipal User currentUser) {
        Long id = recipeService.saveRecipe(newRecipe, currentUser);
        return Collections.singletonMap("id", id);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@Valid @PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        if (recipeService.existsById(id)) {
            if (recipeService.isAuthor(id, currentUser)) {
                recipeService.deleteById(id, currentUser);
                return new ResponseEntity<>("Recipe was deleted", HttpStatus.NO_CONTENT);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to delete this recipe");
            }
        } else {
            return new ResponseEntity<>("Recipe does not found", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/search", params = "category")
    public List<Recipe> getRecipesByCategory(@RequestParam String category) {
        return recipeService.getRecipesByCategory(category);
    }

    @GetMapping(value = "/search", params = "name")
    public List<Recipe> getRecipesByName(@RequestParam String name) {
        return recipeService.getRecipesByName(name);
    }

}
