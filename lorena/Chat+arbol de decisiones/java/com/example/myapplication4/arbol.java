package com.example.myapplication4;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class arbol {
    private String _user;
    private String _contact;
    private static ArrayList<String> questions =new ArrayList <String>();

    public arbol (String username, String contacto){
        _user=username;
        _contact=contacto;
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
    }

    private int estado = 0;

    public String play(String res){
        String answer=null;

        switch (estado){
                case 0:
                    answer=(questions.get(1));
                    estado = 1;
                    break;

                case 1:
                    if (res.equals("si")){
                        answer= (questions.get(2));
                        estado=2;
                    }
                    if (res.equals("no")){
                        answer=("call police");
                        estado=100;
                    }
                    break;
                case 2:
                    if (res.equals("si")){
                        answer=(questions.get(9));
                        estado=9;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(3));
                        estado=3;
                    }
                    break;
                case 3:
                    if (res.equals("si")){
                        answer=("call police");
                        estado=100;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(4));
                        estado=4;
                    }
                    break;
                case 4:
                    if (res.equals("si")){
                        answer=(questions.get(5));
                        estado=5;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(9));
                        estado=9;
                    }
                    break;
                case 5:
                    if (res.equals("si")){
                        answer=(questions.get(6));
                        estado=6;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(7));
                        estado=7;
                    }
                    break;
                case 6:
                    if (res.equals("si")){
                        answer=("call police");
                        estado=100;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(7));
                        estado=7;
                    }
                    break;
                case 7:
                    if (res.equals("si")){
                        answer=(questions.get(8));
                        estado=8;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(9));
                        estado=9;
                    }
                    break;
                case 8:
                    if (res.equals("si")){
                        answer=("call police");
                        estado=100;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(9));
                        estado=9;
                    }
                    break;
                case 9:
                    if (res.equals("si")){
                        answer=(questions.get(12));
                        estado=12;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(10));
                        estado=10;
                    }
                    break;
                case 10:
                    if (res.equals("si")){
                        answer=(questions.get(12));
                        estado=12;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(11));
                        estado=11;
                    }
                    break;
                case 11:
                    if (res.equals("si")){
                        answer=("call police");
                        estado=100;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(15));
                        estado=15;
                    }
                    break;
                case 12:
                    if (res.equals("si")){
                        answer=(questions.get(16));
                        estado=16;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(13));
                        estado=13;
                    }
                    break;
                case 13:
                    if (res.equals("si")){
                        answer=(questions.get(16));
                        estado=16;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(14));
                        estado=14;
                    }
                    break;
                case 14:
                    if (res.equals("si")){
                        answer=(questions.get(16));
                        estado=16;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(15));
                        estado=15;
                    }
                    break;
                case 15:
                    if (res.equals("si")){
                        answer=("call " +_contact);
                        estado=98;
                    }
                    if (res.equals("no")){
                        answer=("Fin ");
                        estado=99;
                    }
                    break;
                case 16:
                    if (res.equals("si")){
                        answer=(questions.get(18));
                        estado=18;
                    }
                    if (res.equals("no")){
                        answer=(questions.get(17));
                        estado=17;
                    }
                    break;
                case 17:
                    if (res.equals("si")){
                        answer=("Fin ");
                        estado=99;
                    }
                    if (res.equals("no")){
                        estado=17;
                        try{
                            TimeUnit.SECONDS.sleep(5);
                        }catch(Exception e){}
                        answer=(questions.get(17));
                    }
                    break;
                case 18:
                    if (res.equals("si")){
                        answer=("call police");
                        estado=100;
                    }
                    if (res.equals("no")){
                        estado=17;
                        try{
                            TimeUnit.SECONDS.sleep(5);
                        }catch(Exception e1){}
                        answer=(questions.get(17));
                    }
                    break;
                case 98:
                    estado=101;
                    break;
                case 99:
                    estado=101;
                    break;
                case 100:
                    estado=101;
                    break;


            }
        return answer;
    }

}