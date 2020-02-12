package com.example.sistemas.casalinda;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemas.casalinda.Utilidades.claseGlobal;
import com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView;
import com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerViewE;
import com.example.sistemas.casalinda.entidades.Existencia;
import com.example.sistemas.casalinda.entidades.Pedido;
import com.example.sistemas.casalinda.entidades.Precio;
import com.example.sistemas.casalinda.interfaz.InterfazClickRecyclerView;
import com.example.sistemas.casalinda.interfaz.InterfazClickRecyclerViewE;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.CAN;
import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.COD;
import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.COL;
import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.CUV;
import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.NOM;
import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.POS;
import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.PVP;
import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.TIP;
import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.TOT;
import static com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView.UNI;

public class PedidoEditar extends AppCompatActivity {

    private String posicion;
    private String tipo;
    private String color;
    private String codigo;
    private String nombre;
    private String cantidad;
    private String cantidadN;
    private String unitario;
    private String total;
    private String inventario;
    private String pvp;
    private String cuv;

    String bodega, nbodega, unidades,ubicacion;
    /*final AdaptadorRecyclerViewE adaptadorRecyclerViewE=new AdaptadorRecyclerViewE(new InterfazClickRecyclerViewE() {
        @Override
        public void onClick(View vw, Existencia e) {
        }

    });
*/
    final AdaptadorRecyclerView adaptadorRecyclerView=new AdaptadorRecyclerView(new InterfazClickRecyclerView() {
        @Override
        public void onClick(View v, Pedido p) {

        }
    });
   final AdaptadorRecyclerViewE adaptadorRecyclerViewE=new AdaptadorRecyclerViewE(new InterfazClickRecyclerViewE() {
        @Override
        public void onClick(View vw, Existencia e) {

        }
    });
    TextView tvCodigo;
    TextView tvNombre;
    EditText edCant;
    EditText edUnit;
    Spinner spUni;
    TextView tvTotal;
    Button btAceptar;
    Button btEliminar;



    //ArrayList<Existencia> existenciaList;
   // ArrayList<String> listaExistencias;

    ArrayList<String> listaPrecios;
    ArrayList<Precio>preciosList;
    Connection connect;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_editar);

        posicion=getIntent().getStringExtra(POS);
        tipo=getIntent().getStringExtra(TIP);
        color=getIntent().getStringExtra(COL);
        codigo=getIntent().getStringExtra(COD);
        nombre=getIntent().getStringExtra(NOM);
        cantidad=getIntent().getStringExtra(CAN);
        unitario=getIntent().getStringExtra(UNI);
        total=String.valueOf( (double)Math.round ((Double.valueOf(getIntent().getStringExtra(TOT)))*100d)/100) ;
        pvp=getIntent().getStringExtra(PVP);
        cuv=getIntent().getStringExtra(CUV);

        tvCodigo= findViewById(R.id.tvCod);
        tvCodigo.setText(codigo);

        tvNombre= findViewById(R.id.tvNom);
        tvNombre.setText(nombre);

        edCant= findViewById(R.id.edtCan);
        edCant.setText(cantidad);

        edUnit=findViewById(R.id.edtUni);
        edUnit.setText(unitario);

        tvTotal= findViewById(R.id.tvTot);
        tvTotal.setText(total);

        RecyclerView recyclerViewExistencias=findViewById(R.id.eRecycler);

        btAceptar=findViewById(R.id.btAceptar);
        btEliminar=findViewById(R.id.btEliminar);

        try{
            consultarExistencias(codigo);
            consultarprecio(codigo);



            //ItemTouchHelper itemTouchHelper=new ItemTouchHelper(createHelperCallback());
           // itemTouchHelper.attachToRecyclerView(recyclerViewExistencias);
            //Configuramos cómo se va a organizar las vistas dentro del RecyclerView; simplemente con un LinearLayout para que
            //aparezcan una debajo de otra
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(PedidoEditar.this  );
            recyclerViewExistencias.setLayoutManager(linearLayoutManager);
            //La línea que divide los elementos
            recyclerViewExistencias.addItemDecoration(new DividerItemDecoration(PedidoEditar.this ,LinearLayoutManager.VERTICAL));
            //El adaptador que se encarga de toda la lógica
            recyclerViewExistencias.setAdapter(adaptadorRecyclerViewE);
            //adaptadorRecyverView.agregarPedido(new Pedido());



            spUni= findViewById(R.id.spnUni);
            ArrayAdapter<String> adaptador;
            adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,listaPrecios);
            spUni.setAdapter(adaptador);

          //  int seleccion=spUni.getSelectedItemPosition();
            // ca=adaptador.getItem(seleccion).toString();

            spUni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    unitario=(String) spUni.getItemAtPosition(position);
                    unitario=getNumeros(unitario);
                    edUnit.setText(unitario);
                    cantidad=edCant.getText().toString();
                    unitario=edUnit.getText().toString();

                    total=String.valueOf(Double.valueOf(cantidad)* Double.valueOf(unitario));
                    tvTotal.setText( total);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            edUnit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try{
                        cantidad=edCant.getText().toString();
                        if(cantidad.equals("")){
                            cantidad="0";
                        }
                        unitario=edUnit.getText().toString();
                        if(unitario.equals("")){
                            unitario="0";
                        }
                        total=String.valueOf((double)Math.round ((Double.valueOf(cantidad)* Double.valueOf(unitario))*100d)/100);
                        tvTotal.setText( total);
                    }
                    catch (Exception e){
                        salir(e.getMessage());
                    }
                }
            });

            edCant.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
             /*       try{
                        cantidad=edCant.getText().toString();
                        if(cantidad.equals("")){
                            cantidad="0";
                        }

                        total=String.valueOf((double)Math.round ((Double.valueOf(cantidad)* Double.valueOf(unitario))*100d)/100);
                        tvTotal.setText( total);
                    }
                    catch (Exception e){
                        salir(e.getMessage());
                    }*/
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try{
                        cantidadN=edCant.getText().toString();
                        if(cantidadN.equals("")){
                            cantidadN="0";
                        }
                        unitario=edUnit.getText().toString();
                        if(unitario.equals("")){
                            unitario="0";
                        }
                        if (Double.parseDouble( cantidadN) <= Double.parseDouble( inventario)) {
                            total = String.valueOf((double) Math.round((Double.valueOf(cantidadN) * Double.valueOf(unitario)) * 100d) / 100);
                            tvTotal.setText(total);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Se Asigna la cantidad maxima disponible" + inventario+" no se puede facturar la cantidad digitada de "+cantidadN,Toast.LENGTH_LONG).show();
                            edCant.setText(inventario);
                            total = String.valueOf((double) Math.round((Double.valueOf(cantidadN) * Double.valueOf(unitario)) * 100d) / 100);
                            tvTotal.setText(total);
                        }
                    }
                    catch (Exception e){
                        salir(e.getMessage());
                    }
                }
            });

            btAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    codigo=tvCodigo.getText().toString();
                    nombre=tvNombre.getText().toString();
                    cantidad=edCant.getText().toString();
                    unitario=edUnit.getText().toString();
                    total=tvTotal.getText().toString();
                   //posicion=
                    try{
                        // adaptadorRecyclerView.actualizarPedido(Integer.parseInt(posicion) ,new Pedido(codigo, nombre, cantidad, unitario, total));
                        claseGlobal objEscritura=(claseGlobal)getApplicationContext();
                        objEscritura.setEstado("A");
                        objEscritura.setPos(Integer.parseInt( posicion));
                        objEscritura.setCodigo(codigo);
                        objEscritura.setNombre(nombre);
                        objEscritura.setCol(color);
                        objEscritura.setCantidad(Double.parseDouble(cantidad) );
                        objEscritura.setUnitario(Double.parseDouble(unitario));
                        objEscritura.setTotal(Double.parseDouble(total));


                        try{


                        }catch (Exception e){
                            Toast.makeText( v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                        catch(Exception e){salir("Error: "+ e.getMessage());}


                    finish();
                }
            });
            btEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        claseGlobal objEscritura = (claseGlobal) getApplicationContext();
                        objEscritura.setEstado("E");
                        objEscritura.setPos(Integer.parseInt(posicion));
                    }catch (Exception e){salir(e.getMessage());}
                    finish();
                }
            });

        }
        catch (Exception e){
            //Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            salir(e.getMessage());
        }

    }

    public static String getNumeros(String cadena){
        char []cadena_div=cadena.toCharArray();
        String d="";
        String n="";
        for (int i =0;i<cadena_div.length;i++){
            d=String.valueOf(cadena_div[i]) ;
                if(Character.isDigit(cadena_div[i])||d.equals(".")){
                    n+=cadena_div[i];
                }
/*
            if (d.equals(".")) {

                n+=cadena_div[i];
            }*/
        }
        return n;
    }
    public void salir(String e){
        LinearLayout linear = findViewById(R.id.linear_layout);

            AlertDialog.Builder alerta=new AlertDialog.Builder(this);
            alerta.setMessage(e)
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            //super.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo=alerta.create();
            titulo.setTitle("Salida");
            titulo.show();
            //finish();
            //super.onBackPressed();

    }
    public void consultarExistencias( String c) {
        try {
        String ConnectionResult = "";

          //  existenciaList = new ArrayList<Existencia>();

        claseGlobal objEscritura=(claseGlobal)getApplicationContext();
        claseGlobal objLectura=(claseGlobal)getApplicationContext();


            ConnectionStr conStr=new ConnectionStr();
            connect=conStr.connectionclasss();
            if (connect == null)
            {
                ConnectionResult = "Revisar tu conexion a internet!";
            }
            else {
                CallableStatement call = connect.prepareCall("{call sp_BuscaProductos (?,?)}");
                call.setString(1, objLectura.getC_punto_venta());
                call.setString(2, c);

                ResultSet rs = call.executeQuery();

                if (rs != null) {
                    while (rs.next()) {

                      //  existenciaList.add(new Existencia(rs.getString(10),rs.getString(11),rs.getString(13),rs.getString(16)));
                       bodega = (rs.getString(10));
                        nbodega = (rs.getString(11));
                        unidades = (rs.getString(13));
                        ubicacion = (rs.getString(16));

                        if (ubicacion.equals("Actual")){
                           inventario= unidades;
                        }
                        adaptadorRecyclerViewE.agregarExistencia(new Existencia(bodega,nbodega,unidades,ubicacion));
                    }

                    //adaptadorRecyclerViewE.agregarExistencia(new Existencia(existenciaList.,nbodega,unidades,ubicacion));

                }
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void consultarprecio(String c) {
        try {
            Precio precio = null;
            claseGlobal objLectura=(claseGlobal)getApplicationContext();
            preciosList = new ArrayList<Precio>();

            ConnectionStr conStr = new ConnectionStr();
            connect = conStr.connectionclasss();        // Connect to database

            CallableStatement call = connect.prepareCall("{call sp_BuscaPrecioProducto (?,?)}");
            call.setString(1, objLectura.getC_punto_venta());
            call.setString(2, c);

            ResultSet rs = call.executeQuery();

            if (rs!=null) {
                while (rs.next()) {
                    preciosList.add(new Precio(rs.getString("Tipo"),rs.getString("Precio")));

                }
            }
            obtenerLista();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void obtenerLista() {
        listaPrecios = new ArrayList<String>();
        //listaPuntos.add("Seleccione");
        for (int i = 0; i < preciosList.size(); i++) {
            listaPrecios.add(preciosList.get(i).getTipo() + " - " + preciosList.get(i).getPrecio());
        }
    }

}
