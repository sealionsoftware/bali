package bali.number;

import bali.Integer;

public final class Primitive {

    public static int convert(Integer in) {
        if (in instanceof Int){
            return ((Int) in).value;
        }
        throw new NotImplementedException();
    }

    public static Integer convert(int in) {
        return new Int(in);
    }

    public static Integer parse(String in) {
        return convert(java.lang.Integer.parseInt(in));
    }

}
