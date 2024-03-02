package com.example.sistemas.casalinda.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemas.casalinda.ConnectionStr;
import com.example.sistemas.casalinda.PedidoEditar;
import com.example.sistemas.casalinda.R;
import com.example.sistemas.casalinda.entidades.Pedido;
import com.example.sistemas.casalinda.holder.ViewHolderPedido;
import com.example.sistemas.casalinda.interfaz.InterfazClickRecyclerView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AdaptadorRecyclerView  extends RecyclerView.Adapter<ViewHolderPedido> {

    public static final String POS="Posicion";
    public static final String TIP="Tipo";
    public static final String COL="Color";
    public static final String COD="Codigo";
    public static final String NOM="Nombre";
    public static final String CAN="Cantidad";
    public static final String UNI="Unitario";
    public static final String TOT="Total";
    public static final String PVP="Pvp";
    public static final String CUV="Cuv";
    public static final String BOD="Bodega";
    public static final String PON="Pon";
    String ConnectionResult = "";
    Connection connect;
    private List<Pedido> pedidos;
    private InterfazClickRecyclerView interfazClickRecyclerView;
    public Double tot,un;
    //public String cod,nomb;
    public String totString="0";
    //EditText can,to;
   // private com.example.sistemas.casalinda.holder.ViewHolderPedido holder;
    //private int position;

    public AdaptadorRecyclerView(){
        this.pedidos=pedidos;
    }

    public void setPedidos (List<Pedido>pedidos){
        this.pedidos=pedidos;
        this.notifyDataSetChanged();
    }

    public List<Pedido>getPedidos(){
        return pedidos;
    }

    public AdaptadorRecyclerView(InterfazClickRecyclerView interfazClickRecyclerView){
        this.interfazClickRecyclerView=interfazClickRecyclerView;
        this.pedidos=new ArrayList<>();

    }
    public void actualizarPedido ( int i,Pedido pedido){
        this.pedidos.set(i,pedido);
        un=Double.parseDouble(pedido.getUnitario()) ;
        this.notifyItemChanged(i);
        tot=0.00;
        for (Pedido data:pedidos){
            tot+=Double.parseDouble(data.getTotal());
        }

        totString=String.valueOf(tot);
    }
    public void agregarPedido (Pedido pedido){
        this.pedidos.add(pedido);
        un=Double.parseDouble(pedido.getUnitario()) ;

        this.notifyItemInserted(this.pedidos.size()-1);
         tot=0.00;
        for (Pedido data:pedidos){
            tot+=Double.parseDouble(data.getTotal());
        }

        totString=String.valueOf(tot);

    }

    public void eliminarTodo(){
        this.pedidos.clear();
        this.notifyDataSetChanged();
    }
    public void eliminar(int indice){
        if (indice <0 || indice >=this.getItemCount())return;
        this.pedidos.remove(indice);

        this.notifyItemRemoved(indice);
        tot=0.00;
        for (Pedido data:pedidos){
            tot+=Double.parseDouble(data.getTotal());
        }

        totString=String.valueOf(tot);
    }
    @NonNull
    @Override

    public ViewHolderPedido onCreateViewHolder(@NonNull ViewGroup parent, int viewType){


            //Esta es la vista del layout que muestra los detalles del peddio (layout_recycler.xml)

            View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler, parent, false);
            //Crear el viewholder a partir de esta visa. Mira la calse ViewHolderPedido si quieres
            final ViewHolderPedido viewHolder = new ViewHolderPedido(vista);
            //En el clic de la vista (el pedido en general) invocamos a nuestra interfaz personalizada pasandole la vista y el pedido
        try {
            vista.setOnClickListener(v -> {

                interfazClickRecyclerView.onClick(v, pedidos.get(viewHolder.getAdapterPosition()));
                Context context = v.getContext();
                int position = viewHolder.getAdapterPosition();
                Pedido pedido = pedidos.get(position);
                try {
                    ConnectionStr conStr=new ConnectionStr();
                    connect=conStr.connectionclasss();
                    if (connect == null)
                    {
                        ConnectionResult = "Revisar tu conexion a internet!";
                        Toast.makeText(vista.getContext(),ConnectionResult,Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = new Intent(v.getContext(), PedidoEditar.class);
                        intent.putExtra(POS, String.valueOf(position));
                        intent.putExtra(TIP, pedido.getTipo());
                        intent.putExtra(COL, pedido.getColor());
                        intent.putExtra(COD, pedido.getCodigo());
                        intent.putExtra(NOM, pedido.getNombre());
                        intent.putExtra(CAN, pedido.getCantidad());
                        intent.putExtra(UNI, pedido.getUnitario());
                        intent.putExtra(TOT, pedido.getTotal());
                        intent.putExtra(PVP, pedido.getPvp());
                        intent.putExtra(CUV, pedido.getCuv());
                        intent.putExtra(BOD, pedido.getBod());
                        intent.putExtra(PON, pedido.getPon());
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            }

        catch (Exception e)
        {
            Toast.makeText(vista.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderPedido holder, int position){

        //Dibujar la fila del pedido con los datos de pedido actualmente solicitadao segun la variable position
        Pedido pedido= this.pedidos.get(position);
        //holder.tvCodigo=

        holder.getTextViewProducto().setText(String.valueOf(pedido.getCodigo()));
        holder.getTextViewNombre().setText(String.valueOf(pedido.getNombre()));
        holder.getTextViewCantidad().setText(String.valueOf(pedido.getCantidad()));
        holder.getTextViewUnitario().setText(String.valueOf(pedido.getUnitario()));
        holder.getTextViewTotal().setText(String.valueOf(pedido.getTotal()));

    /*   can=(EditText)can.findViewById(R.id.edtCantidad);
        to=(EditText)to.findViewById(R.id.edtTotal);
        can.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               canti= Double.parseDouble(String.valueOf(can.getText()));
            }
        });
*/
    }
    @Override
    public int getItemViewType(int position){
        int viewType=1;
        if(position==0)viewType=0;
        return viewType;
    }
    @Override
    public int getItemCount(){
            return this.pedidos.size();
    }


}
