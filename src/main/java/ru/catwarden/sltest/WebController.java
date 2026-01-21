package ru.catwarden.sltest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;

@Controller
@RequestMapping
public class WebController {

    private Service service;

    public WebController(Service service){
        this.service = service;
    }

    @GetMapping("/")
    public String showRootPage(){
        return "redirect:/main";
    }


    @GetMapping("/main")
    public String showMainPage(Model model){
        model.addAttribute("today_birthdays",
                service.getTodayBirthdays() != null ? service.getTodayBirthdays() : Collections.emptyList());
        model.addAttribute("upcoming_birthdays",
                service.getUpcomingBirthdays() != null ? service.getUpcomingBirthdays() : Collections.emptyList());

        return "main";
    }

    @GetMapping("/all")
    public String showAllBirthdays(Model model){
        model.addAttribute("all_birthdays",
                service.getAllBirthdayList() != null ? service.getAllBirthdayList() : Collections.emptyList());

        return "all_birthdays";
    }

    @GetMapping("/upcoming")
    public String showUpcomingBirthdays(Model model){
        model.addAttribute("upcoming_birthdays",
                service.getUpcomingBirthdays() != null ? service.getUpcomingBirthdays() : Collections.emptyList());
        return "upcoming_birthdays";
    }

    @GetMapping("/today")
    public String showTodayBirthdays(Model model){
        model.addAttribute("today_birthdays",
                service.getTodayBirthdays() != null ? service.getTodayBirthdays() : Collections.emptyList());
        return "today_birthdays";
    }

    @GetMapping("/skipped")
    public String showSkippedBirthdays(Model model){
        model.addAttribute("skipped_birthdays",
            service.getSkippedBirthdays() != null ? service.getSkippedBirthdays() : Collections.emptyList());
        return "skipped_birthdays";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model){
        Birthday birthday = service.getBirthdayById(id);
        model.addAttribute("birthday", birthday);
        return "edit_birthday";
    }

    @GetMapping("/add")
    public String showAddingForm(){
        return "add_birthday";
    }

    @PostMapping("/add")
    public String addBirthday(@RequestParam String name,
                              @RequestParam java.sql.Date date,
                              @RequestParam MultipartFile photo,
                              Model model) throws IOException{
        // DONE add the first image upload method
        // 1. Create a Birthday object with name and date from arguments^
        // 2. Save this object to the DB
        // 3. Pass the object id to the ImageHandler
        // 4. Set the Birthday photopath from the ImageHandler by the Birthday id and update the photopath for this Birthday in the DB
        // 5. ???
        // 6. PROFIT!!! (update photopath can be reused for editing birthday method if we upload a new photo)

        boolean is_error = false;

        LocalDate current_date = LocalDate.now();
        LocalDate oldest_date = current_date.minusYears(120);

        if(date.toLocalDate().isAfter(current_date) || date.toLocalDate().isBefore(oldest_date)){
            model.addAttribute("dateError", "Указана некорректная дата!");
            is_error = true;
        }

        if(is_error){
            return "add_birthday";
        }

        Birthday birthday = new Birthday();
        birthday.setName(name);
        birthday.setDate(date);
        int birthdayId = service.setNewBirthday(birthday);

        String photopath = ImageHandler.uploadPhoto(birthdayId, photo);

        service.updateBirthdayPhotopath(birthdayId, photopath);

        return "redirect:/all";
    }

    @PostMapping("/edit")
    public String editBirthday(@ModelAttribute Birthday birthday, @RequestParam MultipartFile photo) throws IOException {
        String photopath = ImageHandler.editPhoto(birthday, photo);

        birthday.setPhotoPath(photopath);
        service.editBirthday(birthday);

        return "redirect:/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteBirthday(@PathVariable int id) throws IOException{
        Birthday birthday = service.getBirthdayById(id);
        ImageHandler.deletePhoto(birthday.getPhotoPath());

        service.deleteBirthday(id);
        return "redirect:/all";
    }
}

