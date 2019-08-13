package bali.collection;

import bali.*;
import bali.Integer;
import bali.annotation.Operator;

import static bali.number.Primitive.convert;
import static bali.logic.Primitive.convert;


public final class Chain<T> implements List<T> {

    private int size = 0;

    private Link<T> first = new Link<>();
    private Link<T> last = first;

    public Chain() {
    }

    public Chain(Group<T> in) {
        if (in != null){
            Iterator<T> iterator = in.iterator();
            while (convert(iterator.hasNext())){
                add(iterator.next());
            }
        }
    }

    public T get(Integer index) {
        Link<T> l = getLink(convert(index));
        return l != null ? l.item : null;
    }

    public Chain<T> replace(Integer index, T object) {
        Link<T> l = getLink(convert(index));
        if (l != null){
            getLink(convert(index)).item = object;
        }
        return this;
    }

    private Link<T> getLink(long i){
        if (i < 1 || i > size){
            return null;
        }
        Link<T> current = first;
        while (--i > 0){
            current = current.next;
        }
        return current;
    }

    public Integer size() {
        return convert(size);
    }

    @Operator("<<")
    public Chain<T> add(T object) {
        Link<T> newLink = new Link<>();
        last.item = object;
        last.next = newLink;
        last = newLink;
        size++;
        return this;
    }

    public Chain<T> remove(Integer index) {
        int i = convert(index);
        if (i == 1){
            first = first.next;
        } else if (i == size){
            getLink(i - 1).next = null;
        } else {
            Link before = getLink(i - 1);
            if (before != null){
                before.next = before.next.next;
            }
        }
        size--;
        return this;
    }

    public Chain<T> insert(Integer index, T item) {
        int i = convert(index);
        Link<T> newLink = new Link<>();
        newLink.item = item;
        if (i == 1){
            newLink.next = first;
            first = newLink;
        } else {
            Link<T> before = getLink(i - 1);
            newLink.next = before.next;
            before.next = newLink;
        }
        size++;
        return this;
    }


    public Iterator<T> iterator() {

        return new Iterator<T>() {

            private Link<T> current = first;

            public Logic hasNext() {
                return convert(current.next != null);
            }

            public T next() {
                T ret = current.item;
                current = current.next;
                return ret;
            }
        };
    }

    private static class Link<T> {
        public Link<T> next;
        public T item;
    }

    public Logic isEmpty() {
        return convert(size == 0);
    }

    @Operator("<<")
    public Chain<T> join(Group<T> operand) {
        Iterator<T> iterator = operand.iterator();
        while (convert(iterator.hasNext())){
            add(iterator.next());
        }
        return this;
    }

    public Chain<T> head(Integer index) {
        int i = convert(index);
        if (i < size){
            if (i < 1){
                clear();
            } else {
                last = getLink(i);
                size = i;
            }
        }
        return this;
    }

    public Chain<T> tail(Integer index) {
        int i = convert(index);
        if (i > 1){
            if (i > size){
                clear();
            } else {
                first = getLink(i);
                size = size - i + 1;
            }
        }
        return this;
    }

    private void clear() {
        last = first = new Link<>();
        size = 0;
    }
}