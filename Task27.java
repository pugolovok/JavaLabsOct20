// Выполнил Пуголовок А.С.

// Задание 27

// Запишите в двумерный массив NxN случайные числа от 10 до 99.
// Размерность N получить из аргументов.
// Напишите обобщенную функцию возвращающую одномерный массив из переданного двумерного.
// Напишите обобщенную функцию, находящую среднее значение элементов переданного массива.
// Протестируйте обе функции.

import java.util.Scanner;

class ToOneDimArr<T extends Number> {

    private int n;
    private int len;
    T oneDimArr[];

    ToOneDimArr(int n) { // конструктор класса с одним параметром
        this.n = n;
        len = n*n;
        oneDimArr = (T[]) new Number[len];
    }

    public T[] oneDimArr(T twoDimArr[][]) { // метод для преобразования двумерного массива в одномерный

        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                oneDimArr[count] = twoDimArr[i][j];
                count++;
            }
        }
        return oneDimArr;
    }

    public <T> void showArr(T oneDimArr[]) { // метод для выведения в консоль одномерного массива
        System.out.println();
        System.out.println("Новый одномерный массив:");
        for (int i = 0; i < oneDimArr.length; i++) {
            System.out.print(oneDimArr[i] + ", ");
        }
        System.out.println();
    }

    public <T extends Number> float averageValue(T twoDimArr[][]) { // метод для нахождения среднего значения

        float sum = 0.0f;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum += twoDimArr[i][j].floatValue(); // суммируемые значения приводятся к типу float
            }
        }
        float average =  sum / len;

        return average;
    }
}

public class Task27 {
    public static void main(String[] args) {

        int n = 0; // размерность массива. Далее считывается из консоли

        Scanner s = new Scanner(System.in);

        while (true) { // цикл для отфильтровки
            System.out.println("Введите целое число от 2 до 10:");
            if (s.hasNextInt()) {
                int tmp = s.nextInt();
                if (tmp > 1 && tmp < 11) {
                    n = tmp;
                    s.close();
                    break; // выход из цикла while, если было веедено подходящее число
                }
                else {
                    System.out.println("Число должно быть не менее 2 и не более 10");
                }
            }
            else {
                System.out.println("Буквы, знаки и дробные числа не подходят!");
                s.next();
            }
        }

        int min = 10;
        int max = 100;

        Integer twoDimArr[][] = new Integer[n][n];

        System.out.println("Начальный двумерный массив:");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twoDimArr[i][j] = rand(min, max);
                System.out.print(twoDimArr[i][j] + "  ");
            }
            System.out.println();
        }

        ToOneDimArr obj = new ToOneDimArr(n);

        obj.showArr(obj.oneDimArr(twoDimArr)); // вывод полученного одномерного массива
        System.out.println();
        System.out.println("Среднее значение = " + obj.averageValue(twoDimArr)); // расчёт и вывод среднего значения
    }

    public static int rand(int min, int max) { // метод для генерации случайных числе в заданном диапазоне значений
        return (int) (Math.random() * (max - min) + min);
    }
}
