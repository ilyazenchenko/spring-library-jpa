package ru.zenchenko.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zenchenko.springcourse.model.Person;
import ru.zenchenko.springcourse.services.BooksService;
import ru.zenchenko.springcourse.services.PeopleService;
import ru.zenchenko.springcourse.util.PersonValidator;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonValidator personValidator;

    private final PeopleService peopleService;

    private final BooksService booksService;

    public PeopleController(PersonValidator personValidator, PeopleService peopleService, BooksService booksService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id) {
        Optional<Person> person = peopleService.findById(id);
        person.ifPresent(value -> model.addAttribute("person", value));
        model.addAttribute("bookList", booksService.findByPersonId(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String addPersonPage(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping("/new")
    public String addPerson(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/new";
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPersonPage(@PathVariable int id, Model model) {
        Optional<Person> person = peopleService.findById(id);
        person.ifPresent(value -> model.addAttribute("person", value));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String editPerson(@PathVariable int id,
                             @ModelAttribute @Valid Person person,
                             BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/edit";
        peopleService.update(id, person);
        return "redirect:/people/" + id;
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

}
