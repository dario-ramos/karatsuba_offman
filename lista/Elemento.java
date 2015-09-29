package lista;

public class Elemento implements Cloneable{
	private short dato;

	public Elemento (Object dato){
		this.dato = ((Short)dato).shortValue();
	}
	public Object getDato (){		
		return new Short (dato);
	}
	public void setDato (Object dato){
		this.dato = ((Short)dato).shortValue();
	}
	public Object clone() {
		Elemento copia = new Elemento((Object)new Short(this.dato));
		try{
			copia = (Elemento)super.clone();
		}
		catch(CloneNotSupportedException e){
			e.printStackTrace(System.err);
		}
		return copia;
	}
}