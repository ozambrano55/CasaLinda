package com.example.sistemas.casalinda;

import static com.example.sistemas.casalinda.PedidoActivity.t;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemas.casalinda.Utilidades.claseGlobal;
import com.example.sistemas.casalinda.adaptadores.AdaptadorRecyclerViewB;
import com.example.sistemas.casalinda.entidades.Busca;
import com.example.sistemas.casalinda.interfaz.InterfazClickRecyclerViewB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class BuscaNombreActivity extends AppCompatActivity {
    Connection connect;
    String CODIGO, NOMBRE,CANTIDAD;
   // String NOMBRES,PRODUCTOS;
    //ArrayList<Busca>buscaList;
   String t1="Parametro";
   final AdaptadorRecyclerViewB adaptadorRecyclerViewB=new AdaptadorRecyclerViewB(new InterfazClickRecyclerViewB() {
       @Override
       public void onClick(View v, Busca b) {
           final TextView txcodigo=findViewById(R.id.txtCodigo);
           final TextView txnombre=findViewById(R.id.txtNombre);
           final TextView txtcantidad=findViewById(R.id.txtCantidad);

           txcodigo.setText(b.getCodigo());
           txnombre.setText(b.getNombre());
           txtcantidad.setText(b.getCantidad());

       }
   });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_nombre);

        final Button btnAceptar=findViewById(R.id.btAceptar);
        final Button btnCancelar=findViewById(R.id.btCancelar);
        final TextView txcodigo=findViewById(R.id.txtCodigo);
        final TextView txnombre=findViewById(R.id.txtNombre);
        final EditText etBuscar=findViewById(R.id.edtBuscar);
        claseGlobal objLectura=(claseGlobal)getApplicationContext();
        t1 = getIntent().getStringExtra(t);
        RecyclerView recyclerViewBusca =findViewById(R.id.pRecyclerB);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BuscaNombreActivity.this);
        recyclerViewBusca.setLayoutManager(linearLayoutManager);
        recyclerViewBusca.addItemDecoration(new DividerItemDecoration(BuscaNombreActivity.this, LinearLayoutManager.VERTICAL));

       // Intent i=getIntent();
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(etBuscar.getText().toString().isEmpty()){
                        adaptadorRecyclerViewB.eliminarTodo();
                        txcodigo.setText("");
                        txnombre.setText("");
                    }
                    else{
                        txcodigo.setText("");
                        txnombre.setText("");
                        adaptadorRecyclerViewB.eliminarTodo();
                    consultaClienteProducto(t1, etBuscar.getText().toString(),objLectura.getC_punto_venta().toString());

                    recyclerViewBusca.setAdapter(adaptadorRecyclerViewB);}

                }catch (Exception e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}
            }
        });
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si el Editext no está vació recogemos el resultado.
                if (txcodigo.getText().length() != 0) {
                    // Guardamos el texto del EditText en un String.
                    String resultado = txcodigo.getText().toString();
                    // Recogemos el intent que ha llamado a esta actividad.
                    Intent i = getIntent();
                    // Le metemos el resultado que queremos mandar a la
                    // actividad principal.
                    i.putExtra("RESULTADO", resultado);
                    // Establecemos el resultado, y volvemos a la actividad
                    // principal. La variable que introducimos en primer lugar
                    // "RESULT_OK" es de la propia actividad, no tenemos que
                    // declararla nosotros.
                    setResult(RESULT_OK, i);

                    // Finalizamos la Activity para volver a la anterior
                    finish();
                } else {
                    // Si no tenía nada escrito el EditText lo avisamos.
                    Toast.makeText(BuscaNombreActivity.this, "No se ha introducido nada en el campo de texto", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si se pulsa el botón, establecemos el resultado como cancelado.
                // Al igual que con "RESULT_OK", esta variable es de la activity.
                setResult(RESULT_CANCELED);

                // Finalizamos la Activity para volver a la anterior
                finish();
            }
        });
    }


    public void consultaClienteProducto(String t, String c,String p) {
        try {
            String ConnectionResult = "";


            ConnectionStr conStr=new ConnectionStr();
            connect=conStr.connectionclasss();
            if (connect == null)
            {
                ConnectionResult = "Revisar tu conexion a internet!";
            }
            else {
                CallableStatement call = connect.prepareCall("{call sp_BuscaCP (?,?,?)}");
                call.setString(1, t);
                call.setString(2,c);
                call.setString(3,p);
                ResultSet rs=call.executeQuery();

                if (rs != null) {
                    while (rs.next()) {

                        //  existenciaList.add(new Existencia(rs.getString(10),rs.getString(11),rs.getString(13),rs.getString(16)));
                        CODIGO=(rs.getString(1));
                        NOMBRE=(rs.getString(2));
                        CANTIDAD=(rs.getString(3));
                        adaptadorRecyclerViewB.agregarBusca(new Busca(CODIGO,NOMBRE,CANTIDAD));
                    }

                }
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
