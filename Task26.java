// Выполнил Пуголовок А.С.

// Задание 26
// Напишите функцию, создающую аббревиатуры. Например, на входе строка: "Санкт- Петербургский
// Государственный Технический Институт им. Патриса Лумумбы". На выходе строка: СПБГТИПЛ.

import java.io.*;

public class Task26 {

    public static void main(String args[]) {

        String text = "";

        System.out.println("Введите строку:");

        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            text = bufferedReader.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println("Заданная строка: " + "\n" + text);

        String textOnlyLetters = text.replaceAll("[^a-z^A-Z^а-я^А-Я]", " "); // ненужные знаки заменятся пробелами

        char chArr[] = new char[textOnlyLetters.length() - 1]; // массив символов

        String textClear = ""; // промежуточная строка для текста после удаления знаков пунктуации и цифр

        for (int i = 0; i < textOnlyLetters.length() - 1; i++) // наполнение массива символов
        {
            chArr[i] = textOnlyLetters.charAt(i);
        }

        for (int i = 0; i < textOnlyLetters.length() - 1; i++) // удаление двойных пробелов
        {
            if (i < textOnlyLetters.length() - 2 && chArr[i] == 32 && chArr[i + 1] == 32)
            {
                continue;
            }
            else
            {
                textClear += chArr[i];
            }
        }

        //System.out.println(textClear);

        String[] words = textClear.split("\\s"); // разбиение текста на массив элементов типа String

        System.out.println("\n" + "Аббревиатура: " + abbreviation(words)); // передаём в функцию массив слов
    }

    static public String abbreviation(String[] words)
    {
        String abbrLowReg = "";
        String abbrHighReg = "";

        for (int i = 0; i < words.length; i++) // собираем в String первые буквы всех слов
        {
            abbrLowReg += words[i].charAt(0);
        }

        abbrHighReg = abbrLowReg.toUpperCase(); // все собранные первые буквы переводятся в верхний регистр

        return abbrHighReg;
    }
}
