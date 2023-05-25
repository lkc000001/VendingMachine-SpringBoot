package com.vendingmachine.frontend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/images")
public class ImageController {
	
	@GetMapping("/{imageName}")
    public byte[] getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get("C:/data/image/" + imageName);
        return Files.readAllBytes(imagePath);
    }
}
