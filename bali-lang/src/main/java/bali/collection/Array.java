package bali.collection;

import bali.Group;
import bali.Integer;
import bali.Iterator;
import bali.Logic;

import java.util.Arrays;

import static bali.logic.Primitive.convert;
import static bali.number.Primitive.convert;

public class Array<T> implements Group<T> {

    private T[] items;

    @SafeVarargs
    public Array(T... items) {
        this.items = items;
    }

    public Integer size() {
        return convert(items.length);
    }

    public Logic isEmpty() {
        return convert(items.length == 0);
    }

    public T get(Integer index) {
        int i = convert(index);
        return i > 0 && i <= items.length ? items[i - 1] : null;
    }

    public Array<T> head(Integer number) {

        int numberOfItems = items.length;
        int take = convert(number);
        if (take > numberOfItems){
            take = numberOfItems;
        }

        return new Array<T>(Arrays.copyOf(items, take));
    }

    public Array<T> tail(Integer number) {

        int numberOfItems = items.length;
        int take = convert(number);
        if (take > numberOfItems){
            take = numberOfItems;
        }

        return new Array<T>(Arrays.copyOfRange(items, numberOfItems - take, numberOfItems));
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private int i = 0;

            public Logic hasNext() {
                return convert(i < items.length);
            }

            public T next() {
                return i < items.length ? items[i++] : null;
            }
        };
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Array<?> array = (Array<?>) o;

        return Arrays.equals(items, array.items);

    }

    public String toString() {
        return Arrays.toString(items);
    }
}

