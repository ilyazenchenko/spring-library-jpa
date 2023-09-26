package ru.zenchenko.springcourse.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zenchenko.springcourse.model.Book;
import ru.zenchenko.springcourse.model.Person;
import ru.zenchenko.springcourse.services.BooksService;
import ru.zenchenko.springcourse.services.PeopleService;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;
    private final BooksService booksService;

    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(name = "page", required = false, defaultValue = "-1") String pageNumber,
                        @RequestParam(name = "books_per_page", required = false, defaultValue = "-1") String booksPerPage,
                        @RequestParam(name = "sort_by_year", required = false, defaultValue = "false") String sortByYear) {
        model.addAttribute("booksUtil", booksService.findAll(pageNumber, booksPerPage, sortByYear));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id,
                       @ModelAttribute("personObj") Person person) {
        Optional<Book> book = booksService.findById(id);
        book.ifPresent(value -> model.addAttribute("book", value));
        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("owner", booksService.findOwnerByBookId(id));
        return "books/show";
    }

    @GetMapping("/new")
    public String addBookPage(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping("/new")
    public String addBook(@ModelAttribute("book") Book book) {
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBookPage(@PathVariable int id, Model model) {
        Optional<Book> book = booksService.findById(id);
        book.ifPresent(value -> model.addAttribute("book", value));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String editBook(@PathVariable int id, @ModelAttribute Book book) {
        booksService.update(id, book);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/appoint")
    public String appointBook(@PathVariable int id,
                              @ModelAttribute("personObj") Person person) {
        booksService.appointBook(id, person.getId());
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable int id) {
        booksService.releaseBook(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam(name = "pattern", required = false) String pattern,
                             Model model){
        model.addAttribute("books", booksService.findByPattern(pattern));
        System.out.println(booksService.findByPattern(pattern));
        return "books/search";
    }
}
