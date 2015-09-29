package lista;

public class Lista implements Cloneable{
	public static final short PRI_L = -2;
	public static final short ANT_L = -1;
	public static final short SIG_L =  1;
	public static final short ULT_L =  2;

	private Nodo primero;
	private Nodo corriente;
	private Nodo ultimo;

	public Lista (){
		primero = corriente = ultimo = null;
	}
	public boolean insertar (Nodo n, short mov) {
		if (primero == null) primero = ultimo = n;
		else{
			switch(mov) {
	 			case PRI_L: n.setSiguiente(primero);
							 n.setAnterior(null);
							 primero.setAnterior(n);
							 primero = n;
							 break;
			 	case ULT_L: n.setSiguiente(null);
							 n.setAnterior(ultimo);
							 ultimo.setSiguiente (n);
							 ultimo = n;
							 break;
			  	case ANT_L: n.setSiguiente(corriente);
							 n.setAnterior(corriente.getAnterior());
							 if (corriente != primero)
								 (corriente.getAnterior()).setSiguiente(n);
							 else
							 	 primero = n;
							 corriente.setAnterior (n);
							 break;
			  	case SIG_L:	n.setSiguiente (corriente.getSiguiente());
							n.setAnterior(corriente);
							if (corriente != ultimo)
								(corriente.getSiguiente()).setAnterior (n);
							else
								ultimo = n;
							corriente.setSiguiente (n);
							break;
			 	default:  return false;
			}
		}
		corriente = n;
		return true;
	 }

	public boolean vacia(){
	   return (primero == null);
	}

	public boolean moverCte (short mov) {
	   switch(mov) {
	     case PRI_L: corriente = primero;
		       		return false;
	     case ULT_L: corriente = ultimo;
		       		return false;
	     case ANT_L: if (corriente == primero) return true;
		       		else corriente = corriente.getAnterior();
		       		return false;
	     case SIG_L: if (corriente == ultimo) return true;
		       		else corriente = corriente.getSiguiente();
		       		return false;
	     default: return true;
	   }
	}

	public void vaciar() {
	   primero = ultimo = corriente = null;
	}
	//PRE: Lista no vacia.
	public Elemento getCorriente(){
		return corriente.getElem();
	}

	public void setCorriente (Elemento elem){
	   elem.setDato (elem.getDato());
	}
	//PRE: Lista no vacia.
	public void elimCte () {
	   Nodo aux = corriente;
	   if ((corriente == primero) && (corriente == ultimo))
	      primero = ultimo = null;
	   else{
	      if (corriente == primero){
				primero = corriente.getSiguiente();
				corriente = primero;
				corriente.setAnterior(null);
	      }else if (corriente == ultimo){
			   ultimo = corriente.getAnterior();
			   corriente = ultimo;
			   corriente.setSiguiente(null);			   
	      }else{
		      (aux.getAnterior()).setSiguiente (aux.getSiguiente());
		      (aux.getSiguiente()).setAnterior (aux.getAnterior());
		      corriente = corriente.getSiguiente();
	      }
	   }
	   aux = null;
	}
	public Object clone() {
		Lista copia = new Lista();
		try{
			copia = (Lista)(super.clone());
		}
		catch(CloneNotSupportedException e){
			e.printStackTrace(System.err);
		}
		copia.primero = (Nodo)(this.primero.clone());
		copia.primero.setAnterior(null);
		copia.primero.setSiguiente(null);
		copia.corriente = copia.primero;
		for (corriente = primero.getSiguiente(); corriente != null;corriente=corriente.getSiguiente()){
			Nodo aux = (Nodo)(corriente.clone());
			copia.corriente.setSiguiente(aux);
			aux.setAnterior(copia.corriente);
			copia.corriente = aux;
		}
		copia.ultimo = copia.corriente;
		return copia;
	}
}