package aed.polinomios;

import java.util.Arrays;
import java.util.function.BiFunction;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;

import java.lang.Math;
import java.text.DecimalFormat;

/**
 * Operaciones sobre polinomios de una variable con coeficientes enteros.
 */
public class Polinomio {

	// Una lista de monomios
	PositionList<Monomio> terms;

	/**
	 * Crea el polinomio "0".
	 */
	public Polinomio() {
		terms = new NodePositionList<>();
	}

	/**
	 * Crea un polinomio definado por una lista con monomios.
	 * @param terms una lista de monomios
	 */
	public Polinomio(PositionList<Monomio> terms) {
		this.terms = terms;
	}

	/**
	 * Crea un polinomio definado por un String.
	 * La representación del polinomio es una secuencia de monomios separados
	 * por '+' (y posiblemente con caracteres blancos).
	 * Un monomio esta compuesto por tres partes,
	 * el coefficiente (un entero), el caracter 'x' (el variable), y el exponente
	 * compuesto por un un caracter '^' seguido por un entero.
	 * Se puede omitir multiples partes de un monomio, 
	 * ejemplos:
	 * <pre>
	 * {@code
	 * new Polinomio("2x^3 + 9");
	 * new Polinomio("2x^3 + -9");
	 * new Polinomio("x");   // == 1x^1
	 * new Polinomio("5");   // == 5x^0
	 * new Polinomio("8x");  // == 8x^1
	 * new Polinomio("0");   // == 0x^0
	 * }
	 * </pre>
	 * @throws IllegalArgumentException si el argumento es malformado
	 * @param polinomio - una secuencia de monomios separados por '+'
	 */
	public Polinomio(String polinomio) {
		throw new RuntimeException("no esta implementado todavia");
	}

	/**
	 * Suma dos polinomios.
	 * @param p1 primer polinomio.
	 * @param p2 segundo polinomio.
	 * @return la suma de los polinomios.
	 */


	public static Polinomio suma(Polinomio p1, Polinomio p2) { 

		if(p1.terms.isEmpty()) return p2;
		if(p2.terms.isEmpty()) return p1;
		Position<Monomio> posicion1 = p1.terms.first();  
		Position<Monomio> posicion2 = p2.terms.first(); 

		Polinomio res = new Polinomio();


		while(!(posicion1 == null && posicion2 == null)) {



			if((posicion1 != null && posicion2 != null) && posicion1.element().getExponente()== posicion2.element().getExponente()) { //hacer suma
				//comprobar que la suma de coef no sea 0 para no meter un elemento nulo
				int pos = posicion1.element().getCoeficiente() + posicion2.element().getCoeficiente();
				if(pos!= 0) {
					res.terms.addLast(new Monomio(pos, posicion1.element().getExponente() ));
				}

				posicion1 = p1.terms.next(posicion1);

				posicion2 = p2.terms.next(posicion2);


			}else if(((posicion1 != null) &&(posicion2 == null)) 
					|| ((posicion1 != null) && posicion1.element().getExponente() > posicion2.element().getExponente())
					) {



				res.terms.addLast(new Monomio(posicion1.element().getCoeficiente(),posicion1.element().getExponente() ));

				posicion1 = p1.terms.next(posicion1);


			}else {
				res.terms.addLast(new Monomio(posicion2.element().getCoeficiente(),posicion2.element().getExponente() ));

				posicion2 = p2.terms.next(posicion2);


			}

		}

		return res; 
	}

	/**
	 * Substraccion de dos polinomios.
	 * @param p1 primer polinomio.
	 * @param p2 segundo polinomio.
	 * @return la resta de los polinomios.
	 */
	public static Polinomio resta(Polinomio p1, Polinomio p2) {
		
		
		Polinomio p2negativo = multiplica(new Monomio(-1,0),p2);
		
		return suma(p1, p2negativo);
	}

	/**
	 * Calcula el producto de dos polinomios.
	 * @param p1 primer polinomio.
	 * @param p2 segundo polinomio.
	 * @return el producto de los polinomios.
	 */
	public static Polinomio multiplica(Polinomio p1, Polinomio p2) {



		Polinomio res = new Polinomio();

		if(p1.terms.isEmpty() || p2.terms.isEmpty()) {
			return res;
		}

		Position<Monomio> posicion = p1.terms.first();  

		while(!(p1.terms.last().equals(posicion))) {

			res = suma(res, multiplica(posicion.element(), p2));
			posicion = p1.terms.next(posicion);

		}

		res = suma(res, multiplica(posicion.element(), p2));


		return res; 
	}

	/**
	 * Calcula el producto de un monomio y un polinomio.
	 * @param m el monomio
	 * @param p el polinomio
	 * @return el producto del monomio y el polinomio
	 */
	public static Polinomio multiplica(Monomio m, Polinomio p) {



		Polinomio res = new Polinomio();

		if(p.terms.isEmpty() ) { //TODO quiz ver si el monomio es vacio
			return res;
		}
		Position<Monomio> posicion = p.terms.first();  

		while(!p.terms.last().equals(posicion)) {

			res.terms.addLast(new Monomio(posicion.element().getCoeficiente()* m.getCoeficiente()
					,posicion.element().getExponente() + m.getExponente()));
			posicion = p.terms.next(posicion);

		}

		res.terms.addLast(new Monomio(posicion.element().getCoeficiente()* m.getCoeficiente()
				,posicion.element().getExponente() + m.getExponente()));

		return res; 
	}

	/**
	 * Devuelve el valor del polinomio cuando su variable es sustiuida por un valor concreto.
	 * Si el polinomio es vacio (la representacion del polinomio "0") entonces
	 * el valor devuelto debe ser -1.
	 * @param valor el valor asignado a la variable del polinomio
	 * @return el valor del polinomio para ese valor de la variable.
	 */
	public long evaluar(int valor) {
		if(terms.isEmpty()) return -1; 
		//if(terms.first().element().getCoeficiente() == 0) return 0;
		int resultado = 0;
		Position<Monomio> actual = terms.first();
		while (!actual.equals(terms.last())){

			resultado += (Math.pow(valor, actual.element().getExponente())* actual.element().getCoeficiente());

			actual = terms.next(actual);
		}

		resultado += (Math.pow(valor, terms.last().element().getExponente())*  terms.last().element().getCoeficiente());

		return resultado;
	}

	/**
	 * Devuelve el exponente (grado) del monomio con el mayor grado
	 * dentro del polinomio
	 * @return el grado del polinomio
	 */
	public int grado() {
		if(terms.isEmpty()) return -1;
		return terms.first().element().getExponente();
	}
	
	
	@Override
	public boolean equals(Object o) {
	    // self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;

	    return  resta(this, (Polinomio) o).grado() ==-1;
	}
	// https://www.sitepoint.com/implement-javas-equals-method-correctly/
	
	
	@Override
	public String toString() {
		if (terms.isEmpty()) return "0";
		else {
			StringBuffer buf = new StringBuffer();
			Position<Monomio> cursor = terms.first();
			while (cursor != null) {
				Monomio p = cursor.element();
				int coef = p.getCoeficiente();
				int exp = p.getExponente();
				buf.append(new Integer(coef).toString());
				if (exp > 0) {
					buf.append("x");
					buf.append("^");
					buf.append(new Integer(exp)).toString();
				}
				cursor = terms.next(cursor);
				if (cursor != null) buf.append(" + ");
			}
			return buf.toString();
		}
	}
	
}