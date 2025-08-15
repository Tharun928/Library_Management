package com.wipro.bookservice.controller;

import com.wipro.bookservice.entity.Book;
import com.wipro.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Book API", description = "Operations related to book management")
public class BookController {

    private final BookService bookService;
    public BookController(BookService bookService) { this.bookService = bookService; }

    @Operation(
        summary = "Add a new book",
        description = "Creates a new book record in the system",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Book details",
            required = true,
            content = @Content(
                schema = @Schema(implementation = Book.class),
                examples = @ExampleObject(value = """
                {
                    "title": "Effective Java",
                    "author": "Joshua Bloch",
                    "category": "Programming",
                    "isbn": "9780134685991",
                    "publisher": "Addison-Wesley",
                    "publishedYear": 2018
                }
                """)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Book created successfully",
                content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        }
    )
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @Operation(
        summary = "Get all books",
        description = "Fetches all books, with optional filtering by title, author, or category",
        responses = @ApiResponse(responseCode = "200", description = "List of books",
            content = @Content(schema = @Schema(implementation = Book.class)))
    )
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(bookService.getAllBooks(title, author, category));
    }

    @Operation(
        summary = "Get book by ID",
        description = "Fetches a single book based on its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Book found",
                content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(
        summary = "Update a book",
        description = "Updates an existing book record by ID",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated book details",
            content = @Content(
                schema = @Schema(implementation = Book.class),
                examples = @ExampleObject(value = """
                {
                    "title": "Updated Title",
                    "author": "Updated Author",
                    "category": "Updated Category",
                    "isbn": "1234567890123",
                    "publisher": "Updated Publisher",
                    "publishedYear": 2024
                }
                """)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book not found")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return ResponseEntity.ok(bookService.updateBook(id, updatedBook));
    }

    @Operation(
        summary = "Delete a book",
        description = "Removes a book record from the system by ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
