package ru.zenchenko.springcourse.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zenchenko.springcourse.model.Person;
import ru.zenchenko.springcourse.services.PeopleService;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Person.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person p = (Person) target;

        if (peopleService.containsName(p.getName()))
            errors.rejectValue("name", "",
                    "Такое имя уже есть");
    }
}
