package com.challenge.alura.AluraFlix.core.repositories;

import com.challenge.alura.AluraFlix.core.entities.categories.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository <Category, String> {

}
