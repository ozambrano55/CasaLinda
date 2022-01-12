package com.example.sistemas.casalinda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sistemas.casalinda.Utilidades.claseGlobal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity  {
    // Declaring layout button, edit texts
    Button btn_login;
    EditText ET_Username,ET_Password;
    ProgressBar progressBar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Connection connect;
    String UserNameStr,PasswordStr,c_punto_venta;
    //End Declaring connection variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Inicia   ndo sharedpreference
        preferences=getSharedPreferences("guardar",Context.MODE_PRIVATE);
        editor=preferences.edit();
        // Getting values from button, texts and progress bar
        btn_login = findViewById(R.id.btn_login);
        ET_Username = findViewById(R.id.ET_Username);
        ET_Password = findViewById(R.id.ET_Password);
        progressBar = findViewById(R.id.progressBar);



        // End Getting values from button, texts and progress bar


        progressBar.setVisibility(View.GONE);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String textoEncriptadoConSHA=DigestUtils.sha1Hex(ET_Password.getText().toString());
                UserNameStr=ET_Username.getText().toString();
                PasswordStr= Hash.sha1( ET_Password.getText().toString());
                checklogin check_Login = new checklogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                check_Login.execute(UserNameStr,PasswordStr);
            }
        });
    }

    public class checklogin extends AsyncTask<String,String,String>
    {
        String ConnectionResult = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {

            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String result)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                //Toast.makeText(LoginActivity.this , "Bienvenido "+ UserNameStr , Toast.LENGTH_LONG).show();
                //finish();

                String aux= c_punto_venta;
                if (aux.isEmpty()) {
                    //Toast.makeText(LoginActivity.this,"Vacio",Toast.LENGTH_SHORT).show();
                    Intent nuevoform = new Intent(LoginActivity.this, PuntoActivity.class);
                    startActivity(nuevoform);
                    finish();
                }
                else
                {
                    //Toast.makeText(LoginActivity.this,"Leno",Toast.LENGTH_SHORT).show();
                    Intent nuevoform = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(nuevoform);
                    finish();
                }
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String usernam = UserNameStr;
            String passwordd =PasswordStr;
            claseGlobal objEscritura=(claseGlobal)getApplicationContext();
            //claseGlobal objLectura=(claseGlobal)getApplicationContext();

            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                ConnectionResult = "Ingresar Usuario y Contraseña";
            else
            {
                try
                {
                    ConnectionStr conStr=new ConnectionStr();

                    connect =conStr.connectionclasss();        // Connect to database
                    if (connect == null)
                    {
                        ConnectionResult = "Revisar tu conexion a internet!";
                    }
                    else
                    {
                        // Change below query according to your own database.
                        String query = "select f.C_Funcionario,f.Funcionario,rtrim(f.C_Punto_Venta)c_punto_venta," +
                                "rtrim(p.C_Punto_Venta)+'-'+rtrim(p.N_Punto_Venta)Punto, p.c_bodega, f.cod_pedidos from v_Gen_Funcionarios as f " +
                                "left join Fac_Puntos_Venta as p on f.C_Punto_Venta=p.C_Punto_Venta where flag_activo=1 " +
                                "and C_Funcionario='" + usernam + "' and Clave_Funcionario='"+ passwordd +"'  ";
                        Statement stmt = connect.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            //ConnectionResult = "Bienvenido " + rs.getString(2);
                            // c_funcionario=rs.getString(1);
                            //funcionario=rs.getString(2);
                            editor.putString("CPunto", rs.getString(3));
                            editor.putString("CFuncionario",rs.getString(1));
                            editor.putString("Funcionario",rs.getString(2));
                            editor.putString("Punto",rs.getString(4));
                            editor.putString("Bodega",rs.getString(5));
                            editor.putString("CodPedido",rs.getString(5));
                            editor.apply();
                            c_punto_venta=preferences.getString("CPunto","No existe dato");
                            //c_punto_venta=rs.getString(3);
                            //punto=rs.getString(4);
                            //llenando variables globales
                            objEscritura.setC_funcionario(preferences.getString("CFuncionario","No existe dato"));
                            objEscritura.setFuncionario(preferences.getString("Funcionario","no existe dato"));
                            objEscritura.setC_punto_venta(preferences.getString("CPunto","No existe Dato"));
                            objEscritura.setPunto(preferences.getString("Punto","NO Existe dato"));
                            objEscritura.setC_bodega(preferences.getString("Bodega","no existe"));
                            objEscritura.setCod_pedidos(preferences.getString("CodPedido","No existe"));
                            isSuccess=true;
                            connect.close();
                        }
                        else
                        {
                            ConnectionResult = "Contraseña no valida!";
                            isSuccess = false;
                            //salirp("Inicio de sesión","Contraseña incorrecta",1);
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    ConnectionResult = ex.getMessage();
                    //salirp("Inicio de sesión","Contraseña incorrecta",1);
                }
            }
            return ConnectionResult;
        }
    }

    public static class Hash {

        /* Retorna un hash a partir de un tipo y un texto */
        public static String getHash(String txt, String hashType) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest
                        .getInstance(hashType);
                byte[] array = md.digest(txt.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < array.length; ++i) {
                    sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                            .substring(1, 3));
                }
                return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        /* Retorna un hash MD5 a partir de un texto */
        public static String md5(String txt) {
            return Hash.getHash(txt, "MD5");
        }

        /* Retorna un hash SHA1 a partir de un texto */
        public static String sha1(String txt) {
            return Hash.getHash(txt, "SHA1");
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
    public void salir (View view){
        finish();
    }
}
