package com.example.library;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
    @Bean
    public CommandLineRunner initData(BookRepository bookRepository) {
        return args -> {
            // Создаем полноценную книгу для нашего маркетплейса
            // Вместо старого текста harryPotterText теперь передаем прямую ссылку на PDF в самый конец!
            String desc = "История о мальчике, который выжил...";
            String url = "https://wordpress.com";

// Передаем и описание (content), и ссылку (url) в новый конструктор:
            bookRepository.save(new Book("Harry Potter", "J.K. Rowling", true, 15.99, desc, url));


            System.out.println("📚 Книга 'Harry Potter' с текстом и ценой успешно загружена в магазин!");
        };
    }

}
