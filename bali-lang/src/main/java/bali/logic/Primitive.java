package bali.logic;

import bali.Boolean;

public final class Primitive {

    public static Boolean convert(boolean in) {
        return in ? Boolean.TRUE : Boolean.FALSE;
    }

    public static boolean convert(Boolean in) {
        return in == Boolean.TRUE;
    }


}
