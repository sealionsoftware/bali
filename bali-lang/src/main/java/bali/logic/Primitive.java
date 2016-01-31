package bali.logic;

import bali.Logic;

public final class Primitive {

    public static Logic convert(boolean in) {
        return in ? Logic.TRUE : Logic.FALSE;
    }

    public static boolean convert(Logic in) {
        return in == Logic.TRUE;
    }


}
