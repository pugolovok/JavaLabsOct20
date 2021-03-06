// Выполнил Пуголовок А.С.

// Задание 28
// Длина слова - дискретная случайная величина X. На вход программе передается текст.
// Найти закон распределения X в форме таблицы, например:
// Вычислите мат. ожидание и дисперсию X.

// Примечание.
// Обрабатывается русский и английский текст. В методе main, до передачи текста в методы
// класса, удаляются все небуквенные символы (они заменяются пробелами), затем удаляются
// двойные пробелы в тексте и пробелы в начале текста (при их наличии).
// Блоком try-catch обработана ошибка, когда отсутствует нужный документ.
// Если нужный документ не содержит сомволов или содержит только цифры и знаки, но не слова,
// то об этом выводится соответствующее уведомление, и дальнейшие вычисления не производятся.


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

class LenNumbFreq { // класс, служащий типом для используемого ArrayList
    int length; // длина слова (количество букв)
    int countThisLen; // сколько раз встречалась эта длина
    float frequency; // вероятность данной длины (как относительная частота)
}

class mathOperations {

    private int countWords; // количество слов
    private String wordsArr[]; // массив слов
    private int lengthWordArr[]; // массив длин слов
    private float matExp; // мат. ожидание
    private int count;
    private float dispers;

    ArrayList<LenNumbFreq> arrList = new ArrayList<LenNumbFreq>();

    mathOperations(String argWords[]) { // конструктор (с аргументом)
        countWords = argWords.length;
        wordsArr = new String[countWords];
        for (int i = 0; i < countWords; i++) {
            wordsArr[i] = argWords[i];
        }
        lengthWordArr = new int[countWords];
        for (int i = 0; i < countWords; i++) {
            lengthWordArr[i] = wordsArr[i].length();
        }
        zakonRaspred();
        mathOzhidanie();
        dispersion();
    }

    public void zakonRaspred() { // метод для вычисления закона распределения
        //System.out.println("Закон распределения:");
        // отсортируем массив длин
        for (int i = 0; i < countWords - 1; i++) {
            for (int j = i + 1; j < countWords; j++) {
                if (lengthWordArr[i] > lengthWordArr[j]) {
                    int tmp = lengthWordArr[i];
                    lengthWordArr[i] = lengthWordArr[j];
                    lengthWordArr[j] = tmp;
                }
            }
        }

        // создаём список и добавляем в него первый элемент массива
        LenNumbFreq objStart = new LenNumbFreq();
        arrList.add(objStart);
        arrList.get(0).length = lengthWordArr[0];
        arrList.get(0).countThisLen = 1;

        count = 0;
        // далее перебираем все элементы отсортированного массива
        for (int i = 1; i < countWords; i++) {
            if (lengthWordArr[i] == lengthWordArr[i - 1]) // если длина как у предыдущего
            {   // увеличиваем счётчик для данной длины
                arrList.get(count).countThisLen = arrList.get(count).countThisLen + 1;
            } else // если нет, то создаём новый элемент списка
            {
                count++;
                LenNumbFreq obj = new LenNumbFreq();
                arrList.add(obj);
                arrList.get(count).length = lengthWordArr[i];
                arrList.get(count).countThisLen = 1;
            }
        }
        count++;

        System.out.println("Длина слова и количество слов такой длины:");
        for (int i = 0; i < count; i++) {
            System.out.println(arrList.get(i).length + " -> " + arrList.get(i).countThisLen);
        }
        System.out.println();
        for (int i = 0; i < count; i++) { // вычисляем относительную частоту появления каждой длины (вероятность)
            arrList.get(i).frequency = (float) arrList.get(i).countThisLen / countWords;
        }
        System.out.println("Закон распределения:");
        System.out.print("Xi   "); // вывод строки со значениями длин
        for (int i = 0; i < count; i++) {
            System.out.print(arrList.get(i).length + "      ");
        }
        System.out.println();
        System.out.print("Pi   "); // вывод вероятностей длин
        for (int i = 0; i < count; i++) {
            System.out.printf("%.2f", arrList.get(i).frequency);
            System.out.print("   ");
        }
    }

    public void mathOzhidanie() { // метод для вычисления математического ожидания
        System.out.println("\n" + "\n" + "Математическое ожидание длины слова:");

        matExp = 0.0f;
        for (int i = 0; i < count; i++) { // как сумма произведений длин и их вероятностей
            matExp += arrList.get(i).length * arrList.get(i).frequency;
        }
        System.out.printf("%.2f", matExp);
        System.out.println();
    }

    public void dispersion() { // метод для вычисления дисперсии D = SUM ((Xi - M(X))^2 * Pi)
        System.out.println("\n" + "Дисперсия:");
        dispers = 0.0f;
        for (int i = 0; i < count; i++) {
            dispers = (arrList.get(i).length - matExp) * (arrList.get(i).length - matExp) * arrList.get(i).frequency;
        }
        System.out.printf("%.2f", dispers);
        System.out.println();
    }
}

public class Task28 {

    public static void main(String[] args) throws FileNotFoundException {

        String text = "";
        try
        {
            Scanner in = new Scanner(new File("text.txt"));
            while (in.hasNext())
                text += in.nextLine() + "\n";
            in.close();
        }
        catch (Exception e)
        {
            System.out.println("Указанный документ не обнаружен.");
          //  e.printStackTrace();
            System.exit(0);
        }

        if (text.length() == 0) // если документ не содержит текста
        {
            System.out.println("Указанный документ не содержит символов.");
        }
        else // если документ не пустой, начинается обработка
        {
            System.out.println("Вывод заданного текста:");
            System.out.println(text);

            String textOnlyLetters = ""; // переменная для текста, в котором оставлены только буквы
            // далее с помощью регулярного выражения оставляются только буквы
            textOnlyLetters = text.replaceAll("[^a-z^A-Z^а-я^А-Я]", " ");

            String textClear = ""; // переменная для очищенного текста (без удаления пробелов перед текстом)

            // далее удаляются двойные пробелы
            for (int i = 0; i < textOnlyLetters.length() - 1; i++) {
                if (i < textOnlyLetters.length() - 2 && textOnlyLetters.charAt(i) == 32 && textOnlyLetters.charAt(i + 1) == 32) {
                    continue;
                } else {
                    textClear += textOnlyLetters.charAt(i);
                }
            }

            String textClearFinal = ""; // полностью очищенный текст

            for (int i = 0; i < textClear.length() - 1; i++) // удаление пробелов в начале текста
            {
                if (i == 0 && textClear.charAt(i) == 32) {
                    continue;
                } else {
                    textClearFinal += textClear.charAt(i);
                }
            }
                //System.out.println(textClearFinal.length());

            if (textClearFinal.length() == 0)
            {
                System.out.println("В документе отсутствуют слова.");
            }
            else
            {
                System.out.println("Вывод очищенного текста:");
                System.out.println(textClearFinal + "\n" + "\n");
                // очищенный текст превращается в массив типа String, элемент массива - слово
                String[] words = textClearFinal.split("\\s");
                mathOperations obj = new mathOperations(words); // массив слов передаётся в конструктор класса
            }
        }

    }

}
