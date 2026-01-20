package ru.catwarden.sltest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping
public class WebController {

    private ru.catwarden.sltest.Controller controller;

    public WebController(ru.catwarden.sltest.Controller controller){
        this.controller = controller;
    }


    @GetMapping("/main")
    public String showMainPage(Model model){
        model.addAttribute("today_birthdays",
                controller.getTodayBirthdays() != null ? controller.getTodayBirthdays() : Collections.emptyList());
        model.addAttribute("upcoming_birthdays",
                controller.getUpcomingBirthdays() != null ? controller.getUpcomingBirthdays() : Collections.emptyList());

        return "main";
    }

    @GetMapping("/all")
    public String showAllBirthdays(Model model){
        model.addAttribute("all_birthdays",
                controller.getAllBirthdayList() != null ? controller.getAllBirthdayList() : Collections.emptyList());

        return "all_birthdays";
    }

    @GetMapping("/upcoming")
    public String showUpcomingBirthdays(Model model){
        model.addAttribute("upcoming_birthdays",
                controller.getUpcomingBirthdays() != null ? controller.getUpcomingBirthdays() : Collections.emptyList());
        return "upcoming_birthdays";
    }

    @GetMapping("/today")
    public String showTodayBirthdays(Model model){
        model.addAttribute("today_birthdays",
                controller.getTodayBirthdays() != null ? controller.getTodayBirthdays() : Collections.emptyList());
        return "today_birthdays";
    }

    @GetMapping("/skipped")
    public String showSkippedBirthdays(Model model){
        model.addAttribute("skipped_birthdays",
            controller.getSkippedBirthdays() != null ? controller.getSkippedBirthdays() : Collections.emptyList());
        return "skipped_birthdays";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model){
        Birthday birthday = controller.getBirthdayById(id);
        model.addAttribute("birthday", birthday);
        return "edit_birthday";
    }

    @PostMapping("/edit")
    public String editBirthday(@ModelAttribute Birthday birthday){
        controller.editBirthday(birthday);
        return "redirect:/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteBirthday(@PathVariable int id){
        controller.deleteBirthday(id);
        return "redirect:/all";
    }
}
