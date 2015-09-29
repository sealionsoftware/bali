package com.sealionsoftware;

import java.util.Iterator;

public class Collections {

    public static <I, J> Iterable<Each<I, J>> both(Iterable<I> i, Iterable<J> j) {
        return () -> new Iterator<Each<I, J>>() {
            private Iterator<I> ii = i.iterator();
            private Iterator<J> ji = j.iterator();
            public boolean hasNext() {
                return ii.hasNext() && ji.hasNext();
            }
            public Each<I, J> next() {
                return new Each<>(ii.next(), ji.next());
            }
        };
    }

    public static <I, J> Iterable<Each<I, J>> both(I[] i, J[] j) {
        return () -> new Iterator<Each<I, J>>() {
            private int n = 0;
            public boolean hasNext() {
                return n < i.length && n < j.length;
            }
            public Each<I, J> next() {
                return new Each<>(i[n], j[n++]);
            }
        };
    }

    public static class Each<I, J> {
        public final I i;
        public final J j;

        public Each(I i, J j) {
            this.i = i;
            this.j = j;
        }
    }
}
