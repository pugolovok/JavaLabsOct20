// Выполнил Пуголовок А.С.

// Задание 36 "Ассоциации".

//Словесная игра - необходимо угадать слово по набору ассоциаций
// (слова или фразы из ассоциативного словаря - английский или русский).
// Статья ассоциативного словаря состоит из СЛОВА - <ассоциаиция1>X, <ассоциация2>Y...,
// где X и Y - частотность ассоциации (или её вес). Разработать правила игры,
// предусмотреть несколько уровней сложности.
// Персистенция результатов игроков в файл + 10 баллов.


import java.sql.*;
import java.io.*;

class Connector{ // класс для подключения к БД, классы - уровни игры являются его наследниками
    static protected Connection connection = null;
    static protected Statement statement;

    public Connector(){
        String user = "sashadb";
        String password = "12345";

        // подключение к БД также возможно при вводе имени пользователя и пароля в консоли:
/*
        System.out.println("enter USER name");
        InputStream inputStreamUs = System.in;
        Reader inputStreamReaderUs = new InputStreamReader(inputStreamUs);
        BufferedReader bufferedReaderUs = new BufferedReader(inputStreamReaderUs);

        String user = "";
        try {
            user = bufferedReaderUs.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println("enter password");
        InputStream inputStreamPa = System.in;
        Reader inputStreamReaderPa = new InputStreamReader(inputStreamPa);
        BufferedReader bufferedReaderPa = new BufferedReader(inputStreamReaderPa);

        String password = "";
        try {
            password = bufferedReaderPa.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
*/
        try { // попытка установить соединение
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/association", user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            System.exit(0);
        }
        System.out.println("You are connected to \"Association game\"!" + "\n");
    }
}

class StartMenu{ // класс главного меню программы

    private String gameType = "";

    public StartMenu(){ // конструктор
        showGameLevels();
        makeChoose();
        readChoose();
    }

    private void showGameLevels(){
        System.out.println("Choose type of game by entering corresponding number:");
        System.out.println("2 - game with two association words");
        System.out.println("3 - game with three association words");
        System.out.println("4 - game with four association words");
        System.out.println("5 - game with two start association words and three additional words");
        System.out.println("0 - for switch off game");
        System.out.println();
    }

    private void makeChoose(){ // метод для ввода выбранного варианта
        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            gameType = bufferedReader.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void readChoose(){ // метод для считывания сделанного выбора
        if (gameType.contentEquals("2")){
            new LevelTwo();
        }

        if (gameType.contentEquals("3")){
            new LevelThree();
        }

        if (gameType.contentEquals("4")){
            new LevelFour();
        }

        if (gameType.contentEquals("5")){
            new LevelFive();
        }

        if (gameType.contentEquals("0")){
            System.out.println("Goodbye!");
            System.exit(0);
        }
    }
}

class LevelTwo extends Connector { // класс игры с двумя словами-ассоциациями
    private String trueAnswer = "";
    private int penaltyPoints = 0;

    public LevelTwo() {
        intro();
        play();
    }

    private void intro() {
        System.out.println("You chose game with two association words!");
        System.out.println("You can make 4 mistakes. If you will make 5 mistakes game will break off." + "\n");
        System.out.println("Lets start!" + "\n");
    }

    private void play() {

        for (int i = 1; i < 6; i++) {

            System.out.println("Association words are:");

            try {
                ResultSet resultset = statement.executeQuery("select one, two from twoWords where id = " + i);
                while (resultset.next()) {
                    System.out.println(resultset.getString(1) + "," + "\t" + resultset.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet resultset = statement.executeQuery("select answer from twoWords where id = " + i);
                while (resultset.next()) {
                    trueAnswer = resultset.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("\n" + "Enter your answer:");

            boolean incorrectAnswer = true;

            while (incorrectAnswer) {

                InputStream inputStreamAnswer = System.in;
                Reader inputStreamReaderAnswer = new InputStreamReader(inputStreamAnswer);
                BufferedReader bufferedReaderQuery = new BufferedReader(inputStreamReaderAnswer);

                String answer = "";
                try {
                    answer = bufferedReaderQuery.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (answer.contentEquals(trueAnswer)) {
                    if (i == 5) {
                        System.out.println("\n" + "You win! Take our congratulations!" + "\n" + "\n");
                        break;
                    } else {
                        System.out.println("It is correct answer! Go ahead!" + "\n");
                        incorrectAnswer = false;
                    }
                } else {
                    penaltyPoints++;
                    if (penaltyPoints == 5) {
                        System.out.println("Your answer is wrong. You have " + penaltyPoints + " penalty points" + "\n" + "You lost.");
                        break; // для выхода из цикла while
                    }
                    System.out.println("Your answer is wrong. You have " + penaltyPoints + " penalty points" + "\n" + "Try again! Enter your answer:");
                }
            }

            if (penaltyPoints == 5)
            {
                System.out.println("\n" + "\n");
                new StartMenu(); // для возврата в главное меню в случае проигрыша в игре
            }
        }
        new StartMenu(); // для возврата в главное меню в случае победы в игре
    }
}

class LevelThree extends Connector{

    private String trueAnswer = "";
    private int penaltyPoints = 0;

    public LevelThree(){
        intro();
        play();
    }

    private void intro() {
        System.out.println("You chose game with three association words!");
        System.out.println("You can make 3 mistakes. If you will make 4 mistakes game will break off." + "\n");
        System.out.println("Lets start!" + "\n");
    }

    private void play() {
        for (int i = 1; i < 6; i++) {

            System.out.println("Association words are:");

            try {
                ResultSet resultset = statement.executeQuery("select one, two, three from threeWords where id = " + i);
                while (resultset.next()) {
                    System.out.println(resultset.getString(1) + "," + "\t" + resultset.getString(2) + "," + "\t" + resultset.getString(3));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet resultset = statement.executeQuery("select answer from threeWords where id = " + i); // id = i
                while (resultset.next()) {
                    trueAnswer = resultset.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("\n" + "Enter your answer:");

            boolean incorrectAnswer = true;

            while(incorrectAnswer) {

                InputStream inputStreamAnswer = System.in;
                Reader inputStreamReaderAnswer = new InputStreamReader(inputStreamAnswer);
                BufferedReader bufferedReaderQuery = new BufferedReader(inputStreamReaderAnswer);

                String answer = "";
                try {
                    answer = bufferedReaderQuery.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (answer.contentEquals(trueAnswer)) {
                    if (i == 5) {
                        System.out.println("\n" + "You win! Take our congratulations!" + "\n" + "\n");
                        break;
                    }
                    else {
                        System.out.println("It is correct answer! Go ahead!" + "\n");
                        incorrectAnswer = false;
                    }
                }
                else {
                    penaltyPoints++;
                    if (penaltyPoints == 4)
                    {
                        System.out.println("Your answer is wrong. You have " + penaltyPoints + " penalty points" + "\n" + "You lost.");
                        break;
                    }
                    System.out.println("Your answer is wrong. You have " + penaltyPoints + " penalty points" + "\n" + "Try again! Enter your answer:");
                }
            }
            if (penaltyPoints == 4)
            {
                System.out.println("\n" + "\n");
                new StartMenu();
            }
        }
        new StartMenu();
    }
}

class LevelFour extends Connector{

    private String trueAnswer = "";
    private int penaltyPoints = 0;

    public LevelFour(){
        intro();
        play();
    }

    private void intro() {
        System.out.println("You chose game with four association words!");
        System.out.println("You can make 2 mistakes. If you will make 3 mistakes game will break off." + "\n");
        System.out.println("Lets start!" + "\n");
    }

    private void play() {

        for (int i = 1; i < 6; i++) {

            System.out.println("Association words are:");

            try {
                ResultSet resultset = statement.executeQuery("select one, two, three, four from fourWords where id = " + i);
                while (resultset.next()) {
                    System.out.println(resultset.getString(1) + "," + "\t" + resultset.getString(2) + "," + "\t" + resultset.getString(3) + "," + "\t" + resultset.getString(4));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet resultset = statement.executeQuery("select answer from fourWords where id = " + i); // id = i
                while (resultset.next()) {
                    trueAnswer = resultset.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("\n" + "Enter your answer:");

            boolean incorrectAnswer = true;

            while(incorrectAnswer) {

                InputStream inputStreamAnswer = System.in;
                Reader inputStreamReaderAnswer = new InputStreamReader(inputStreamAnswer);
                BufferedReader bufferedReaderQuery = new BufferedReader(inputStreamReaderAnswer);

                String answer = "";
                try {
                    answer = bufferedReaderQuery.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (answer.contentEquals(trueAnswer)) {
                    if (i == 5) {
                        System.out.println("\n" + "You win! Take our congratulations!" + "\n" + "\n");
                        break;
                    }
                    else {
                        System.out.println("It is correct answer! Go ahead!" + "\n");
                        incorrectAnswer = false;
                    }
                }
                else {
                    penaltyPoints++;
                    if (penaltyPoints == 3)
                    {
                        System.out.println("Your answer is wrong. You have " + penaltyPoints + " penalty points" + "\n" + "You lost.");
                        break;
                    }
                    System.out.println("Your answer is wrong. You have " + penaltyPoints + " penalty points" + "\n" + "Try again! Enter your answer:");
                }
            }

            if (penaltyPoints == 3)
            {
                System.out.println("\n" + "\n");
                new StartMenu();
            }
        }
        new StartMenu();
    }
}

class LevelFive extends Connector{

    private String trueAnswer = "";
    private int penaltyPoints = 0;
    private int yourScore = 0;

    public LevelFive(){
        intro();
        play();
    }

    private void intro() {
        System.out.println("You chose game with two start association words and three additional words!");
        System.out.println("You should finish game with max quantity of points.");
        System.out.println("If you make correct answer without additional association words you get 4 points.");
        System.out.println("Each additional word decrease you got points for this answer by 1 point.");
        System.out.println("If you want take additional word enter \"+word\".");
        System.out.println("You can make 3 mistakes. If you will make 4 mistakes game will break off." + "\n");
        System.out.println("Lets start!" + "\n");
    }

    private void play() {

        for (int i = 1; i < 6; i++) {

            int successPoints = 4;
            String [] addWord = {"three", "four", "five"};
            int addWordCounter = 0;

            System.out.println("Association words are:");

            try {
                ResultSet resultset = statement.executeQuery("select one, two from twoPlus where id = " + i);
                while (resultset.next()) {
                    System.out.println(resultset.getString(1) + "," + "\t" + resultset.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet resultset = statement.executeQuery("select answer from twoPlus where id = " + i);
                while (resultset.next()) {
                    trueAnswer = resultset.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("\n" + "Enter your answer:");

            boolean incorrectAnswer = true;

            while(incorrectAnswer) {

                InputStream inputStreamAnswer = System.in;
                Reader inputStreamReaderAnswer = new InputStreamReader(inputStreamAnswer);
                BufferedReader bufferedReaderQuery = new BufferedReader(inputStreamReaderAnswer);

                String answer = "";
                try {
                    answer = bufferedReaderQuery.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                if (answer.contentEquals("+word") && addWordCounter < 3) {
                    successPoints--;
                    if (addWordCounter == 2) {
                        System.out.print("BE CAREFUL! YOU HAVE USED ALL AVAILABLE ADDITIONAL WORDS." + "\n");
                    }
                    String additionalWord = addWord[addWordCounter];
                    addWordCounter++;
                    try {
                        ResultSet resultset = statement.executeQuery("select " + additionalWord + " from twoPlus where id = " + i);
                        System.out.print("Additional word is:  ");
                        while (resultset.next()) {
                            System.out.println(resultset.getString(1));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\n" + "Read additional word and enter your answer:");

                }
                else {

                    if (answer.contentEquals(trueAnswer)) {
                        if (i == 5) {
                            yourScore += successPoints;
                            System.out.println("\n" + "You win! Take our congratulations!");
                            System.out.println("You got " + yourScore + " points of success in game!" + "\n" + "\n");
                            break;
                        } else {
                            yourScore += successPoints;
                            System.out.println("It is correct answer! You have got " + successPoints + " points of success.");
                            System.out.println("Go ahead!" + "\n");
                            incorrectAnswer = false;
                        }
                    } else {
                        penaltyPoints++;
                        if (penaltyPoints == 4) {
                            System.out.println("Your answer is wrong. You have " + penaltyPoints + " penalty points" + "\n" + "You lost.");
                            break;
                        }
                        System.out.println("Your answer is wrong. You have " + penaltyPoints + " penalty points" + "\n" + "Try again! Enter your answer:");
                    }
                }
            }

            if (penaltyPoints == 4)
            {
                System.out.println("\n" + "\n");
                new StartMenu();
            }
        }
        new StartMenu();
    }
}

public class Task36 {

    public static void main(String args[]) {

        new StartMenu(); // вызов гласса главного меню программы
    }
}
