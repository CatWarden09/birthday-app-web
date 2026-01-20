package ru.catwarden.sltest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/birthdays")
public class BirthdayWebController {

    private ru.catwarden.sltest.Controller controller;

    public BirthdayWebController(ru.catwarden.sltest.Controller controller){
        this.controller = controller;
    }


    @GetMapping("/all")
    public String showAllBirthdays(Model model){
        model.addAttribute("all_birthdays",
                controller.getAllBirthdayList() != null ? controller.getAllBirthdayList() : Collections.emptyList());

        return "all_birthdays";
    }

    @GetMapping("/main")
    public String showMainPage(Model model){
        model.addAttribute("today_birthdays",
                controller.getTodayBirthdays() != null ? controller.getTodayBirthdays() : Collections.emptyList());
        model.addAttribute("upcoming_birthdays",
                controller.getUpcomingBirthdays() != null ? controller.getUpcomingBirthdays() : Collections.emptyList());

        return "main";
    }


}
