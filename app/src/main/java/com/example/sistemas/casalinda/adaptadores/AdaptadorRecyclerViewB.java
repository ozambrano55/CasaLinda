package com.example.sistemas.casalinda.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemas.casalinda.R;
import com.example.sistemas.casalinda.entidades.Busca;
import com.example.sistemas.casalinda.holder.ViewHolderBusca;
import com.example.sistemas.casalinda.interfaz.InterfazClickRecyclerViewB;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorRecyclerViewB extends  RecyclerView.Adapter<ViewHolderBusca> {
    private List <Busca>buscar;
    private InterfazClickRecyclerViewB interfazClickRecyclerViewB;
public String A,B,C;

    public AdaptadorRecyclerViewB(InterfazClickRecyclerViewB interfazClickRecyclerViewB) {
        this.interfazClickRecyclerViewB=interfazClickRecyclerViewB;
        this.buscar=new ArrayList<>();
    }

    public  List <Busca>getBusca(){return buscar;}
    public void eliminarTodo(){
        this.buscar.clear();
        this.notifyDataSetChanged();
    }
    public void agregarBusca(Busca busca){
        this.buscar.add(busca);
        this.notifyItemInserted(this.buscar.size());
    }


    @NonNull
    @Override
    public ViewHolderBusca onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_recyclerb,parent,false);
        final ViewHolderBusca viewHolder=new ViewHolderBusca(vista);
        vista.setOnClickListener(v ->  {

                interfazClickRecyclerViewB.onClick(v,buscar.get(viewHolder.getAdapterPosition()));

                int position=viewHolder.getAdapterPosition();
                Busca busca=buscar.get(position);

                try{

                   A=busca.getCodigo();
                   B=busca.getNombre();
                    C=busca.getCantidad();

            }catch (Exception e){}
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBusca holder, int position) {
        Busca busca =this.buscar.get(position);
        holder.getCodigo().setText(String.valueOf(busca.getCodigo()));
        holder.getNombre().setText(String.valueOf(busca.getNombre()));
        holder.getCantidad().setText(String.valueOf(Double.valueOf( busca.getCantidad())));
    }
    @Override
    public int getItemViewType(int position){
            int viewType=1;
            if(position==0)viewType=0;
            return viewType;
    }
    @Override
    public int getItemCount() {
        return this.buscar.size();
    }
}
