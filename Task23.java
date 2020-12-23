// Выполнил Пуголовок А.С.

// Задание 23
// Напишите функцию boolean isPolindrom(String input), проверяющую, является
// ли строка полиндромом. Протестируйте.

import java.io.*;

public class Task23 {
    public static void main(String[] args) {

        String text = "";

        System.out.println("Enter text to verify for polindrom:");

        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            text = bufferedReader.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (isPolindrom(text))
        {
            System.out.println("String " + "\"" + text + "\"" + " is polindrom.");
        }
         else
        {
            System.out.println("String " + "\"" + text + "\"" + " is not polindrom.");
        }
    }

    static boolean isPolindrom(String text) {
        int len = text.length();
        boolean polindrom = true; // изначально предполагаем, что полиндром
        for (int i = 0; i < len/2; i++) // средний символ при нечётной длине не проверяем,
                                        // т.к. он всегда совпадает сам с собой
        {
            if (text.charAt(i) != text.charAt(len - 1 - i))
            {
                polindrom = false; // первое же несовпадание указывает, что не полиндром
                break;
            }
        }
        return polindrom;
    }
}
