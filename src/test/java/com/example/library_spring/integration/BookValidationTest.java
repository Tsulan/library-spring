package com.example.library_spring.integration;

import com.example.library_spring.controller.BookController;
import com.example.library_spring.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    public void shouldReturnBadRequest_WhenValidationFails() throws Exception {
        String invalidBookJson = """
                {
                    "title": "",
                    "author": "",
                    "isbn": "123abc",
                    "availableCopies": -1,
                    "totalCopies": 1001
                }
                """;

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("title: Title cannot be blank")))
                .andExpect(content().string(containsString("author: Author cannot be blank")))
                .andExpect(content().string(containsString("isbn: ISBN must contain only numbers and dashes")))
                .andExpect(content().string(containsString("availableCopies: Available copies must be at least 0")))
                .andExpect(content().string(containsString("totalCopies: Total copies cannot exceed 1000")));
    }
}
