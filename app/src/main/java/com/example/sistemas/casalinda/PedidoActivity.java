package com.example.sistemas.casalinda;

import static java.lang.Math.ceil;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemas.casalinda.Utilidades.claseGlobal;
import com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerView;
import com.example.sistemas.casalinda.entidades.Pedido;
import com.example.sistemas.casalinda.interfaz.InterfazClickRecyclerView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class PedidoActivity extends AppCompatActivity {
    String  tip,cod,nomb,cant,cant1,unit,total,col,pvp,cuv,bod,pon,c;
    //region  variables clientes
    String c_tipo_tercero,c_cod_cliente,cliente, direccion,rciudad, ciudad,tel1,fax,c_vendedor,c_director,c_lider,c_zona_fac,c_lista_precios,c_codicion_pago,c_dcto_financiero;
   //endregion
    //region variables pedido
    String C_Empresa,
            N_Orden_Pedido,
            N_Orden_Cotiza,
            F_Orden,
            F_Recep_Espe,
            F_Recep_Real,
            P_Cod_Postal,
            R_Cod_Postal,
            R_Tel1,
            R_Fax,
            V_Por_Anticipo,
            V_Dias_Saldo,
            F_Pago_Anticipo_Prog,
            F_Pago_Saldo_Prog,
            N_Comentario,
            C_Funcionario,
            Estado_Orden,
            Estado_Proceso,
            V_Por_Descuento_Pie,
            V_Fletes,
            V_Manipulacion,
            F_Grabacion,
            C_Punto_Venta,
            C_Tipo_Factura,
            T_Con_RTF,
            C_Con_RTF,
            T_Con_ICA,
            C_Con_ICA,
            C_Campana,
            C_Tipo_Pago,
            Estado_Impresion,
            C_Fun_Pik,
            Estado_Reserva,
            Aprobado_Factu,
            C_Cat_Pedido,
            Estado_Factu,
            tipo_ped,
            Dato_Ref1,
            C_Tipo_Envio,
            C_Cod_Medio_Trans,
            premio3,
            premio5,
            Estado_Modifica;
//endregion variables pedido
    int posicion;
    int REQUEST_CODE;
    Connection connect;
    public static final String  t="Parametro";
    private final static int NOMBRE=1;
    private final static int PRODUCTO=2;

    int SOLICITO_UTILIZAR_CAMARA;
    private ZXingScannerView vistaescaner;
    EditText etCodigo,etNomb, etCant,etCedula;
    Button btnAgregar, btnGrabar, btnEscaner, btnNuevo;
    TextView textViewProforma, textViewFactura, textViewPedido,textViewCliente;
    RecyclerView recyclerViewPedidos;
    final AdaptadorRecyclerView adaptadorRecyclerView=new AdaptadorRecyclerView(new InterfazClickRecyclerView() {
        @Override

       public void onClick(View v, Pedido p) {
           // Toast.makeText(PedidoActivity.this, "Daniel es "+p.toString(),Toast.LENGTH_LONG).show();
        }
    });
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        //Permiso por el usuario
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},SOLICITO_UTILIZAR_CAMARA);

        btnAgregar=findViewById(R.id.btnAgregar);
        btnGrabar= findViewById(R.id.btnGuardar);
        btnEscaner=findViewById(R.id.btnEscaner);
        btnNuevo=findViewById(R.id.btnNuevo);
        recyclerViewPedidos =findViewById(R.id.pRecycler);

        etCodigo=findViewById(R.id.edtCodigo);
        //etNomb=findViewById(R.id.edtNombre);
        etCant=findViewById(R.id.edtCant);
        etCedula=findViewById(R.id.edtCedula);

        textViewProforma=findViewById(R.id.txtProforma);
        textViewFactura=findViewById(R.id.txtFactura);
        textViewPedido=findViewById(R.id.txtPedido);
        textViewCliente=findViewById(R.id.txtCliente);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(createHelperCallback());
       // itemTouchHelper.attachToRecyclerView(recyclerViewPedidos);

       //Configuramos cómo se va a organizar las vistas dentro del RecyclerView; simplemente con un LinearLayout para que
        //aparezcan una debajo de otra
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(PedidoActivity.this  );
        recyclerViewPedidos.setLayoutManager(linearLayoutManager);
        //La línea que divide los elementos
        recyclerViewPedidos.addItemDecoration(new DividerItemDecoration(PedidoActivity.this ,LinearLayoutManager.VERTICAL));
        //El adaptador que se encarga de toda la lógica
        recyclerViewPedidos.setAdapter(adaptadorRecyclerView);
        //adaptadorRecyverView.agregarPedido(new Pedido());

        etCedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    consultaCliente(etCedula.getText().toString());
                    if(etCant.toString().isEmpty())
                    {
                        textViewCliente.setText("");
                    }
                    if(cliente!=null) {
                        textViewCliente.setText(cliente);
                    }else
                    {
                        textViewCliente.setText("");
                    }
                }
                catch (Exception e){
                    salir(e.getMessage());
                }
            }
        });

        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (adaptadorRecyclerView.getItemCount()>0&& textViewPedido.getText().toString()==""){
                        nuevo("Pedido sin guardar desea Guardarlos");
                    }
                    else {
                        nuevop();
                    }
                }catch (Exception e){salir(e.getMessage());}
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   String codigo = etCodigo.getText().toString();
                   String ca=etCant.getText().toString() ;
                    String dato="";

                   if (codigo.isEmpty()|ca.isEmpty()) {
                       //Toast.makeText(PedidoActivity.this, "Rellena los campos", Toast.LENGTH_SHORT).show();
                       salirp("Producto","Rellene los campos",1);
                       return;
                   }else {
                            if (Double.parseDouble(ca) <=0.00)
                            {
                                salirp("Ponga una cantidad","mayor a 0",1);
                            }
                            else{
                                   consultarproducto(codigo,Integer.parseInt(ca) );
                                   if (cod.isEmpty() | cant.isEmpty()) {
                                       etCodigo.setText("");
                                       etCodigo.setHint("Codigo No existe");
                                       etCant.setText("");
                                       etCant.setHint("Cant.");
                                       salirp("Producto no Existe","en punto de venta actual",1);

                                   } else {
                                       if (Double.valueOf(cant1) >=Double.valueOf(cant)) {
                                           int registros =adaptadorRecyclerView.getItemCount();
                                            int a=0;
                                            int d=0;
                                            int pa=0;
                                            Double c=0.00;
                                            Double u=0.00;
                                           while ( a<registros)
                                           {
                                               dato=adaptadorRecyclerView.getPedidos().get(a).getCodigo();
                                               c=Double.parseDouble( adaptadorRecyclerView.getPedidos().get(a).getCantidad());
                                               u=Double.parseDouble(adaptadorRecyclerView.getPedidos().get(a).getUnitario());
                                               //dato=adaptadorRecyclerView;
                                                if(cod.equals( dato)){
                                                    d=d+1;
                                                    pa=a;
                                                    cant=String.valueOf(c+Integer.parseInt(cant)) ;
                                                    total=String.valueOf( (double)Math.round ((u*Double.valueOf(cant))*100d)/100) ;
                                                }
                                                   a++;
                                           }
                                           if (d==1){
                                               if (Double.valueOf( cant)<=Double.valueOf( cant1)){

                                                   adaptadorRecyclerView.actualizarPedido(pa, new Pedido(tip,col, cod, nomb, cant, unit, total,pvp,cuv,bod,pon));
                                               }
                                                   else
                                                   {
                                                       Toast.makeText(getApplicationContext(),"No se puede facturar la cantidad digitada de "+cant+" Solo existen: "+cant1+" unidades.",Toast.LENGTH_LONG).show();
                                                       //zaadaptadorRecyclerView.actualizarPedido(pa, new Pedido(cod, nomb, cant1, unit, total));
                                                   }

                                               textViewProforma.setText(String.valueOf((double) Math.round(((adaptadorRecyclerView.tot)) * 100d) / 100));
                                               textViewFactura.setText(String.valueOf((double) Math.round((((adaptadorRecyclerView.tot)) * 1.12) * 100d) / 100));
                                               etCodigo.requestFocus();
                                           }else {
                                               adaptadorRecyclerView.agregarPedido(new Pedido(tip,col, cod, nomb, cant, unit, total,pvp,cuv,bod,pon));
                                               etCodigo.requestFocus();
                                                }
                                           //cod=adaptadorRecyclerView.getPedidos().get(i ).getCodigo()

                                           etCodigo.setText("");
                                           etCodigo.setHint("Código de Producto");
                                           etCant.setText("");
                                           etCant.setHint("Cant.");
                                           textViewProforma.setText(String.valueOf((double) Math.round(((adaptadorRecyclerView.tot)) * 100d) / 100));
                                           textViewFactura.setText(String.valueOf((double) Math.round((((adaptadorRecyclerView.tot)) * 1.12) * 100d) / 100));
                                       }
                                       else {
                                           if (Double.valueOf(cant1) > 0) {
                                               //Toast.makeText(PedidoActivity.this, "Ingrese una cantidad Menor o igual a " + cant1, Toast.LENGTH_LONG).show();
                                               salirp("Ingrese una cantidad menor o igual a "+cant1,"para registrar dicho codigo",1);
                                               cant="";
                                               etCant.setText("");
                                               etCant.setHint("Cant.");
                                               etCant.requestFocus();
                                           }
                                           else
                                           {
                                               //Toast.makeText(PedidoActivity.this, "No hay existencia, digite otro producto" , Toast.LENGTH_LONG).show();
                                               salirp("Sin Existencias","Digite otro producto",1);
                                               etCodigo.setText("");
                                               etCodigo.setHint("Codigo No existe");
                                               etCant.setText("");
                                               etCant.setHint("Cant.");
                                               etCodigo.requestFocus();
                                           }
                                       }
                                   }
                   }
                   }
               } catch (Exception e){
                   Toast.makeText(PedidoActivity.this,"Error ONCREATE: "+e.getMessage() ,Toast.LENGTH_LONG).show();
               }

                }

        });
        btnEscaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    escaner();
                }catch (Exception e){
                    Toast.makeText(PedidoActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               grabar();
            }
        });



    }


    public void onResume() {
        super.onResume();
        final TextView textViewProforma=findViewById(R.id.txtProforma);
        final TextView textViewFactura=findViewById(R.id.txtFactura);
        claseGlobal objLectura = (claseGlobal) getApplicationContext();
        try {
                if(objLectura.getPos()!=null) {
                    switch (objLectura.getEstado()){
                        case "A":
                            posicion = objLectura.getPos();
                            cod = objLectura.getCodigo();
                            nomb = objLectura.getNombre();
                           col=objLectura.getCol();
                            cant = objLectura.getCantidad().toString();
                            unit = objLectura.getUnitario().toString();
                            total = objLectura.getTotal().toString();
                            adaptadorRecyclerView.actualizarPedido(posicion, new Pedido(tip,col, cod, nomb, cant, unit, total,pvp,cuv,bod,pon));
                            textViewProforma.setText (adaptadorRecyclerView.tot.toString());
                            textViewFactura.setText(String.valueOf((double) Math.round((((adaptadorRecyclerView.tot)) * 1.12) * 100d) / 100));
                            break;
                        case "E":
                            posicion = objLectura.getPos();
                            adaptadorRecyclerView.eliminar(posicion);
                            textViewProforma.setText (adaptadorRecyclerView.tot.toString());
                            textViewFactura.setText(String.valueOf((double) Math.round((((adaptadorRecyclerView.tot)) * 1.12) * 100d) / 100));
                            break;
                    }

                }
        }catch (Exception e){
            //Toast.makeText(PedidoActivity.this,"ERROR ON RESUME:"+ e.getMessage(),Toast.LENGTH_LONG).show();
            }
    }
    public void nuevop(){
        etCodigo.setText("");
        etCant.setText("");
        etCedula.setText("");
        etCedula.setEnabled(true);
        etCodigo.setEnabled(true);
        etCant.setEnabled(true);
        textViewCliente.setText("");
        textViewFactura.setText("");
        textViewPedido.setText("");
        textViewProforma.setText("");
        btnEscaner.setEnabled(true);
        btnAgregar.setEnabled(true);
        btnGrabar.setEnabled(true);

        adaptadorRecyclerView.eliminarTodo();
    }
    public void grabar(){


        try {
            claseGlobal objLectura=(claseGlobal)getApplicationContext();
            String Ced = etCedula.getText().toString();
            if (Ced.isEmpty()) {
                //Toast.makeText(PedidoActivity.this, "Digite cédula cliente", Toast.LENGTH_SHORT).show();
                salirp("Documento de identidad","Digite Cedula de cliente",1);
                return;
            } else {
                consultaCliente(Ced);
                if (c_cod_cliente.isEmpty()) {
                    //Toast.makeText(PedidoActivity.this, "Cliente no Existe", Toast.LENGTH_SHORT).show();
                    salirc("Cliente no Existe, Desea crearlo?");
                } else if(adaptadorRecyclerView.getItemCount()== 0){
                    salirp("Registro","No existen productos ingresados para generar un pedido, favor ingrese productos",1);
                        }
                        else {
                            grabaPedido(objLectura.getCod_pedidos());
                            textViewPedido.setText(N_Orden_Pedido);
                            btnAgregar.setEnabled(false);
                            btnEscaner.setEnabled(false);
                            btnGrabar.setEnabled(false);
                            etCedula.setEnabled(false);
                            etCodigo.setEnabled(false);
                            etCant.setEnabled(false);
                            btnNuevo.setEnabled(true);
                            recyclerViewPedidos.setEnabled(false);
                            //adaptadorRecyclerView.eliminarTodo();
                        }
            }
        }

        catch(Exception e){
            salir(e.getMessage());
        }
    }
    public void salirp(String t,String m,int a){
        LinearLayout linear = findViewById(R.id.linear_layout);

        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage(m)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (a) {
                            case 1:
                                dialog.cancel();
                                break;
                            case 2:
                                finish();
                                break;


                        }
                    }
                })
        ;
        AlertDialog titulo=alerta.create();
        titulo.setTitle(t);
        titulo.show();
        //finish();
        //super.onBackPressed();

    }
    public void obtenerNPedido(String c){
        String ConnectionResult = "";
        claseGlobal objEscritura=(claseGlobal)getApplicationContext();
        claseGlobal objLectura=(claseGlobal)getApplicationContext();
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guayaquil"));
        SimpleDateFormat fecha=new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fechaH=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today=new Date();
        String Current=fechaH.format(today);
        try {
            ConnectionStr conStr=new ConnectionStr();
            connect=conStr.connectionclasss();
            if (connect == null)
            {
                ConnectionResult = "Revisar tu conexion a internet!";
                Toast.makeText(getApplicationContext(),ConnectionResult,Toast.LENGTH_LONG).show();
            }
            else {
                CallableStatement call=connect.prepareCall("{call sp_obtenerNPedido (?)}");
                call.setString(1, c);

                ResultSet rs=call.executeQuery();


                if (rs.next()) {
                    C_Empresa="3";
                    N_Orden_Pedido=(rs.getString(1));
                    F_Orden= fecha.format(new Date());
                    F_Recep_Espe=fecha.format(new Date());
                    F_Grabacion=Current;
                }
                else
                {
                    salir("No se genero ningun Numero de pedido");
                    C_Empresa="";
                    N_Orden_Pedido="";
                    N_Orden_Cotiza="";
                    F_Orden= fecha.format(new Date());
                    F_Recep_Espe=fecha.format(new Date());
                    F_Recep_Real="";
                    P_Cod_Postal="";
                    R_Cod_Postal="";
                    R_Tel1="";
                    R_Fax="";
                    V_Por_Anticipo="";
                    V_Dias_Saldo="";
                    F_Pago_Anticipo_Prog="";
                    F_Pago_Saldo_Prog="";
                    N_Comentario="";
                    C_Funcionario="";
                    Estado_Orden="";
                    Estado_Proceso="";
                    V_Por_Descuento_Pie="";
                    V_Fletes="";
                    V_Manipulacion="";
                    F_Grabacion="";
                    C_Punto_Venta="";
                    C_Tipo_Factura="";
                    T_Con_RTF="";
                    C_Con_RTF="";
                    T_Con_ICA="";
                    C_Con_ICA="";
                    C_Campana="";
                    C_Tipo_Pago="";
                    Estado_Impresion="";
                    C_Fun_Pik="";
                    Estado_Reserva="";
                    Aprobado_Factu="";
                    C_Cat_Pedido="";
                    Estado_Factu="";
                    tipo_ped="";
                    Dato_Ref1="";
                    C_Tipo_Envio="";
                    C_Cod_Medio_Trans="";
                    premio3="";
                    premio5="";
                    Estado_Modifica="";

                }
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
salir(e.getMessage());
        }
    }
    public void grabaPedido(String c){
        claseGlobal objLectura=(claseGlobal)getApplicationContext();
        //GrabaEncaPedido
        try{
             obtenerNPedido(c);
             grabaPedidoEnca(c);
             grabaPedidoDeta(N_Orden_Pedido);
            salirp("Pedido No.: "+N_Orden_Pedido," Grabado correctamente",1);

        }
        catch (Exception e){
            salir(e.getMessage());
        }
        //GrabaDetaPedido
        try{

        }
        catch (Exception e){
            salir(e.getMessage());
        }
    }
    public void grabaPedidoEnca(String p){
        String ConnectionResult = "";
        claseGlobal objEscritura=(claseGlobal)getApplicationContext();
        claseGlobal objLectura=(claseGlobal)getApplicationContext();

        try {
            ConnectionStr conStr=new ConnectionStr();
            connect=conStr.connectionclasss();
            if (connect == null)
            {
                ConnectionResult = "Revisar tu conexion a internet!";
                Toast.makeText(getApplicationContext(),ConnectionResult,Toast.LENGTH_LONG).show();
            }
            else {
                //Guarda Pedido Enca
                CallableStatement call=connect.prepareCall("{call sp_insertPedidoEnca (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                call.setString(1, "03");//C_Empresa
                call.setString(2, N_Orden_Pedido);//N_Orden_Pedido
                call.setString(3, "");//N_Orden_Cotiza
                call.setString(4, F_Orden);//F_Orden
                call.setString(5, F_Recep_Espe);//F_Recep_Espe
                call.setString(6, "");//F_Recep_Real
                call.setString(7, c_tipo_tercero);//C_Tipo_Tercero
                call.setString(8, c_cod_cliente);//C_Cod_Cliente
                call.setString(9, direccion);//P_Direccion_Cliente
                call.setString(10, ciudad);//P_C_Ciudad
                call.setString(11, "PdV");//P_Cod_Postal
                call.setString(12, tel1);//P_Tel1
                call.setString(13, fax);//P_Fax
                call.setString(14, direccion);//R_Direccion
                call.setString(15, rciudad);//R_C_Ciudad
                call.setString(16, "");//R_Cod_Postal
                call.setString(17, tel1);//R_Tel1
                call.setString(18, fax);//R_Fax
                call.setString(19, "0");//V_Por_Anticipo
                call.setString(20, "0");//V_Dias_Saldo
                call.setString(21, F_Orden);//F_Pago_Anticipo_Prog
                call.setString(22,F_Orden);//F_Pago_Saldo_Prog
                call.setString(23, "Grabado aplicacion movil local");//N_Comentario
                call.setString(24, objLectura.getC_funcionario());//C_Funcionario
                call.setString(25, "1");//Estado_Orden
                call.setString(26, "1");//Estado_Proceso
                call.setString(27, "0");//V_Por_Descuento_Pie
                call.setString(28, "0");//V_Fletes
                call.setString(29, "0");//V_Manipulacion
                call.setString(30, F_Grabacion);//F_Grabacion
                call.setString(31, objLectura.getC_punto_venta());//C_Punto_Venta
                call.setString(32, c_vendedor);//C_Vendedor
                call.setString(33, c_director);//C_Director
                call.setString(34, c_zona_fac);//C_Zona_Fac
                call.setString(35, c_lista_precios);//C_Lista_Precios
                call.setString(36, c_codicion_pago);//C_Condicion_Pago
                call.setString(37, "GRV");//C_Tipo_Factura
                call.setString(38,c_dcto_financiero);//C_Dcto_Financiero
                call.setString(39, "G");//T_Con_RTF
                call.setString(40, "312");//C_Con_RTF
                call.setString(41, "C");//T_Con_ICA
                call.setString(42, "999");//C_Con_ICA
                call.setString(43, "1");//C_Campana
                call.setString(44, c_lider);//C_Lider
                call.setString(45, "C");//C_Tipo_Pago
                call.setString(46, "S");//Estado_Impresion
                call.setString(47, "999");//C_Fun_Pik
                call.setString(48, "S");//Estado_Reserva
                call.setString(49, "S");//Aprobado_Factu
                call.setString(50, "1");//C_Cat_Pedido
                call.setString(51, "N");//Estado_Factu
                call.setString(52, "N");//tipo_ped
                call.setString(53, "");//Dato_Ref1
                call.setString(54, "E");//C_Tipo_Envio
                call.setString(55, "10");//C_Cod_Medio_Trans
                call.setString(56, "");//premio3
                call.setString(57, "");//premio5
                call.setString(58, "N");//Estado_Modifica
                call.setString(59,objLectura.getC_bodega());
                call.setString(60,"F");//Tipo_Factura
                call.execute();


            }


        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
    public void grabaPedidoDeta(String p){
        String ConnectionResult = "";
        claseGlobal objEscritura=(claseGlobal)getApplicationContext();
        claseGlobal objLectura=(claseGlobal)getApplicationContext();

        try {
            ConnectionStr conStr=new ConnectionStr();
            connect=conStr.connectionclasss();
            if (connect == null)
            {
                ConnectionResult = "Revisar tu conexion a internet!";
                Toast.makeText(getApplicationContext(),ConnectionResult,Toast.LENGTH_LONG).show();
            }
            else {

                int registros =adaptadorRecyclerView.getItemCount();
                //int a=0;

                //while ( a<registros)
                for (int a=0;a<registros;a++)
                {

                    CallableStatement call = connect.prepareCall("{call sp_insertPedidoDeta (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                    call.setInt(1, 3);//C_Empresa
                    call.setString(2, N_Orden_Pedido);//N_Orden_Pedido
                    call.setString(3, adaptadorRecyclerView.getPedidos().get(a).getTipo());//T_Elemento
                    call.setString(4, adaptadorRecyclerView.getPedidos().get(a).getCodigo());//C_Item
                    call.setString(5, adaptadorRecyclerView.getPedidos().get(a).getColor());//C_Despieze2
                    call.setDouble(6,  Float.parseFloat( adaptadorRecyclerView.getPedidos().get(a).getCantidad()));//V_Cantidad_Orden
                    call.setDouble(7, Float.parseFloat( String.valueOf(ceil((double)Math.round(Double.parseDouble( adaptadorRecyclerView.getPedidos().get(a).getUnitario())*100d)/100))));//V_Valor_Und
                    call.setDouble(8, 0);//V_Por_Descuento
                    call.setDouble(9, 12);//V_Por_Impuesto
                    call.setString(10, F_Orden);//F_Recep_Espe_Item
                    call.setString(11, F_Orden);//F_Recep_Real_Item
                    call.setFloat(12, 0);//V_Cant_Recepcion
                    call.setFloat(13,0);//V_Cant_Devolucion
                    call.setString(14,  adaptadorRecyclerView.getPedidos().get(a).getCuv()) ;//C_Und_Venta
                    call.setDouble(15, Float.parseFloat( adaptadorRecyclerView.getPedidos().get(a).getCantidad()));//Cant_Desp1
                    call.setDouble(16, 0);//Cant_Desp2
                    call.setDouble(17,0);//Cant_Desp3
                    call.setDouble(18, 0);//Cant_Desp4
                    call.setDouble(19, 0);//Cant_Desp5
                    call.setDouble(20,0);//Cant_Desp6
                    call.setDouble(21, 0);//Cant_Desp7
                    call.setDouble(22, 0);//Cant_Desp8
                    call.setDouble(23, 0);//Cant_Desp9
                    call.setDouble(24, 0);//Cant_Desp10
                    call.setDouble(25, 0);//Cant_Remis1
                    call.setDouble(26, 0);//Cant_Remis2
                    call.setDouble(27, 0);//Cant_Remis3
                    call.setDouble(28, 0);//Cant_Remis4
                    call.setDouble(29, 0);//Cant_Remis5
                    call.setDouble(30,0);//Cant_Remis6
                    call.setDouble(31, 0);//Cant_Remis7
                    call.setDouble(32, 0);//Cant_Remis8
                    call.setDouble(33, 0);//Cant_Remis9
                    call.setDouble(34,0);//Cant_Remis10
                    call.setDouble(35, 0);//Cant_Devol1
                    call.setDouble(36, 0);//Cant_Devol2
                    call.setDouble(37, 0);//Cant_Devol3
                    call.setDouble(38, 0);//Cant_Devol4
                    call.setDouble(39, 0);///Cant_Devol5
                    call.setDouble(40,0);///Cant_Devol6
                    call.setDouble(41,0);///Cant_Devol7
                    call.setDouble(42, 0);///Cant_Devol8
                    call.setDouble(43,0);///Cant_Devol9
                    call.setDouble(44, 0);///Cant_Devol10
                    call.setDouble(45, 0);///V_Por_Descuento_Pie
                    call.setDouble(46,  Float.parseFloat( String.valueOf(ceil((double)Math.round(Double.parseDouble( adaptadorRecyclerView.getPedidos().get(a).getTotal())*100d)/100))));//Vlr_Bruto
                    call.setDouble(47, 0);///Vlr_Dcto
                    call.setDouble(48,  Float.parseFloat( String.valueOf(ceil((double)Math.round(Double.parseDouble( adaptadorRecyclerView.getPedidos().get(a).getTotal())*100d)/100))));//Vlr_Neto
                    call.setDouble(49, 0);///Vlr_Dcto_Pie
                    call.setDouble(50,  Float.parseFloat( String.valueOf(ceil((double)Math.round(Double.parseDouble( adaptadorRecyclerView.getPedidos().get(a).getTotal())*100d)/100))));//Vlr_Neto_Final
                    call.setDouble(51,  Float.parseFloat( String.valueOf(ceil((double)Math.round((Double.parseDouble( adaptadorRecyclerView.getPedidos().get(a).getTotal())*0.12)*100d)/100))));///Vlr_Impto
                    call.setDouble(52,  Float.parseFloat( String.valueOf(ceil((double)Math.round((Double.parseDouble( adaptadorRecyclerView.getPedidos().get(a).getTotal())*1.12)*100d)/100))));///Vlr_Total
                    call.setString(53, adaptadorRecyclerView.getPedidos().get(a).getBod());//C_Bodega
                    call.setString(54, "S");//Estado_Disponible
                    call.setString(55, "1");//C_Cat_Activo
                    call.setString(56, "1");//C_Cat_Item
                    call.setDouble(57,  Float.parseFloat(String.valueOf(ceil((double)Math.round((Double.parseDouble( adaptadorRecyclerView.getPedidos().get(a).getPvp())*100d)/100)))));//V_Pvp
                    call.setString (58,adaptadorRecyclerView.getPedidos().get(a).getPon());//costoPonderado
                    call.execute();

                }

            }

    }catch(Exception e){
            salir(e.getMessage()+" CAUSA: "+e.getCause()+" Localize"+e.getLocalizedMessage()+" Trace:"+e.getStackTrace()+" Supress:"+e.getSuppressed()+" Class:"+e.getClass());
        Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

    }
    }
    public void consultaCliente(String c){
        String ConnectionResult = "";
        claseGlobal objEscritura=(claseGlobal)getApplicationContext();
        claseGlobal objLectura=(claseGlobal)getApplicationContext();
        try {
            ConnectionStr conStr=new ConnectionStr();
            connect=conStr.connectionclasss();
            if (connect == null)
            {
                ConnectionResult = "Revisar tu conexion a internet!";
                Toast.makeText(getApplicationContext(),ConnectionResult,Toast.LENGTH_LONG).show();
            }
            else {
                CallableStatement call=connect.prepareCall("{call sp_BuscaCliente (?)}");
                call.setString(1, c);

                ResultSet rs=call.executeQuery();


                if (rs.next()) {
                    c_tipo_tercero=(rs.getString(1));
                    c_cod_cliente=(rs.getString(2));
                    cliente=(rs.getString(3));
                    direccion=(rs.getString(4));
                    ciudad=(rs.getString(5));
                    tel1=(rs.getString(6));
                    fax=(rs.getString(7));
                    c_vendedor=(rs.getString(8));
                    c_director=(rs.getString(9));
                    c_lider=(rs.getString(10));
                    c_zona_fac=(rs.getString(11));
                    c_lista_precios=(rs.getString(12));
                    c_codicion_pago=(rs.getString(13));
                    c_dcto_financiero=(rs.getString(14));
                    rciudad=(rs.getString(15));
                }
                else
                {
                    c_tipo_tercero=("");
                    c_cod_cliente=("");
                    cliente=("");
                    direccion=("");
                    ciudad=("");
                    tel1=("");
                    fax=("");
                    c_vendedor=("");
                    c_director=("");
                    c_lider=("");
                    c_zona_fac=("");
                    c_lista_precios=("");
                    c_codicion_pago=("");
                    c_dcto_financiero=("");
                }
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
    public void consultarproducto(String c, int ca){
        String ConnectionResult = "";
        claseGlobal objEscritura=(claseGlobal)getApplicationContext();
        claseGlobal objLectura=(claseGlobal)getApplicationContext();

        try {
            ConnectionStr conStr=new ConnectionStr();
            connect=conStr.connectionclasss();
            if (connect == null)
            {
                ConnectionResult = "Revisar tu conexion a internet!";
                Toast.makeText(getApplicationContext(),ConnectionResult,Toast.LENGTH_LONG).show();
            }
            else {
                CallableStatement call=connect.prepareCall("{call sp_BuscaProducto (?,?)}");
                call.setString(1, objLectura.getC_punto_venta());
                call.setString(2, c);

                ResultSet rs=call.executeQuery();


                if (rs.next()) {
                    tip=(rs.getString(1));
                    cod=(rs.getString(2));
                    nomb=(rs.getString(3));
                    if(((double)Math.round( Double.valueOf( rs.getString(8))*100d)/100)==0){
                        unit= String.valueOf (ceil((double)Math.round( Double.valueOf( rs.getString(12))*100d)/100));
                    }else{
                        unit= String.valueOf (ceil((double)Math.round( Double.valueOf( rs.getString(8))*100d)/100));
                    }
                    bod=(rs.getString(10));
                    cant1=(rs.getString(13));
                    cant=String.valueOf(ca);
                    total=String.valueOf( ceil((double)Math.round ((Double.valueOf(unit)*Double.valueOf(cant))*100d)/100) );
                    col=(rs.getString(17));
                    pvp=(rs.getString(18));
                    cuv=(rs.getString(19));
                    pon=(rs.getString(20));

                }
                else{
                    tip="";
                    cod="";
                    nomb="";
                    unit="";
                    bod="";
                    cant="";
                    total="0.00";
                    col="";
                    pvp="";
                    cuv="";
                    pon="";

                }


            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
    //Mensaje Cliente
    public void nuevo(String e){
        LinearLayout linear = findViewById(R.id.linear_layout);

        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage(e)
                .setCancelable(false)

                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        grabar();
                        nuevop();
                        //dialog.cancel();
                        // finish();
                        //super.finish();
                    }
                })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nuevop();
                    }
                });
        AlertDialog titulo=alerta.create();
        titulo.setTitle("Salida");
        titulo.show();
        //finish();
        //super.onBackPressed();

    }
    public void salir(String e){
        LinearLayout linear = findViewById(R.id.linear_layout);

        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage(e)
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // finish();
                        //super.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
                );
        AlertDialog titulo=alerta.create();
        titulo.setTitle("Salida");
        titulo.show();
        //finish();
        //super.onBackPressed();

    }
    public void salirc(String e){
        LinearLayout linear = findViewById(R.id.linear_layout);

        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage(e)
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent( PedidoActivity.this, ClienteActivity.class );
                        intent.putExtra("d0","1");
                        intent.putExtra("d1",etCedula.getText().toString() );
                        startActivity(intent);
                        // finish();
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
    //Validar atras
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {




        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        // Esto es lo que hace mi botón al pulsar ir a atrás

            //Toast.makeText(getApplicationContext(), "Voy hacia atrás!!",     Toast.LENGTH_SHORT).show();
            //onBackPressed();

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Quiere salir del pedido");
            builder.setTitle("Confirmación");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setCancelable(false)
            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                     dialog.cancel();
                }
             });

            builder.create();
            builder.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
   //METODO PARA ESCANEAR
    public void escaner(){
        IntentIntegrator intent =new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);

        intent.setPrompt("ESCANEAR CODIGO")
                .setCameraId(0)
                .setOrientationLocked(false)
                .setBeepEnabled(false)
                .setCaptureActivity(CaptureActivityPortrait.class)
                .setBarcodeImageEnabled(false)
                .initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        etCodigo=findViewById(R.id.edtCodigo);
        IntentResult result =IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!=null){
            if (result.getContents()==null){
                Toast.makeText(this,"Cancelaste el escaneo", Toast.LENGTH_LONG).show();

            }else   {
                etCodigo.setText(result.getContents());
                etCant.requestFocus();
            }

        }else {
            super.onActivityResult(requestCode,resultCode,data);
        }
        if (resultCode==RESULT_CANCELED){
            // Si es así mostramos mensaje de cancelado por pantalla.
            Toast.makeText(this, "Resultado cancelado", Toast.LENGTH_SHORT).show();
        }else {
            // De lo contrario, recogemos el resultado de la segunda actividad.
            String resultado = data.getExtras().getString("RESULTADO");
            // Y tratamos el resultado en función de si se lanzó para rellenar el
            // nombre o el apellido.
            switch (requestCode) {
                case NOMBRE:
                    etCedula.setText(resultado);
                    break;
                case PRODUCTO:
                    etCodigo.setText(resultado);
                    break;
            }
        }
    }
    private ItemTouchHelper.Callback createHelperCallback(){
        ItemTouchHelper.SimpleCallback simpleCallback=
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP| ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        final TextView textViewProforma=findViewById(R.id.txtProforma);
                        final TextView textViewFactura=findViewById(R.id.txtFactura);
                        final TextView textViewPedido=findViewById(R.id.txtPedido);

                            try {
                                deleteItem(viewHolder.getAdapterPosition());
                                if (adaptadorRecyclerView.tot == 0) {
                                    textViewProforma.setText("0.00");
                                    textViewFactura.setText("0.00");

                                } else {
                                    textViewProforma.setText( String.valueOf(ceil( (double) Math.round(((adaptadorRecyclerView.tot)) * 100d) / 100)));
                                    textViewFactura.setText(String.valueOf(ceil((double) Math.round((((adaptadorRecyclerView.tot)) * 1.12) * 100d) / 100)));
                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                    }
                };
        return simpleCallback;
    }
    private void deleteItem(final int position){
        adaptadorRecyclerView.eliminar(position);

    }
    // Método que se ejecuta al pulsar el botón btNombre:
    public void rellenarNombre(View v) {
        Intent i = new Intent(this, BuscaNombreActivity.class);
        // Iniciamos la segunda actividad, y le indicamos que la iniciamos
        // para rellenar el nombre:
        i.putExtra(t,"1");
        startActivityForResult(i, NOMBRE);
    }
    // Método que se ejecuta al pulsar el botón btApellido
    public void rellenarProducto(View v) {
        Intent i = new Intent(this, BuscaNombreActivity.class);
        // Iniciamos la segunda actividad, y le indicamos que la iniciamos
        // para rellenar el apellido:
        i.putExtra(t,"2");
        startActivityForResult(i, PRODUCTO);
    }
}
