package com.example.sistemas.casalinda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.sistemas.casalinda.Utilidades.claseGlobal;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        claseGlobal objLectura=(claseGlobal)getApplicationContext();

        preferences=getSharedPreferences("guardar", Context.MODE_PRIVATE);
        editor=preferences.edit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        //codigo nuevo
        View hView =navigationView.getHeaderView(0);
        TextView punto= hView.findViewById(R.id.edtPunto);
        TextView funcionario= hView.findViewById(R.id.edtFuncionario);
        punto.setText(preferences.getString("Punto","No Existe"));
        funcionario.setText(preferences.getString("Funcionario","No Existe"));
        //punto.setText(objLectura.getPunto());
        //funcionario.setText(objLectura.getFuncionario());
        //fin codigo nuevo
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


    }


    @Override
    public void onBackPressed() {
        salir();
    }
    public void salir(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alerta=new AlertDialog.Builder(this);
            alerta.setMessage("Desea Salir de la Aplicacion")
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm =getSupportFragmentManager();

        if (id == R.id.nav_inicio) {
            // Handle the camera action
            fm.beginTransaction().replace(R.id.contenedor, new inicio()).commit();
        } else if (id == R.id.nav_cartera) {
            //fm.beginTransaction().replace(R.id.contenedor, new cartera()).commit();
            Intent intent= new Intent( MainActivity.this, CarteraActivity.class );
            startActivity(intent);
        } else if (id == R.id.nav_cliente) {
            //fm.beginTransaction().replace(R.id.contenedor, new cartera()).commit();
            Intent intent= new Intent( MainActivity.this, ClienteActivity.class );
            intent.putExtra("d0","0");
            intent.putExtra("d1","" );
            startActivity(intent);
        } else if (id == R.id.nav_Pedidos) {
            //fm.beginTransaction().replace(R.id.contenedor, new Pedido()).commit();
            Intent intent= new Intent( MainActivity.this, PedidoActivity.class );
            startActivity(intent);
        } else if (id == R.id.nav_Productos) {
           // fm.beginTransaction().replace(R.id.contenedor, new Producto()).commit();
            Intent intent= new Intent( MainActivity.this, ProductoActivity.class );
            startActivity(intent);
        } else if (id == R.id.nav_salir) {
            AlertDialog.Builder alerta=new AlertDialog.Builder(this);
            alerta.setMessage("Desea Salir de la Aplicacion")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
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

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
