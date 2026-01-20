    package ru.catwarden.sltest;

    import java.sql.Date;
    import java.time.DateTimeException;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.*;

    public class Console {
        private Scanner scanner;
        private Controller controller;

        public Console(Scanner scanner, Controller controller) {
            this.scanner = scanner;
            this.controller = controller;

        }

        public void showUi(){

            while(true) {
                printSeparator();
                System.out.println("Введите команду:");
                System.out.println("1. Показать текущие и ближайшие ДР");
                System.out.println("2. Показать все ДР");
                System.out.println("3. Добавить новый ДР");
                System.out.println("4. Показать прошедшие ДР");
                System.out.println("5. Удалить ДР");
                System.out.println("6. Изменить ДР");
                System.out.println("0. Выйти");

                String cmd = scanner.nextLine();

                switch (cmd){
                    case "1":
                        showCurrentBirthdays();
                        showUpcomingBirthdays();
                        break;
                    case "2":
                        showAllBirthdays();
                        break;
                    case "3":
                        setNewBirthday();
                        break;
                    case "4":
                        showSkippedBirthdays();
                        break;
                    case "5":
                        deleteBirthday();
                        break;
                    case "6":
                        editBirthday();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Некорректная команда!");

                }
            }
        }


        public void showCurrentBirthdays(){
            List<BirthdayWithIndex> list = controller.getTodayBirthdays();

            printSeparator();
            System.out.println("Список текущих ДР:");

            if(!list.isEmpty()) {
                for (BirthdayWithIndex birthday : list) {
                    System.out.print(birthday.getIndex() + "." + " ");
                    System.out.print(birthday.getName() + " сегодня празднует своё ");
                    System.out.println(birthday.getAge() + "-летие!");
                }
            } else{
                System.out.println("Сегодня ДР нет!");
            }
        }

        public void showUpcomingBirthdays(){
            List<BirthdayWithIndex> list = controller.getUpcomingBirthdays();

            printSeparator();
            System.out.println("Список ближайших ДР:");

            if(!list.isEmpty()) {
                for (BirthdayWithIndex birthday : list) {
                    System.out.print(birthday.getIndex() + "." + " ");
                    System.out.print(birthday.getName() + " ");
                    System.out.print(birthday.getDate());
                    System.out.println(" будет праздновать своё " + birthday.getAge() + "-летие!");
                }
            } else{
                System.out.println("В ближайшую неделю ДР нет!");
            }

        }

        public void showSkippedBirthdays(){
            List<BirthdayWithIndex> list = controller.getSkippedBirthdays();

            if(list.isEmpty()){
                System.out.println("ДР не найдены!");
                return;
            }

            printSeparator();
            System.out.println("Список прошедших ДР:");
            for(BirthdayWithIndex birthday :list){
                System.out.print(birthday.getIndex() + "." + " ");
                System.out.print(birthday.getName() + " ");
                System.out.print(birthday.getDate());
                System.out.println(" праздновал своё " + birthday.getAge() + "-летие!");
            }
        }


        public void showAllBirthdays(){
            List<BirthdayWithIndex> list = controller.getAllBirthdayList();

            if(list.isEmpty()){
                System.out.println("ДР не найдены!");
                return;
            }

            printSeparator();
            System.out.println("Список всех ДР:");
            for(BirthdayWithIndex birthday :list){
                System.out.print(birthday.getIndex() + "." + " ");
                System.out.print(birthday.getName() + " ");
                System.out.println(birthday.getDate());
            }
        }

        public void setNewBirthday(){


            System.out.println("Укажите имя");
            String name = scanner.nextLine();

            java.sql.Date date = validateUserInput();

            controller.setNewBirthday(name, date);
            System.out.println("ДР добавлен!");
        }

        public void deleteBirthday(){
            while (true) {
                List<BirthdayWithIndex> list = controller.getAllBirthdayList();

                if(list.isEmpty()) {
                    System.out.println("ДР не найдены!");
                    break;
                }

                printSeparator();
                System.out.println("Выберите ДР для удаления:");

                for (BirthdayWithIndex birthday : list) {
                    System.out.print(birthday.getIndex() + "." + " ");
                    System.out.print(birthday.getName() + " ");
                    System.out.println(birthday.getDate());
                }


                try{
                    int index = scanner.nextInt();
                    scanner.nextLine();
                    int index_checked = controller.getBirthdayId(index, list);
                    if (index_checked == -1) {
                        System.out.println("Указан неверный номер ДР!");
                    } else {
                        System.out.println("Вы уверены, что хотите удалить ДР?");
                        System.out.println("1. Удалить");
                        System.out.println("2. Отмена");
                        String cmd = scanner.nextLine();
                        switch (cmd){
                            case "1":
                                controller.deleteBirthday(index_checked);
                                System.out.println("ДР удалён!");
                                break;
                            case "2":
                                break;
                            default:
                                System.out.println("Некорректная команда!");
                                continue;
                        } break;

                    }
                } catch (InputMismatchException exception) {
                    System.out.println("Обнаружены некорректные символы!");
                    scanner.nextLine();
                }



            }
        }

        public void editBirthday() {
            while (true) {

                List<BirthdayWithIndex> list = controller.getAllBirthdayList();

                if(list.isEmpty()) {
                    System.out.println("ДР не найдены!");
                    break;
                }

                printSeparator();
                System.out.println("Выберите ДР для редактирования:");

                for (BirthdayWithIndex birthday : list) {
                    System.out.print(birthday.getIndex() + "." + " ");
                    System.out.print(birthday.getName() + " ");
                    System.out.println(birthday.getDate());
                }

                try{
                    int index = scanner.nextInt();
                    scanner.nextLine();
                    int index_checked = controller.getBirthdayId(index, list);
                    if (index_checked == -1) {
                        System.out.println("Указан неверный номер ДР!");
                    } else{
                        System.out.println("Укажите новое имя");
                        String name = scanner.nextLine();
                        java.sql.Date date = validateUserInput();

                        controller.editBirthday(index_checked, date, name);
                        System.out.println("ДР отредактировано!");
                        break;
                    }
                } catch (InputMismatchException exception){
                    System.out.println("Обнаружены некорректные символы!");
                    scanner.nextLine();
                }

            }
        }

        public java.sql.Date validateUserInput() {

            while (true) {
                System.out.println("Укажите дату рождения в формате DD.MM.YYYY");
                String date_input = scanner.nextLine();

                LocalDate current_date = LocalDate.now();
                LocalDate min_date = current_date.minusYears(120);


                try{
                    LocalDate date = LocalDate.parse(date_input, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    if (date.isAfter(LocalDate.now())){
                        System.out.println("Указана дата в будущем!");
                        continue;
                    } else if(date.isBefore(min_date)){
                        System.out.println("Указана слишком старая дата! Допустимый диапазон - 120 лет от текущей даты" );
                        continue;
                    }

                    return java.sql.Date.valueOf(date);
                } catch (DateTimeException exception){
                    System.out.println("Указана несуществующая дата или некорректный формат даты!");
                }
            }
        }

        public void printSeparator(){
            System.out.println("________________");
        }

        public void printIntro(){
            System.out.println("Поздравлятор 3000");
        }
    }

