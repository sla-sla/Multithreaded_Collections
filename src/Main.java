import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    static BlockingQueue<String> forA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> forB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> forC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {

        int quantityTexts = 10_000;
        //Заполняем очереди
        new Thread(() -> {
            for (int i = 0; i < quantityTexts; i++) {
                try {
                    String someText = generateText("abc", 10_000);
                    forA.put(someText);
                    forB.put(someText);
                    forC.put(someText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            try {
                hasStringMaxChar(forA, 'a', quantityTexts);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                hasStringMaxChar(forB, 'b', quantityTexts);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                hasStringMaxChar(forC, 'c', quantityTexts);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    //метод, который будет находить строку с максимальным количеством нужного символа из очереди
    public static void hasStringMaxChar(BlockingQueue<String> queue, char character, int quantityTexts) throws InterruptedException {
        String maxText = "";
        int maxCount = 0;
        for (int i = 0; i < quantityTexts; i++) { //Проходим по всем строкам
            String fromQueue = queue.take();   //Берем строку из очереди
            int count = 0;
            for (int j = 0; j < fromQueue.length(); j++) {  //Проходимся по строке и считаем нужные символы
                if (fromQueue.charAt(j) == character) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxText = fromQueue;
            }
        }
        System.out.println("Max " + character + " in: " + maxText);

    }

    //генерирует текст
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}



