/* Principal del TP1. Ejecuta las operaciones y mide los tiempos.  */
import java.io.*;

public class tdatp1 {
	public static EnteroLargo generarNumAl (long cifras){
		short a = 0;
		EnteroLargo retorno = new EnteroLargo();

		for (long i=0;i<cifras;i++){
			a = (short)(Math.floor(Math.random() * 10));
			retorno.insertarCifra (a,EnteroLargo.PRI);
		}
		return retorno;
	}
	public static void generarEntrada (String nomArch){
		Character op = new Character('+');
		meterTodo (nomArch,op);		
		op = new Character('-');
		meterTodo (nomArch,op);
		op = new Character('*');
		meterTodo (nomArch,op);
		op = new Character('k');
		meterTodo (nomArch,op);				
		op = new Character('^');
		meterTodo (nomArch,op);
		op = new Character('q');
		meterTodo (nomArch,op);								
	}
	public static void guardarEnArchivo (String linea, String nomArch,boolean agregar){
		try{
			FileWriter escritor = new FileWriter(nomArch,agregar);
			BufferedWriter intermediario = new BufferedWriter(escritor);
			PrintWriter salida = new PrintWriter(intermediario);
			salida.println(linea);
			salida.close();
		}catch (Exception e){
			//Estoy seguro de que no hay ninguna.
		}
	}
	public static void guardarLinea (int cifras,String nomArch,Character op,boolean agregar){
		System.out.println ("Guardando la linea de numeros de "+cifras+" cifras para la operacion: "+op.charValue());
		EnteroLargo num1 = new EnteroLargo();
		EnteroLargo num2 = new EnteroLargo();
		num1 = generarNumAl (cifras);
		num2 = generarNumAl (cifras);
		guardarEnArchivo (cadenaOperacion(num1,num2,op),nomArch,agregar);
	}
	public static void meterTodo (String nomArch, Character op){
		for (int i=5;i<=50;i+=5){
			guardarLinea (i,nomArch,op,true);
		}		
	}
	public static void generarEntrada (){
		generarEntrada ("entrada.txt");
	}

	/** Carga los operandos y determina la operaci�n a realizar.
	 *  PRE: num1 y num2 deben estar creados y vac�os, y linea debe ser distinta de null.*/
	public static char procesarLineaDeEntrada(String linea, EnteroLargo num1, EnteroLargo num2){
		Character op;
		Character c = new Character ('.');		

		boolean finNum1, finNum2;
		finNum1 = finNum2 = false;
		int i=0;
		if (linea.charAt(i) == '-') { 
			num1.cambiarSigno();
			i++;
		}		
		
		while ((i<linea.length()) && (!finNum1)) {
			c = new Character(linea.charAt(i));
			finNum1 = !(Character.isDigit(c.charValue()));
			if (!finNum1){
				num1.insertarCifra((short)
						(Character.getNumericValue(c.charValue())),EnteroLargo.SIG);
			}
			i++;
		}		
		op = new Character(linea.charAt(i));		
		i+=2;
		if (linea.charAt(i) == '-') {
			num2.cambiarSigno();
			i++;
		}		
		while ((i<linea.length()) && (!finNum2)) {
			c = new Character(linea.charAt(i));
			finNum2 = !(Character.isDigit(c.charValue()));
			if (!finNum2){				
				num2.insertarCifra((short)
						(Character.getNumericValue(c.charValue())),EnteroLargo.SIG);
			}
			i++;
		}		
		return op.charValue();
	}

	public static String cadenaOperacion (EnteroLargo num1, EnteroLargo num2, Character op){
		String linea = new String("");
		linea = linea.concat(num1.toString());
		linea = linea.concat(" "+op.charValue()+" ");
		linea = linea.concat(num2.toString());
		return linea;
	}
	/** Pasa el resultado, la cantidad de cifras de los operandos y el tiempo
	 *  insumido a un String.*/
	public static String procesarLineaDeSalida(EnteroLargo result, long n,long Tn){
		String linea = new String("");
		boolean error = false;
		
		String resultado = result.toString();
		if (resultado.length()>=0){
			for (int i=0; i<resultado.length();i++){
				char c = resultado.charAt(i);
				linea = linea+c;					
			}
			linea = linea + "  n: ";
			linea = linea + n;
			linea = linea + "  T(n): ";
			linea = linea + Tn;
		}else{
			linea = linea + "ERROR";
		}
		return linea;
	}

	public static void main(String args[]){
		
		EnteroLargo result = new EnteroLargo();
		boolean error = false;
		char op = '.';
		long n = 0;
		long Ti,Tf,Tn = 0;
		//Permite representar la hora del sistema en milisegundos.
		String linea = new String();		
		/**/System.out.println("Mat�as Aleardo Mazzei 84496 ");
		/**/System.out.println("Dar�o Eduardo Ramos  84885 ");

		/**/System.out.println("ENTRADA: "+ args[0]);
		/**/System.out.println("SALIDA : "+ args[1]);
		/**/System.out.println("Se ejecutar�n las �rdenes del archivo: "+ args[0]);

		try{
			FileReader lector = new FileReader (args[0]);
			BufferedReader entrada = new BufferedReader(lector);

			FileWriter escritor = new FileWriter(args[1]);
			BufferedWriter intermediario = new BufferedWriter(escritor);
			PrintWriter salida = new PrintWriter(intermediario);
			System.out.println("Calculando..."); 
			linea = entrada.readLine();
			guardarEnArchivo ("RESULTADO - CIFRAS - TIEMPO (en milisegundos) - OP",args[1],false);//Creo un nuevo archivo para la salida.
			while ((linea != null) && (!error)){
				EnteroLargo num1 = new EnteroLargo();
				EnteroLargo num2 = new EnteroLargo();
				op = procesarLineaDeEntrada(linea,num1,num2);				 
				// n es el m�ximo entre las longitudes de num1 y num2.
				n = (num1.getCantCifras()>num2.getCantCifras())?num1.getCantCifras():num2.getCantCifras();

				//Comienza la medici�n del tiempo (se desprecia la evaluaci�n de op).
				//Antes, se llama al recolector de basura por las dudas.
				System.gc();
				Ti = System.currentTimeMillis();
				switch (op){
					case '+':	System.out.println("NUM1: "+num1); 
								System.out.println("NUM2: "+num2);
								result = num1.suma(num2);
					            System.out.println("RES: "+result);
									break;
					case  '-':	result=  num1.resta(num2);
								System.out.println("Resultado: "+result);
									break;
					case  'k':	result=  num1.multKO(num2);
								System.out.println("Resultado: "+result);
									break;
					case  '*':	result=  num1.multIngenua(num2);
								System.out.println("Resultado: "+result);
									break;
					case  'q':	result=  num1.potenciaKO(num2);
								System.out.println("Resultado: "+result);
									break;
					case  '^':	result=  num1.potenciaIngenua(num2);
								System.out.println("Resultado: "+result);
									break;
					default  :	error = true;
				}
				// Finaliza la medici�n del tiempo.
				Tf = System.currentTimeMillis();
				Tn = Tf-Ti;
				linea = procesarLineaDeSalida(result,n,Tn);
				System.out.println("Se guardaron los datos de la operaci�n "+op+" con "+n+" cifras");
				guardarEnArchivo (linea + " Op: " + op,args[1],true);
				linea = entrada.readLine();
			}
			entrada.close();
		}catch(FileNotFoundException archNoEnc){
			System.out.println("Generando entrada...");
			generarEntrada (args[0]);
			System.out.println("Archivo no encontrado: " + archNoEnc);
			System.out.println("Se ha creado un nuevo archivo de pruebas con nombre: "+args[0]);
		}catch(IOException ioExc){
			ioExc.printStackTrace(System.err);
		}
		System.out.println("----------PROGRAMA TERMINADO----------"); 
	}
}