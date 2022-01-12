package com.example.sistemas.casalinda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemas.casalinda.Utilidades.claseGlobal;
import com.example.sistemas.casalinda.entidades.Punto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PuntoActivity extends AppCompatActivity {

    Spinner comboPersonas;

    TextView twfuncioanrio;
    ArrayList<String> listaPuntos;
    ArrayList<Punto> puntosList;
    //String  c_funcionario, funcionario,c_punto_venta,punto;
    Connection connect;
    //claseGlobal objEscritura=(claseGlobal)getApplicationContext();
    //claseGlobal objLectura=(claseGlobal)getApplicationContext();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto);
        claseGlobal objLectura=(claseGlobal)getApplicationContext();

        twfuncioanrio = findViewById(R.id.txtFuncionario);
        twfuncioanrio.setText(objLectura.getFuncionario());
        preferences=getSharedPreferences("guardar", Context.MODE_PRIVATE);
        editor=preferences.edit();
        consultarpersona();
        comboPersonas = findViewById(R.id.comboPersonas);
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listaPuntos);
        comboPersonas.setAdapter(adaptador);

    }

    public void consultarpersona() {
        try {
            Punto peresona = null;

            puntosList = new ArrayList<Punto>();

            ConnectionStr conStr = new ConnectionStr();
            connect = conStr.connectionclasss();        // Connect to database

            String query = "select Rtrim(C_Punto_Venta)C_punto, rtrim(N_Punto_Venta)n_Punto_venta, C_Bodega from Fac_Puntos_Venta where Flag_Vigente=1";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs!=null) {
                while (rs.next()) {
                    puntosList.add(new Punto(rs.getString("c_punto"),rs.getString("n_Punto_venta")));

                }
            }
            obtenerLista();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultabodega(String p) {
        try {

            claseGlobal objEscritura=(claseGlobal)getApplicationContext();


            ConnectionStr conStr = new ConnectionStr();
            connect = conStr.connectionclasss();        // Connect to database
           // '"+ passwordd +"'  ";
            String query = "select  C_Bodega from Fac_Puntos_Venta where C_punto_venta='"+ p +"'  ";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                editor.putString("Bodega",rs.getString("C_Bodega"));
                editor.apply();
                objEscritura.setC_bodega(preferences.getString("Bodega", "No existe"));
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void obtenerLista() {
        listaPuntos = new ArrayList<String>();
        //listaPuntos.add("Seleccione");
        for (int i = 0; i < puntosList.size(); i++) {
            listaPuntos.add(puntosList.get(i).getC_punto() + " - " + puntosList.get(i).getPunto());
        }
    }

    public void goMainP(View view) {
        Intent goMainP = new Intent(PuntoActivity.this, MainActivity.class);

        claseGlobal objEscritura=(claseGlobal)getApplicationContext();
        claseGlobal objLectura=(claseGlobal)getApplicationContext();

        String seleccion=comboPersonas.getSelectedItem().toString();
        //punto=seleccion;
        editor.putString("Punto", seleccion);
        editor.putString("CPunto",seleccion.substring(0,3));
        editor.apply();
        objEscritura.setPunto(preferences.getString("Punto","No existe"));
        objEscritura.setC_punto_venta(preferences.getString("CPunto","No existe"));
        //consultabodega(objLectura.getC_punto_venta());
        consultabodega(preferences.getString("CPunto","No Existe"));
        startActivity(goMainP);
        finish();
    }

    public void salir(View view) {
        finish();
    }
}