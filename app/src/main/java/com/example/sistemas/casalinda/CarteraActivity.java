package com.example.sistemas.casalinda;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CarteraActivity extends AppCompatActivity {
    Button btnConsultar;
    //View vista;
    TextView Cedula,Nombre,Saldo;
    EditText ET_Cedula;

    //formato decimal
    // currency = new DecimalFormat( "###,###.##" );
    // Declaring connection variables
    Connection connect;
    //String DBUserNameStr,DBPasswordStr,db,ip,UserNameStr,PasswordStr,ced;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cartera );
        Cedula= findViewById( R.id.txtCedula );
        Nombre= findViewById( R.id.txtNombre );
        Saldo= findViewById( R.id.txtSaldo );
        ET_Cedula= findViewById( R.id.edtCedula);


        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );



        btnConsultar= findViewById( R.id.btnConsultar );
        btnConsultar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //limpia cajas de texo
                Cedula.setText( "" );
                Nombre.setText( "" );
                Saldo.setText( "");
                consultarpersona();
            }
        } );


    }
    public void consultarpersona(){
        String ConnectionResult = "";
        try {
            ConnectionStr conStr=new ConnectionStr();
            connect=conStr.connectionclasss();
            if (connect == null)
            {
                ConnectionResult = "Revisar tu conexion a internet!";
            }
            else {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select C_Cod_Cliente, Cliente,ROUND(sum( Saldo_Neto),2)SaldoNeto from V_Fac_Facturas_Enca where (Estado_Proceso = 1) AND (Saldo_Bruto <> 0) and C_Cod_Cliente= '" + ET_Cedula.getText().toString() + "'  group by C_Cod_Cliente, Cliente  ORDER BY Cliente ");
                if (rs.next()) {
                    Cedula.setText(rs.getString(1));
                    Nombre.setText(rs.getString(2));
                    Saldo.setText(rs.getString(3));
                }
                ET_Cedula.setText("");
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

}
