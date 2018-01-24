package com.tutorial.spring.receipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tutorial.spring.receipe.service.IRecipeService;

import lombok.extern.slf4j.Slf4j;

/**
 * A simple spring mvc controller to serve the recipes.html page
 * and provide the necessary data through the service
 * 
 * @author Bastian Bräunel
 *
 */
@Slf4j
@Controller
public class RecipeController{

	private IRecipeService recipeService;
	
	/**
	 * Constructor based injection of the service
	 * 
	 * @param recipeService		the service injected by spring
	 */
	public RecipeController(IRecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@RequestMapping("/recipes")
	public String findAllRecipies(Model model) {
		log.debug(this.getClass().toString() + ": loading recipes webpage.");
		model.addAttribute("allRecipes", recipeService.getRecipes());
		
		return "recipes";
	}

	@RequestMapping("/recipes/show/{id}")
	public String showById(Model model, @PathVariable String id) {
		log.debug(this.getClass().toString() + ":showById - Loading the recipe gow webpage.");
		model.addAttribute("recipe", recipeService.findById(new Long(id)));
		
		return "recipes/show";
	}
}
