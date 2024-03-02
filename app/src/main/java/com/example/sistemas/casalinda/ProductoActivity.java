package com.example.sistemas.casalinda;

import static com.example.sistemas.casalinda.R.layout.activity_producto;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.sistemas.casalinda.Utilidades.claseGlobal;
import com.google.zxing.Result;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ProductoActivity extends AppCompatActivity {
    EditText codigo;
    Button btnTraer;
    private ZXingScannerView vistaescaner;
    Connection connect;
    TextView referencia,nombre,costo,promedio, mayor,mayorista,negocio,publico,milagro, cantidad;
    int SOLICITO_UTILIZAR_CAMARA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( activity_producto);

        //Permiso por el usuario
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},SOLICITO_UTILIZAR_CAMARA);

        referencia=(TextView)findViewById(R.id.txtCodigo);
        nombre=(TextView)findViewById(R.id.txtNombre);
        costo=(TextView)findViewById(R.id.txtCosto);
        codigo=(EditText)findViewById(R.id.edtCodigo);
        btnTraer=(Button)findViewById(R.id.btnTraer);
        btnTraer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarproducto(codigo.toString());
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    public void Escanear(View view){
        vistaescaner=new ZXingScannerView(this);
        vistaescaner.setResultHandler(new zxingscanner());
        setContentView(vistaescaner);
        vistaescaner.startCamera();
    }
    class zxingscanner implements ZXingScannerView.ResultHandler{
        @Override
        public void handleResult(Result result){
            String dato=result.getText();
            setContentView(activity_producto);
            vistaescaner.stopCamera();
            codigo=(EditText)findViewById(R.id.edtCodigo);
            codigo.setText(dato);
            consultarproducto(codigo.toString());
        }
    }

    public void consultarproducto( String c){
        String ConnectionResult = "";
        codigo=(EditText)findViewById(R.id.edtCodigo);
        promedio=(TextView) findViewById(R.id.txtPromedio);
        mayorista=(TextView)findViewById(R.id.txtMayorista);
        mayor=(TextView)findViewById(R.id.txtMayor);
        negocio=(TextView)findViewById(R.id.txtNegocio);
        publico=(TextView)findViewById(R.id.txtPublico);
        milagro=(TextView)findViewById(R.id.txtMilagro);
        cantidad=(TextView)findViewById(R.id.txtCantidad);

        claseGlobal objEscritura=(claseGlobal)getApplicationContext();
        claseGlobal objLectura=(claseGlobal)getApplicationContext();

        try {
            ConnectionStr conStr=new ConnectionStr();
            connect=conStr.connectionclasss();
            if (connect == null)
            {
                ConnectionResult = "Revisar tu conexion a internet!";
            }
            else {
                CallableStatement call=connect.prepareCall("{call sp_BuscaProducto (?,?)}");
                call.setString(1,objLectura.getC_punto_venta().toString());
                call.setString(2,c.toString());

                ResultSet rs=call.executeQuery();


                if (rs.next()) {
                    referencia.setText(rs.getString(2));
                    nombre.setText(rs.getString(3));
                    costo.setText (rs.getString(4));
                    promedio.setText(rs.getString(12));
                    mayor.setText(rs.getString(5));
                    mayorista.setText(rs.getString(6));
                    negocio.setText(rs.getString(7));
                    publico.setText(rs.getString(8));
                    milagro.setText(rs.getString(9));
                    cantidad.setText(rs.getString(13));
                }
                else{

                    referencia.setText("");
                    nombre.setText("");
                    costo.setText ("");
                    promedio.setText("");
                    mayor.setText("");
                    mayorista.setText("");
                    negocio.setText("");
                    publico.setText("");
                    milagro.setText("");
                    cantidad.setText("");
                }
                //ET_Cedula.setText("");
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
