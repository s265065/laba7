package lab.server;

import lab.Hat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public class Wardrobe extends ArrayList<Hat> implements Comparator<Hat> {

    private static int maxCollectionElements = 256;
    private Date createdDate = new Date();

    static void setMaxCollectionElements(int maxCollectionElements) {
        Wardrobe.maxCollectionElements = maxCollectionElements;
    }

    static int getMaxCollectionElements() {
        return maxCollectionElements;
    }

    /**
     * Сравнивает две шляпы по размеру
     *
     * @param a Шляпа которую нужно сравнить
     * @param b Шляпа с которой нужно сравнить
     * @return положительное число, если первая шляпа больше; отрицательное если ниже; ноль, если шляпы равны
     */
    public int compare(Hat a, Hat b) {
        return (a.size - b.size);
    }


    /**
     * Добавляет шляпу в гардероб
     *
     * @param a Шляпа, которую нужно добавить
     * @return true, если шляпа успешно добавлена
     */
    synchronized public boolean add(Hat a) {
        if (this.size() >= maxCollectionElements)
            throw new WardrobeOverflowException();
        if (!(a.color.equals(""))) {
            super.add(new Hat(a.size, a.color, a.num, a.content));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Выводит весь гардероб.
     * Цвет и размер каждой из шляп и их содержимое
     */
    String show() {
        StringBuilder result = new StringBuilder();
        for (Hat hat : this) result.append(hat.showHat());
        if (this.size() == 0) {
            result.append("гардероб пуст");
        }
        return result.toString();
    }

    /**
     * Выводит информацию о гардеробе
     */
    String info() {
        return ("Гардероб - коллекция типа ArrayList содержит " + this.size() + " шляп \n" +
                "создан " + createdDate);
    }


    /**
     * Удаляет шляпу из гардероба
     *
     * @param a Шляпа, которую нужно удалить
     */
    synchronized boolean remove(Hat a) {
        boolean result = false;
        for (int index = 0; index < this.size(); index++) {
            if (((a.size) == (this.get(index).size)) && /* vs & */ ((a.num) == (this.get(index).num)) & ((a.color).equals(this.get(index).color))) {
                super.remove(index);
                result = true;
            }
        }
        return result;
    }

    /**
     * Добавляет шляпу в коллекцию, если она меньше каждой из уже имеющихся
     *
     * @param a Шляпа, которую нужно добавить
     */
    synchronized boolean addIfMin(Hat a) {
        boolean result = false;
        Stream<Hat> stream = stream();
        Optional<Hat> min = stream.min(Comparator.comparingInt(Hat::getSize));
        Hat minHat = min.get();
        if (compare(a, minHat) < 0) {
            add(a);
            result = true;
        }
        return result;
    }

}
