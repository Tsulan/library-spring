package com.example.library_spring;

import com.example.library_spring.entity.AppUser;
import com.example.library_spring.entity.Book;
import com.example.library_spring.repository.AppUserRepository;
import com.example.library_spring.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(BookRepository bookRepository, AppUserRepository appUserRepository) {
        return args -> {
            System.out.println("\nInitializing data...\n");

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
            System.out.println("Initial books: " + bookRepository.findAll());

            AppUser appUser1 = new AppUser();
            appUser1.setName("Herbert Schildt");
            appUser1.setEmail("herbert.schildt@gmail.com");
            appUser1.setPassword("Herbert Schildt");

            AppUser appUser2 = new AppUser();
            appUser2.setName("John Doe");
            appUser2.setEmail("john.doe@gmail.com");
            appUser2.setPassword("John Doe");

            appUserRepository.save(appUser1);
            appUserRepository.save(appUser2);
            System.out.println("Initial users: " + appUserRepository.findAll());
        };
    }
}
