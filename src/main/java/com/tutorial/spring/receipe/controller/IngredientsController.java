package com.tutorial.spring.receipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tutorial.spring.receipe.commands.IngredientsCommand;
import com.tutorial.spring.receipe.commands.RecipeCommand;
import com.tutorial.spring.receipe.commands.UnitOfMeasureCommand;
import com.tutorial.spring.receipe.model.Ingredient;
import com.tutorial.spring.receipe.service.IIngredientService;
import com.tutorial.spring.receipe.service.IRecipeService;
import com.tutorial.spring.receipe.service.UnitOfMeasureService;

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
	
	private final IIngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;

	/**
	 * default contructor
	 * 
	 * @param recipeService		will be injected by the Spring framework
	 */
	public IngredientsController(IRecipeService recipeService, IIngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
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
	
	/**
	 * Show the details of a specific ingredient
	 */
    @GetMapping
    @RequestMapping("recipes/{recipeId}/ingredients/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String id, Model model){
    	log.debug(this.getClass().toString() + ":showRecipeIngredient - Loading the Ingredient view for the recipe with the id [" + recipeId + "] to display its ingredient and open recipes/ingredients/show");
    	
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        
        return "recipes/ingredients/show";
    }
    
    /**
     * Open the view to update a ingredient
     */
    @GetMapping
    @RequestMapping("recipes/{recipeId}/ingredients/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, 
    									 @PathVariable String ingredientId, Model model) {
    	log.debug(this.getClass().toString() + ":showRecipeIngredient - Loading the Ingredient view for the recipe with the id [" + recipeId + "] to display its ingredient and open recipes/ingredients/show");
    	
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));
        model.addAttribute("uomList", unitOfMeasureService.findAllUnits() );
        
        return "recipes/ingredients/ingredientForm";
    }
    
    /**
     * Save an ingredient and return to the list of the ingredients
     */
    @PostMapping
    @RequestMapping("recipes/{recipeId}/ingredients/save")
    public String saveRecipeIngredient(@ModelAttribute IngredientsCommand command) {
    	IngredientsCommand savedCommand = ingredientService.saveIngredientCommand(command);
    	
        log.debug(this.getClass().toString() + ":saveRecipeIngredient - Saved recipe with the id [" + savedCommand.getRecipeId() + "]");
        log.debug(this.getClass().toString() + ":saveRecipeIngredient - saved ingredient with the id [" + savedCommand.getId() + "]");
    	
    	return "redirect:/recipes/" + savedCommand.getRecipeId() + "/ingredients/" + savedCommand.getId() + "/show";
    }
    
    @GetMapping
    @RequestMapping("/recipes/{recipeId}/ingredients/new")
    public String createRecipeIngredient(@PathVariable String recipeId, Model model) {
    	RecipeCommand recipeCommand = recipeService.findRecipeCommandById(Long.valueOf(recipeId));
    	
    	if (recipeCommand == null) {
    		throw new IllegalArgumentException(this.getClass().toString() + ":createRecipeIngredient - Recipe was not found in db!");
		}
    	
    	log.debug(this.getClass().toString() + ":createRecipeIngredient - Create new ingredient for the recipe [" + recipeId + "].");
    	
    	IngredientsCommand ingredientsCommand = new IngredientsCommand();
    	// init RecipeCommand
    	ingredientsCommand.setRecipeId(Long.valueOf(recipeCommand.getId()));
    	ingredientsCommand.setRecipe(recipeCommand);
    	// init UnitOfMeasureCommand
    	ingredientsCommand.setUnitOfMeas(new UnitOfMeasureCommand());
    	
    	model.addAttribute("ingredient", ingredientsCommand);
    	model.addAttribute("uomList", unitOfMeasureService.findAllUnits());

    	return "recipes/ingredients/ingredientForm";
    }
}
