package com.example.micalculadora;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    List<String> operation = new LinkedList<String>();
    boolean radians = true,trig=false,mathe=false,result=false;
    String dummy="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }
    public void radians(View v){
        if(radians){
            radians = false;
            ((Button) v).setText("Deg");
        }
        else{
            radians = true;
            ((Button) v).setText("Rad");
        }

    }

    public void OnScreen(View v){
        Log.d("prueba","He pulsado: "+((Button) v).getText().toString());
        String btnclc = ((Button) v).getText().toString();
        TextView Tv = findViewById(R.id.screenTxt);

        if(result){
            dummy = "";
            operation.clear();
            Tv.setText("");
            result = false;
        }

        if(btnclc.equals("AC")){
            Tv.setText("");
            dummy ="";
            operation.clear();
            trig = false;
            mathe = false;
            result = false;
        } else if (btnclc.equals("sin") || btnclc.equals("cos") || btnclc.equals("tan") || btnclc.equals(")")) {
            if(btnclc.equals(")") && trig){
                trig = false;
                operation.add(dummy);
                dummy ="";
                operation.add(")");
                String nop = String.join(", ",operation).replace(",","").replace(" ","");
                Tv.setText(nop);
            } else if (btnclc.equals("sin") || btnclc.equals("cos") || btnclc.equals("tan")) {
                trig = true;
                operation.add(btnclc);
                operation.add("(");
                String nop = Tv.getText().toString() + btnclc+"(";
                Tv.setText(nop);
            }else {
                mathe = true;
                operation.add(" ");
                String nop = Tv.getText().toString() + btnclc;
                Tv.setText(nop);
            }

        } else if (btnclc.equals("Back")) {
            if(!operation.isEmpty()){
                if(trig){
                    trig=false;
                    operation.remove(operation.size()-2);
                    String nop = String.join(", ",operation).replace(",","").replace(" ","");
                    Tv.setText(nop);
                } else if (mathe && operation.get(operation.size()-1).equals(")")){
                    mathe = false;
                    operation.remove(operation.size()-1);
                    String nop = String.join(", ",operation).replace(",","").replace(" ","");
                    Tv.setText(nop);
                } else {
                    operation.remove(operation.size()-1);
                    String nop = String.join(", ",operation).replace(",","").replace(" ","");
                    Tv.setText(nop);
                }
            }else{
                Toast.makeText(this, "is empty",Toast.LENGTH_LONG);
            }


        }else if (btnclc.equals("=")) {
            if(mathe)
                Tv.setText("Syntax Error");
            else if (trig)
                Tv.setText("Calculator Error 1: Close the trigomic operation");
            else {
                double resultado = evaluarExpresion(operation,radians);
                if(resultado!=-1.0){
                    Tv.setText(String.valueOf((float) resultado));
                    result = true;
                }else {
                    Tv.setText("Math Error");
                }

            }

        } else{
            if(!trig){
                operation.add(btnclc);
                String nop = Tv.getText().toString() + btnclc;
                Tv.setText(nop);
            }else{
                dummy += btnclc;
                String nop = Tv.getText().toString() + btnclc;
                Tv.setText(nop);
            }
        }
    }

    public static double evaluarExpresion(List<String> expresion, boolean enRadianes) {
        try {
            return evaluar(expresion, enRadianes);
        } catch (Exception e) {
            return -1;  // Retorna -1 en caso de error
        }
    }

    // Función que evalúa una lista de strings
    private static double evaluar(List<String> expresion, boolean enRadianes) {
        double resultado = 0;
        char operador = '+';  // Empezamos con suma

        for (int i = 0; i < expresion.size(); i++) {
            String elemento = expresion.get(i);

            // Si el elemento es un número
            if (esNumero(elemento)) {
                double valor = Double.parseDouble(elemento);
                resultado = aplicarOperacion(resultado, valor, operador);
            }
            // Si es un operador
            else if (esOperador(elemento.charAt(0))) {
                operador = elemento.charAt(0);  // Actualizamos el operador
            }
            // Evaluar funciones trigonométricas
            else if (elemento.equals("sin") || elemento.equals("cos") || elemento.equals("tan")) {
                i+=2;  // Avanzar al siguiente elemento, que debería ser el argumento de la función trigonométrica

                if (i < expresion.size() && esNumero(expresion.get(i))) {
                    double argumento = Double.parseDouble(expresion.get(i));
                    argumento = convertirAngulo(argumento, enRadianes);  // Convertimos a radianes si es necesario

                    if (elemento.equals("sin")) {
                        resultado = aplicarOperacion(resultado, Math.sin(argumento), operador);
                    } else if (elemento.equals("cos")) {
                        resultado = aplicarOperacion(resultado, Math.cos(argumento), operador);
                    } else if (elemento.equals("tan")) {
                        resultado = aplicarOperacion(resultado, Math.tan(argumento), operador);
                    }
                } else {
                    // Si no es un número válido, o la lista está incompleta
                    return -1;  // Retornar error de sintaxis
                }
            }
        }

        return resultado;
    }

    // Función para convertir de grados a radianes si es necesario
    private static double convertirAngulo(double angulo, boolean enRadianes) {
        if (enRadianes) {
            return angulo;  // Si es radianes, no se hace ninguna conversión
        } else {
            return Math.toRadians(angulo);  // Si no es radianes, convertimos a radianes
        }
    }

    // Función para aplicar una operación (sin tener en cuenta la prioridad)
    private static double aplicarOperacion(double acumulado, double valor, char operador) {
        switch (operador) {
            case '+': return acumulado + valor;
            case '-': return acumulado - valor;
            case '*': return acumulado * valor;
            case '/': return acumulado / valor;
            default: return valor;  // Para el caso inicial
        }
    }

    // Verifica si el string es un número
    private static boolean esNumero(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Verifica si el caracter es un operador
    private static boolean esOperador(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/');
    }

}