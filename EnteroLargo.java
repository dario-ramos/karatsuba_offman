import lista.*;
import lista.Elemento;

public class EnteroLargo implements Cloneable{
/*-----------------------------------------------*/
	private Lista numero;
	 /**1:positivo, -1: negativo*/
	private int signo; 
	private long cantCifras;	
	/**Cantidad maxima de cifras que puede tener un long tal que su cuadrado no supere
	 *a MAX_LONG.  */
	public static final short CIFRAS = 4;
	
	public static short PRI = -2;
	public static short ANT = -1;
	public static short SIG = 1;
	public static short ULT = 2;	
/*-----------------------------------------------*/

	public EnteroLargo (){
	   numero = new Lista();
	   signo = 1;
	   cantCifras = 0;	   
	}

	public boolean elegirCifra(short mov){
	   return numero.moverCte(mov);	   
	}

	public void insertarCifra(short cifra, short mov){
		Short s = new Short(cifra);
		Elemento e = new Elemento (s);
		Nodo n = new Nodo (e);
		numero.insertar(n, mov);
		cantCifras++;
	}

	public void quitarPrimCifra (){
	   numero.moverCte(PRI);
	   numero.elimCte();
	   cantCifras--;
	}

	public void cambiarSigno (){
	   signo = -signo;
	}

	public int getSigno (){
	   if (this.esCero()) return 1;
	   else return signo;
	}

	public boolean esCero (){
	   boolean pareceCero = true;
	   int  i;

	   numero.moverCte(PRI);
	   for (i=0;((i<cantCifras) && (pareceCero));i++){
			pareceCero = ((pareceCero)&&(this.cifraActual()== 0));
	        numero.moverCte(SIG);
	   }
	   return pareceCero;
	}

	public long getCantCifras(){
	   return cantCifras;
	}
	/***************************************************************************
	 * PRE: Numero con cantidad de digitos mayor que 0.*/
	public short cifraActual (){
	   return ((Short)(numero.getCorriente()).getDato()).shortValue();
	}

	public void cargarNumero (long origen){
		this.borrar();
		Lista destino = new Lista ();
	   if (origen == 0){
			this.insertarCifra ((short)0,ANT);
	   }else{
	      long cantCifras;

			if (origen < 0){
				this.cambiarSigno ();
				origen=-origen;
			}
			cantCifras = (long) (Math.floor((double)((Math.log((double)origen)))/(Math.log(10))) + 1);
	      for(long numCifra = 0; numCifra < cantCifras; numCifra++){
	         int digito = 0;

	         digito = (int) origen % 10;
	         origen /= 10;
				this.insertarCifra ((short)digito,ANT);
	      }
		}
	}

	public void borrar (){
	   cantCifras = 0;
	   signo  = 1;
	   numero.vaciar ();
	}

	public void quitarCerosIzq(){
		if (!this.esCero()){	
		   boolean parar;
		   parar = false;
	
		   this.elegirCifra(PRI);
		   while(!parar){
		      if (this.cifraActual() == 0) this.quitarPrimCifra();
		      else parar = true;
		   }
		}else {
			this.borrar();
			this.insertarCifra((short)0,PRI);			
		}
	}	
	/***************************************************************************
	 * Devuelve una copia profunda del argumento implicito.*/
	public EnteroLargo copia(){
		return ((EnteroLargo)this.clone());
	}
	/***************************************************************************
	 * PRE: los dos numeros deben tener la misma cantidad de cifras.
	 * POST: devuelve la suma de los numeros, tratandolos como naturales y un d�gito
	 * que representa el acarreo en el ultimo paso.*/
	private EnteroLargo sumador(EnteroLargo num2, Integer acarreo) {
	   EnteroLargo sumaUAL; //Suma como la Unidad Arimetico - Logica.
	   boolean error = (this.elegirCifra(ULT));
	   error = (num2.elegirCifra(ULT) || (error));
	   acarreo = new Integer(0); 
	   sumaUAL = new EnteroLargo();
	   while (!error){
	      int sumAux = acarreo.intValue() + this.cifraActual() + num2.cifraActual();
	      if (sumAux>=10){acarreo = new Integer(1); sumAux-=10;}
	      else acarreo = new Integer(0);
	      sumaUAL.insertarCifra((short)sumAux,ANT);
			error = (this.elegirCifra(ANT));
			error = (num2.elegirCifra(ANT) || (error));
	   }
	   return sumaUAL;
	}
	/***************************************************************************
	 * Calcula el complemento a la base de un n�mero entero:
	 * Si el numero es positivo, lo deja igual pero le agrega un cero a la izquierda
	 * para representar su signo.
	 * Si un numero es negativo le invierte todos sus digitos (restandoselos a nueve),
	 * le suma uno al resultado y le agrega un nueve a la izquierda para representar
	 * su signo.*/
	private EnteroLargo complementar() {
		int signo = this.getSigno();
		EnteroLargo uno = new EnteroLargo (); uno.insertarCifra ((short)1,PRI);
		EnteroLargo complementado = new EnteroLargo ();
		
		this.elegirCifra(PRI);
		for(int i=0; i<this.getCantCifras(); i++){
			if (signo<0)
				complementado.insertarCifra ((short)(9-this.cifraActual()),SIG);
			else complementado.insertarCifra (this.cifraActual (),SIG);
			this.elegirCifra (SIG);
		}
		if (signo<0){
			EnteroLargo sum;
			Integer acarreo = new Integer(0);

			complementado.insertarCifra ((short)9,PRI);
			complementado.equilibrar(uno);
			sum = complementado.sumador(uno, acarreo);
			complementado.borrar ();
			complementado = sum.copia();
			sum.borrar ();
			complementado.elegirCifra(PRI);
			/*
			 * El sumador hace una suma con complemento a la base y esta es la senial
			 * de acarreo que devuelve. Lo agregamos despues del signo, lo que im-
			 * plica insertar una cifra significativa.
			 * En este caso no hay que analizar overflow ya que los sumandos son de
			 * distinto signo (complementado < 0 y uno > 0)
			 */
			if (acarreo.intValue() != 0) complementado.insertarCifra(acarreo.shortValue(),SIG);
	   }else complementado.insertarCifra ((short)0,PRI);
	   uno.borrar();
	   return complementado;
	}
	/***************************************************************************
	 * Descomplementa un numero entero al que se aplico "complementar".
	 * Le quita, al numero, el digito indicador de signo y, si es negativo,
	 * complementa a nueve todos sus digitos y le suma uno al resultado.*/
	private EnteroLargo descomplementar (){
	   int signo;
	   Integer acarreo = new Integer(0);
	   EnteroLargo aux,uno;
	   EnteroLargo descomplementado = this.copia();	   

	   descomplementado.elegirCifra(PRI);
	   signo = (descomplementado.cifraActual() != 9)?(1):(-1);	 
	   //Se retira el digito que indica el signo.
	   descomplementado.quitarPrimCifra();	   
	   if (signo < 0){
	      uno = new EnteroLargo(); uno.insertarCifra((short)1,ULT);	      
	      aux = new EnteroLargo();
	      descomplementado.elegirCifra(ULT);	      
	      for(int i=0; i<descomplementado.getCantCifras(); i++){	      	     	
	      	aux.insertarCifra ((short)((short)9-descomplementado.cifraActual()),ANT);
			descomplementado.elegirCifra(ANT);
	      }	            
	      aux.equilibrar(uno);	      
	      descomplementado = aux.sumador(uno, acarreo);	      
	      descomplementado.elegirCifra(ULT);
	      if (acarreo.intValue() == 1) aux.insertarCifra(acarreo.shortValue(),ULT);
	      descomplementado.cambiarSigno();
	      aux.borrar();
	      uno.borrar();
	   } 	   
	   return descomplementado;	   
	}	
	/***************************************************************************
	 * Suma dos numeros enteros calculando su complemento a la base, sum�ndolos como
	 * naturales, con "sumador", y descomplementando el resultado.
	 * Debido a que no trabajamos con una cantidad fija de digitos, tendremos en cuenta
	 * si hubo overflow para expandir la cantidad de digitos del resultado.
	 * PRE: ambos numeros deben estar creados y no vacios.
	 * OBS: el overflow se da si y solo si los sumandos tienen identico signo y este es
	 * opuesto al de la suma. */
	public EnteroLargo suma (EnteroLargo num2){	   
	   EnteroLargo resultado;	   
	   if (this.esCero ()) resultado = num2.copia();
	   else if (num2.esCero()) resultado = this.copia();
	   else{
	      Integer acarreo = new Integer(0);
	      EnteroLargo complementado1, complementado2;
	      
	      complementado1= this.copia();
	      complementado2= num2.copia();		  
	      complementado1.equilibrar(complementado2);	      
	      System.out.println();
	      complementado1 = complementado1.complementar();
	      complementado2 = complementado2.complementar();	      
	      
	      resultado = complementado1.sumador (complementado2,acarreo);	      
	      
	   /*Lo que sigue es para actuar en caso de un overflow en la suma...*/
	      complementado1.elegirCifra(PRI);
	      complementado2.elegirCifra(PRI);
	      if ((complementado1.cifraActual() == complementado2.cifraActual()) &&
	         (complementado2.cifraActual() != resultado.cifraActual())){
	            resultado.insertarCifra(complementado1.cifraActual(),PRI);	            
	            }
	   /*----------------------------------------------------------------*/
	      complementado1.borrar();
	      complementado2.borrar();	      
	      resultado = resultado.descomplementar();	      
	      resultado.quitarCerosIzq();
	   }
	   return resultado;
	}
	/***************************************************************************
	 * Resta num2 a num1.*/
	public EnteroLargo resta (EnteroLargo num2){
	   num2.cambiarSigno();
	   return (this.suma(num2));
	}
	/***************************************************************************
	 * PRE:  El argumento implicito debe haber sido creado.
	 * POST: Agrega ceros a la izquierda.*/
	private void agregarCeros(long cantidad){
	   this.elegirCifra (PRI);
	   
	   for (long i=0;i<cantidad;i++)
	      this.insertarCifra((short)0, SIG);
	}
	/***************************************************************************
	 * Agrega ceros a la derecha, tantos como indique el exponente. */
	private void shift10(long exponente){
	   if (!this.esCero()){
	      this.elegirCifra(ULT);
	      for (long i=0;i<exponente;i++)
		 		this.insertarCifra((short)0, SIG);
	   }
	}	
	/*************************************************************************** 
	 * Divide al argumento implicito en dos partes de igual longitud, asignandole
	 * la primera a u1 y la segunda a u2*/
	private void partir(EnteroLargo u1, EnteroLargo u2){
	   	  
	   this.elegirCifra(PRI);
	   for(long i=0;i<this.getCantCifras()/2;i++){
	      u1.insertarCifra(this.cifraActual(),SIG);
	      this.elegirCifra(SIG);
	   }	   
	   for(long i=this.getCantCifras()/2; i<this.getCantCifras(); i++){
	      u2.insertarCifra(this.cifraActual(),SIG);
	      this.elegirCifra(SIG);
	   }	   
	   if (this.getSigno() < 0){
	      u1.cambiarSigno();
	      u2.cambiarSigno();
	   }	   
	}
	/**************************************************************************
	 * Devuelve a los dos numeros con la misma cantidad de digitos, que es la del
	   que mas tiene. Para ello, agrega ceros a la izquierda.*/
	private void equilibrar(EnteroLargo num2){
	   this.elegirCifra(PRI);
	   num2.elegirCifra(PRI);
	   while (this.getCantCifras() < num2.getCantCifras())
	      this.insertarCifra((short)0,ANT);
	   while (this.getCantCifras() > num2.getCantCifras())
	      num2.insertarCifra((short)0,ANT);
	}
	/***************************************************************************
	 * Devuelve el cociente entre el argumento implicito y "den", y ademas modifica
	 * a "resto" con el resto de la division. */
	private EnteroLargo divisionEntera (long den, LongMejorado resto){	   	
	   boolean error = false;
	   long numeradorAux = 0;
	   EnteroLargo cociente = new EnteroLargo ();
	   
	   this.elegirCifra (PRI);
	   resto.setValor(0);
	   while (!error){
	      numeradorAux = resto.getValor();
	      while ((numeradorAux < den) && (!error)){
	         numeradorAux *= 10;
	         numeradorAux += this.cifraActual();
	         error = this.elegirCifra (SIG);
	      }
	      cociente.insertarCifra((short)(numeradorAux/den), SIG);
	      resto.setValor(numeradorAux % den);
	   }	   
	   return cociente;
	}
	
	/** 
	 * PRE: num1 y num2 deben estar equilibrados y tener menos digitos que CIFRAS.
	 * Ello es necesario para que su producto pueda ser expresado con un tipo long. */
	public EnteroLargo productoUsual (EnteroLargo num2){		
		long n1 = 0;
		long n2 = 0;
		long cifra;
		boolean error = false;
		EnteroLargo resultado = new EnteroLargo();
		
		this.elegirCifra (PRI);
		num2.elegirCifra (PRI);
		 /*Ambos numeros tienen la misma cantidad de cifras*/
		cifra=0;
		while (!error){
			n1 *= 10;
			n2 *= 10;
			n1 += this.cifraActual();
			n2 += num2.cifraActual();
			error = this.elegirCifra (SIG);
			num2.elegirCifra (SIG);
			cifra++;
		}		     	
		resultado.cargarNumero(n1*n2);		     	
		if (this.getSigno() != num2.getSigno()) resultado.cambiarSigno();		     	
		return resultado;
	}
	/***************************************************************************
	 * Producto Karatsuba-Ofman. */
	public EnteroLargo multKO (EnteroLargo num2) {
	   EnteroLargo u, v; 
	   EnteroLargo productoKO;

	   u = this.copia();   u.quitarCerosIzq ();
	   v = num2.copia();   v.quitarCerosIzq ();
	   if (u.esCero() || v.esCero()){
			productoKO = new EnteroLargo();
			productoKO.cargarNumero (0);
			return (productoKO);
	   }else if ((u.getCantCifras() <= CIFRAS) && (v.getCantCifras() <= CIFRAS)){
	      u.equilibrar(v);
	      return u.productoUsual(v);
	   }else{
	      EnteroLargo u1, u2, v1, v2;
	      u1 = new EnteroLargo(); u2 = new EnteroLargo(); 
	      v1 = new EnteroLargo(); v2 = new EnteroLargo();
		  EnteroLargo M,L,H,J,u1masu2,v1masv2;

	      u.equilibrar(v);
	      if((u.getCantCifras() % 2) != 0) {
	         u.insertarCifra((short)0,PRI);
	         v.insertarCifra((short)0,PRI);
	      }
	      u.partir(u1,u2);
	      v.partir(v1,v2);
	      /*Armo los terminos de la expresion. Apenas dejo de usar algo, lo borro.*/
	      M= u1.multKO (v1);
			L= u2.multKO (v2);
			u1masu2= u1.suma(u2);
			v1masv2= v1.suma(v2);
			H= u1masu2.multKO(v1masv2);
	      u1.borrar ();   u2.borrar ();
			v1.borrar ();   v2.borrar ();
			u1masu2.borrar ();
			v1masv2.borrar ();
	      L.cambiarSigno();
	      M.cambiarSigno();
			J= H.suma(L);
	      H.borrar();
	      J= J.suma(M);
			L.cambiarSigno();
	        M.cambiarSigno();
	      /* Hago los shifts necesarios. */
	      M.shift10(u.getCantCifras());
	      J.shift10((u.getCantCifras())/2);
	      H= M.suma(J);
			M.borrar(); J.borrar();
	      productoKO= H.suma(L);
			H.borrar();   L.borrar();
	      return productoKO;
	   }
	}
	/***************************************************************************
	 * El producto "Ingenuo". */
	public EnteroLargo multIngenua (EnteroLargo num2){	     
	   EnteroLargo productoIngenuo;
	   EnteroLargo u = this.copia();
	   EnteroLargo v = num2.copia();	   
	   u.equilibrar(v);	   

		if (u.esCero () || v.esCero ()) {
			productoIngenuo = new EnteroLargo();
			productoIngenuo.cargarNumero (0);
	        return (productoIngenuo);
		}else if ((u.getCantCifras()<=CIFRAS) && (v.getCantCifras()<=CIFRAS)){			
			u.equilibrar(v);
	        return u.productoUsual(v);
		}else{
		  EnteroLargo u1,u2, v1,v2;
	      u1 = new EnteroLargo(); u2 = new EnteroLargo(); 
	      v1 = new EnteroLargo(); v2 = new EnteroLargo();
	      EnteroLargo M,L,H,J,HJ,ML;

	      if ((u.getCantCifras() % 2) !=0) {
	         u.insertarCifra((short)0,PRI);
	         v.insertarCifra((short)0,PRI);
	      }
	      u.partir(u1,u2);	      
	      v.partir(v1,v2); 	      
	      /* Armo los terminos de la expresion */
	      M= u1.multIngenua(v1);	      		  
	      L= u2.multIngenua(v2);	      
	      H= u1.multIngenua(v2);	      
	      J= u2.multIngenua(v1);	      
	      u1.borrar ();   u2.borrar ();
	      v1.borrar ();   v2.borrar ();
	      HJ= H.suma(J);	      
	      H.borrar(); J.borrar();
	      M.shift10(u.getCantCifras() );	      
	      ML= M.suma(L);	      
	      M.borrar(); L.borrar();
	      HJ.shift10((u.getCantCifras())/2 );	      	      
	      productoIngenuo= ML.suma(HJ);	      
	      ML.borrar(); HJ.borrar();
	      return(productoIngenuo);
	   }
	}
	/**************************************************************************
	 * Potenciacion basada en multKO. La base es el argumento implicito. */
	public EnteroLargo potenciaKO (EnteroLargo exponente){
	   EnteroLargo potencia;

	   if (exponente.esCero ()){
			potencia = new EnteroLargo();
			potencia.cargarNumero (1);
			return potencia;
	   }else{
	      EnteroLargo cociente;
	      LongMejorado resto = new LongMejorado(0); /*El exponente nunca sera negativo.*/
	      EnteroLargo aux;
			cociente= exponente.divisionEntera(2,resto);
			potencia = this.potenciaKO(cociente);
	      cociente.borrar ();
	      aux = potencia.multKO(potencia);
	      potencia.borrar ();
	      if (resto.getValor() != 0) {
				potencia = aux.multKO(this);
	   	   aux.borrar();
	   	   return potencia;
	      }else return aux;
	   }
	}
	/**************************************************************************
	 * Potenciacion basada en multIngenua. La base es el argumento implicito.*/
	public EnteroLargo potenciaIngenua (EnteroLargo exponente) {
	   EnteroLargo potencia;	   

		if (exponente.esCero ()){
		  potencia = new EnteroLargo();
	      potencia.cargarNumero (1);	      
	      return potencia;
		}else{
	      EnteroLargo cociente, aux;
	      cociente = new EnteroLargo(); aux = new EnteroLargo();
		  LongMejorado resto = new LongMejorado(0);      /* Exponente siempre positivo. */

		  cociente = exponente.divisionEntera(2,resto);		  
		  potencia= this.potenciaIngenua(cociente);		  
		  cociente.borrar();
		  aux= potencia.multIngenua(potencia);		  
		  potencia.borrar ();
		  if (resto.getValor() != 0){
	         potencia= aux.multIngenua(this);
	         aux.borrar ();	         
	         return potencia;
		  }else {		  	
		  	return aux;
		  }
		}
	}

	public Object clone(){
		EnteroLargo copia = new EnteroLargo();
		try{
			copia = (EnteroLargo)super.clone();
		}
		catch(CloneNotSupportedException e){
			e.printStackTrace(System.err);
		}
		copia.numero = (Lista)(this.numero.clone());
		copia.signo = this.signo;
		copia.cantCifras = this.cantCifras;
		return copia;
	}
	public String toString (){
		String versionVisible = new String ();
		if (this.getSigno()== -1) versionVisible = versionVisible+'-'; 
		this.elegirCifra(PRI);
		for (int i= 0;i < this.getCantCifras();i++){
			versionVisible = versionVisible + this.cifraActual();
			this.elegirCifra(SIG);
		}
		return versionVisible;
	}	
}
