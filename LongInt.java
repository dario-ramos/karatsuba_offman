
public class LongInt {
	
	private long valor;
	
	public LongInt(long inicial){
		this.valor = inicial;
	}
	
	public long getValor() {
		return valor;
	}
	public void setValor(long valor) {
		this.valor = valor;
	}
	public String toString (){
		String retorno = new String ();
		retorno = retorno + this.valor;
		return retorno;
	}
}
