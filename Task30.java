// Выполнил Пуголовок А.С.

// Задание 30
// Реализуйте класс робота, имеющего 2 ноги. Каждая нога осуществляет работу (шаг) в отдельном потоке.
// Необходимо выполнить одно условие: шаги выполняются поочередно разными ногами.
// При этом неважно с какой ноги начинает ходить робот.

// Примечание.
// Поочерёдность шагания обеспечивается с помощью конструкции monitor-wait-notify.
// Количество пар шагов определяется числом, вводимым в консоли, при этом
// отфильтровывается любой ввод кроме целого положительного числа.
// Какая нога начинает ходить первой, определяется чётностью/нечётностью случайного числа в методе main.
// Классы для "одной" и "другой" ноги отличаются только строкой вывода в консоль
// Когда одна нога шагает, другая ждёт. Нога после своего шагания сама переходит в режим ожидания,
// уступая право для шага другой ноге.

import java.util.Scanner;

class Robot { //содержит данные, которые непосредственно не связаны с синхронизацией

    static int coupleOfSteps; //параметр, указывающий количество повторов вывода
    static Object monitor = new Object(); // monitor - объект, используемый как взаимное исключение,
                                          // используемый для синхронизации

    Robot(int coupleOfSteps) { //конструктор класса (с параметром)
        this.coupleOfSteps = coupleOfSteps;
    }
}

class SomeLeg extends Thread { //класс для одной ноги (наследуется от класса Thread)
    public void run() { //входная точка потока одной ноги
        for (int i = 0; i < Robot.coupleOfSteps; i++) {
            synchronized (Robot.monitor) { // синхронизации по объекту monitor
                Robot.monitor.notify(); // метод notify() возобновляет работу потока
                System.out.println("Step by some leg"); //вывод данных
                try {
                    if (i < Robot.coupleOfSteps - 1) { // данное условие для того, чтобы на последнем шаге цикла
                    // не вызывался метод wait(), что не позволит программе завершить работу.
                        Robot.monitor.wait();  // метод wait() переводит поток в режим ожидания и отдаёт монитор
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

class AnotherLeg extends Thread { // класс для другой ноги
    public void run() {
        for (int i = 0; i < Robot.coupleOfSteps; i++) {
            synchronized (Robot.monitor) {
                Robot.monitor.notify();
                System.out.println("Step by another leg");
                try {
                    if (i < Robot.coupleOfSteps - 1) {
                        Robot.monitor.wait();
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

public class Task30 {
    public static void main(String args[]) {

        int coupleOfSteps = 0; // количество пар шагов
        Scanner s = new Scanner(System.in);

        while (true) { // цикл для отфильтровки всего кроме целого положительного числа
            System.out.println("Введите целое положительное число:");
            if (s.hasNextInt()) {
                int tmp = s.nextInt();
                if (tmp > 0) {
                    coupleOfSteps = tmp;
                    s.close(); // остановка процесса считывания
                    break; // выход из цикла while, если было веедено целое положительное число
                }
                else {
                    System.out.println("Нужно положительное, а Вы ввели отрицательное!");
                }
            }
            else {
                System.out.println("Буквы, знаки и дробные числа не подходят!");
                s.next();
            }
        }

        // про использование StringBuilder почитал и осмыслил.
        // поскольку тут единственный вывод (без цикла), конкатенация представляется уместной
        System.out.println("Заданное количество пар шагов: " + coupleOfSteps + "\n");

        Robot robot = new Robot(coupleOfSteps); //создание объекта класса помощника

        SomeLeg thread1 = new SomeLeg();
        AnotherLeg thread2 = new AnotherLeg();

        int number = (int) (Math.random() * 10); // задаётся целое случайное число

        if (number % 2 == 0) { // если оно чётное
            thread1.start(); // сначала запуск потока одной ноги
            thread2.start(); // затем запуск потока другой ноги
        } else { // в противном случае
            thread2.start(); // сначала запуск потока другой ноги
            thread1.start(); // затем запуск потока другой ноги
        }
    }
}