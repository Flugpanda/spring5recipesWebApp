package com.tutorial.spring.receipe.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Bastian Bräunel
 *
 */
public interface IImageService {

	public void saveImageFile(Long id, MultipartFile file);
}