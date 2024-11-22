package com.example.library_spring;

import com.example.library_spring.entity.Book;
import com.example.library_spring.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(BookRepository bookRepository) {
        return args -> {
            Book book1 = new Book();
            book1.setTitle("Spring in Action");
            book1.setAuthor("Craig Walls");
            book1.setIsbn("9781617294945");
            book1.setAvailableCopies(5);
            book1.setTotalCopies(5);

            Book book2 = new Book();
            book2.setTitle("Java: The Complete Reference");
            book2.setAuthor("Herbert Schildt");
            book2.setIsbn("9781260440232");
            book2.setAvailableCopies(3);
            book2.setTotalCopies(3);

            bookRepository.save(book1);
            bookRepository.save(book2);
        };
    }
}
