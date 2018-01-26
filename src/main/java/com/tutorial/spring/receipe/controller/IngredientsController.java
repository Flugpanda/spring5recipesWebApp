package com.tutorial.spring.receipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tutorial.spring.receipe.commands.RecipeCommand;
import com.tutorial.spring.receipe.model.Recipe;
import com.tutorial.spring.receipe.service.IIngredientService;
import com.tutorial.spring.receipe.service.IRecipeService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Bastian Bräunel
 *
 */
@Slf4j
@Controller
public class IngredientsController {

	private IRecipeService recipeService;
	private IIngredientService ingredientService;

	/**
	 * default contructor
	 * 
	 * @param recipeService		will be injected by the Spring framework
	 */
	public IngredientsController(IRecipeService recipeService, IIngredientService ingredientService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
	}
	
	/**
	 * Load all ingredients for the given recipe
	 * 
	 *  @GetMapping		mapping http GET requests
	 */
	@GetMapping
	@RequestMapping("/recipes/{recipeId}/ingredients")
	public String listIngredients(@PathVariable String recipeId, Model model) {
		RecipeCommand recipeCommand = recipeService.findRecipeCommandById(Long.valueOf(recipeId));
		log.debug(this.getClass().toString() + ":listIngredients - Loading the RecipeCommand with the id [" + recipeId + "] to display the ingredients and open /recipes/ingredients/listIngredients.html");
		model.addAttribute("recipe", recipeCommand);

		return "/recipes/ingredients/listIngredients";
	}
	
    @GetMapping
    @RequestMapping("recipes/{recipeId}/ingredients/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String id, Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        return "recipes/ingredients/show";
    }
}
