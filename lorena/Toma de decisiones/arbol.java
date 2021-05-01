import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Date;

class Arbol{
	
	private String _user;
	private String _contact;
	private static ArrayList <String> questions =new ArrayList <String>();
	
	public Arbol (String username, String contacto){
		_user=username;
		_contact=contacto;
	}


	public void play(){
		
		Scanner teclado=new Scanner(System.in);
		teclado.setSoTimeout(1000);
		String res=null;
		
		int estado =1;
		
		while (estado <101){
			switch (estado){
				
				case 1:
					System.out.println(questions.get(1));
					
					res=teclado.nextLine(); 
					
					 
					if (res.equals("si")){
						estado=2;
					}
					if (res.equals("no")){
						estado=100;
					}
					break;
				case 2:
					System.out.println(questions.get(2));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=9;
					}
					if (res.equals("no")){
						estado=3;
					}
					break;	
				case 3:
					System.out.println(questions.get(3));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=100;
					}
					if (res.equals("no")){
						estado=4;
					}
					break;	
				case 4:
					System.out.println(questions.get(4));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=5;
					}
					if (res.equals("no")){
						estado=9;
					}
					break;	
				case 5:
					System.out.println(questions.get(5));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=6;
					}
					if (res.equals("no")){
						estado=7;
					}
					break;	
				case 6:
					System.out.println(questions.get(6));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=100;
					}
					if (res.equals("no")){
						estado=7;
					}
					break;	
				case 7:
					System.out.println(questions.get(7));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=8;
					}
					if (res.equals("no")){
						estado=9;
					}
					break;	
				case 8:
					System.out.println(questions.get(8));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=100;
					}
					if (res.equals("no")){
						estado=9;
					}
					break;	
				case 9:
					System.out.println(questions.get(9));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=12;
					}
					if (res.equals("no")){
						estado=10;
					}
					break;	
				case 10:
					System.out.println(questions.get(10));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=12;
					}
					if (res.equals("no")){
						estado=11;
					}
					break;	
				case 11:
					System.out.println(questions.get(11));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=100;
					}
					if (res.equals("no")){
						estado=15;
					}
					break;	
				case 12:
					System.out.println(questions.get(12));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=16;
					}
					if (res.equals("no")){
						estado=13;
					}
					break;
				case 13:
					System.out.println(questions.get(13));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=16;
					}
					if (res.equals("no")){
						estado=14;
					}
					break;	
				case 14:
					System.out.println(questions.get(14));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=16;
					}
					if (res.equals("no")){
						estado=15;
					}
					break;	
				case 15:
					System.out.println(questions.get(15));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=98;
					}
					if (res.equals("no")){
						estado=99;
					}
					break;	
				case 16:
					System.out.println(questions.get(16));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=18;
					}
					if (res.equals("no")){
						estado=17;
					}
					break;
				case 17:
					System.out.println(questions.get(17));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=99;
					}
					if (res.equals("no")){
						estado=17;
						System.out.println("sleep");
						try{
							TimeUnit.SECONDS.sleep(5);
						}catch(Exception e){}
					}
					break;	
				case 18:
					System.out.println(questions.get(18));
					res=teclado.nextLine();
					
					if (res.equals("si")){
						estado=100;
					}
					if (res.equals("no")){
						estado=17;
						System.out.println("sleep");
						try{
							TimeUnit.SECONDS.sleep(5);
						}catch(Exception e1){}
					}
					break;	
				case 98:
					System.out.println("call " +_contact);
					estado=101;
					break;		
				case 99:
					System.out.println("Fin ");
					estado=101;
					break;		
				case 100:
					System.out.println("call police");
					estado=101;
					break;			
					
			
			}
		}
	}


	public static void main (String []args){
		questions.add("Vacio");
		questions.add("¿Estás bien?");
		questions.add("¿Te sientes seguro?");
		questions.add("¿Necesitas ayuda?");
		questions.add("¿Te sigue alguien?");
		questions.add("¿Parece peligroso?");
		questions.add("¿Te está increpando?");
		questions.add("¿Sientes que te sigue?");
		questions.add("¿Está cerca tuyo?");
		questions.add("¿Estás solo?");
		questions.add("¿Las otras personas son de confianza?");
		questions.add("¿Necesitas ayuda?");
		questions.add("¿Estás volviendo a casa?");
		questions.add("¿Estás yendo a tu trabajo?");
		questions.add("¿Estás yendo a una cita con tu médico, peluquero...?");
		questions.add("¿Necesitas compañía?");
		questions.add("¿Te falta mucho?");
		questions.add("¿Has llegado ya?");
		questions.add("¿Crees que te puede ocurrir algo antes de llegar?");
		
		Arbol tree= new Arbol(args[0], args[1]);
		
		tree.play();
	
	}
}