package lista;

public class Nodo implements Cloneable{
	private Elemento elem;
	private Nodo siguiente;
	private Nodo anterior;

	public Nodo (Elemento elem){
		siguiente = anterior = null;
		this.elem = (Elemento)elem.clone();
	}
	public Elemento getElem () {return elem;}
	public Nodo getSiguiente () {return siguiente;}
	public Nodo getAnterior  () {return anterior;}
	public void setElem (Elemento elem) {this.elem = elem;}
	public void setSiguiente (Nodo siguiente) {this.siguiente = siguiente;}
	public void setAnterior(Nodo anterior) {this.anterior = anterior;}
	public Object clone(){
		short s = 0;
		Short d = new Short(s); 
		Elemento aux = new Elemento(d);
		Nodo copia = new Nodo(aux);
		try{
			copia = (Nodo)(super.clone());
		}
		catch(CloneNotSupportedException e){
			e.printStackTrace(System.err);
		}
		copia.elem = (Elemento)(this.elem.clone());
		return copia;
	}
}