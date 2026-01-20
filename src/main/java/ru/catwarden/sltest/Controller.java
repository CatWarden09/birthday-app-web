package ru.catwarden.sltest;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

@Service
public class Controller {
    private Database db;

    public Controller(Database db){
        this.db = db;
    }

    public List<BirthdayWithIndex> getAllBirthdayList(){
        List<BirthdayWithIndex> list_parsed = new ArrayList<>();

        List<Birthday> list = db.getAllBirthdays();

        for(int i =0; i<list.size();i++){
            BirthdayWithIndex birthday = new BirthdayWithIndex();
            birthday.setId(list.get(i).getId());
            birthday.setIndex(i+1);
            birthday.setName(list.get(i).getName());
            birthday.setDate(list.get(i).getDate());
            birthday.setPhotopath(list.get(i).getPhotoPath());

            list_parsed.add(birthday);
        }
        return list_parsed;
    }

    public void setNewBirthday(String name, Date date){

        db.setNewBirthday(name, date);

    }

    public int getBirthdayId(int index, List<BirthdayWithIndex> list){
        int id = -1;
        for(BirthdayWithIndex birthday:list){
            if(birthday.getIndex() == index){
                id = birthday.getId();
            }
        }
        return id;
    }

    public void deleteBirthday(int id){
        db.deleteBirthday(id);
    }

    public void editBirthday(Birthday birthday){
        db.editBirthday(birthday.getId(), birthday.getDate(), birthday.getName(), birthday.getPhotoPath());
    }

    public List<BirthdayWithIndex> getTodayBirthdays(){
        LocalDate current_date = LocalDate.now();

        int current_month = current_date.getMonthValue();
        int current_day = current_date.getDayOfMonth();
        int current_year = current_date.getYear();

        List<BirthdayWithIndex> list_parsed= new ArrayList<>();

        List<Birthday> list = db.getTodayBirthdays(current_month, current_day);

        for(int i = 0; i<list.size();i++){
            BirthdayWithIndex birthday = new BirthdayWithIndex();
            birthday.setIndex(i+1);
            birthday.setName(list.get(i).getName());

            int year_parsed = list.get(i).getDate().toLocalDate().getYear();
            birthday.setAge(current_year-year_parsed);

            list_parsed.add(birthday);
        }
    return list_parsed;
    }

    public List<BirthdayWithIndex> getUpcomingBirthdays(){
        List<Birthday> list = db.getAllBirthdays();
        List<BirthdayWithIndex> parsed_list = new ArrayList<>();

        // exclude today by adding +1 to the current date
        LocalDate start_date = LocalDate.now().plusDays(1);
        LocalDate end_date = start_date.plusDays(7);
        int current_year = start_date.getYear();
        int index = 1;

        // this loop checks if the birthday in inside the given date range
        // we get only the month and day of the birthday because in the DB the date is stored with the year of birth and we need to get rid of it there (or no birthdays will be in the date range because of the year)
        // to work correctly with the year gap (like if some of the next 7 days are in the next year), check the birthdays, which month and day are lesser than current date, in the next year, by adding +1 year to their date
        // so if the month and day of a birthday with a current year are lesser than current month and day of the current date with year (like 01.02.25 < 31.12.25) we need to check this pair of month and day in the next year
        // some of the actual birthdays within the next 7 days might get lost because of the current year (01.01.26 would be in the date range if the current date is 31.12.25 end date is 06.01.26, but the 01.01.25 will not)
        for(Birthday birthday:list){
            MonthDay birthday_month_day = MonthDay.from(birthday.getDate().toLocalDate());

            // get the birthday date with the current year (get month and day, then assign a year)
            LocalDate current_birthday_date = birthday_month_day.atYear(start_date.getYear());

            // if the birthday already was in this year, assign the next year
            if (current_birthday_date.isBefore(start_date)) {
                current_birthday_date = birthday_month_day.atYear(start_date.getYear() + 1);
            }

            // check if the birthday is inside the dates range and create a Birthday object
            if (!current_birthday_date.isBefore(start_date) && !current_birthday_date.isAfter(end_date)) {
                BirthdayWithIndex birthday_parsed = new BirthdayWithIndex();
                birthday_parsed.setIndex(index);
                birthday_parsed.setName(birthday.getName());
                birthday_parsed.setDate(java.sql.Date.valueOf(current_birthday_date));


                int year_parsed = birthday.getDate().toLocalDate().getYear();

                // set the person's age depending on the next year check (if the birthday is in the next year, add +1 to the age)
                if(current_birthday_date.getYear()> start_date.getYear()){
                    birthday_parsed.setAge((current_year+1)-year_parsed);
                } else{
                    birthday_parsed.setAge(current_year-year_parsed);
                }

                parsed_list.add(birthday_parsed);
                ++index;
            }
        }
        return parsed_list;
    }

    public List<BirthdayWithIndex> getSkippedBirthdays(){
        List<Birthday> list = db.getAllBirthdays();
        List<BirthdayWithIndex> parsed_list = new ArrayList<>();

        // get the date the 7 days before and the date 1 day before today (to avoid current birthdays)
        LocalDate end_date = LocalDate.now().minusDays(1);
        LocalDate start_date = end_date.minusDays(7);
        int current_year = end_date.getYear();

        int index = 1;

        // this loop works exactly like the upcoming birthdays case, we just check the previous year instead of next
        for(Birthday birthday:list){
            MonthDay birthday_month_day = MonthDay.from(birthday.getDate().toLocalDate());

            // get the birthday date with the current year (get month and day, then assign a year)
            LocalDate current_birthday_date = birthday_month_day.atYear(end_date.getYear());

            // if the birthday was not yet in this year, assign the previous year
            if (current_birthday_date.isAfter(end_date)){
                current_birthday_date = birthday_month_day.atYear(end_date.getYear() - 1);
            }

            // check if the birthday in inside the dates range and create a Birthday object
            if((!current_birthday_date.isBefore(start_date) && !current_birthday_date.isAfter(end_date))){
                BirthdayWithIndex birthday_parsed = new BirthdayWithIndex();
                birthday_parsed.setIndex(index);
                birthday_parsed.setName(birthday.getName());
                birthday_parsed.setDate(java.sql.Date.valueOf(current_birthday_date));

                int year_parsed = birthday.getDate().toLocalDate().getYear();

                // set the person's age depending on the previous year check (if the birthday was in the last year, subtract -1 from the age)
                if(current_birthday_date.getYear()< end_date.getYear()){
                    birthday_parsed.setAge((current_year-1)-year_parsed);
                } else{
                    birthday_parsed.setAge(current_year-year_parsed);
                }

                parsed_list.add(birthday_parsed);
                ++index;
            }
        }
        return  parsed_list;
    }
    public Birthday getBirthdayById(int id){
        return db.getBirthdayById(id);
    }
}
