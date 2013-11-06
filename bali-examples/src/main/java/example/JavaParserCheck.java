package example;

/**
 * User: Richard
 * Date: 05/11/13
 */
public class JavaParserCheck {

	public static void main(String[] args){

		D d = new D();
		E e = new E();
		e.d.getE();

		d = d.getE().d; // Struggling with this in Bali

	}

}
