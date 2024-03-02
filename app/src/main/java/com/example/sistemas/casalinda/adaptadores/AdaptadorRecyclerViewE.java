package com.example.sistemas.casalinda.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemas.casalinda.R;
import com.example.sistemas.casalinda.entidades.Existencia;
import com.example.sistemas.casalinda.holder.ViewHolderExistencia;
import com.example.sistemas.casalinda.interfaz.InterfazClickRecyclerViewE;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorRecyclerViewE extends RecyclerView.Adapter<ViewHolderExistencia> {

    //String c_bodega,n_bodega,existencia,ubicacion;

    private List<Existencia>existencias;

    private InterfazClickRecyclerViewE interfazClickRecyclerViewE;


    public AdaptadorRecyclerViewE(InterfazClickRecyclerViewE interfazClickRecyclerViewE){
        this.interfazClickRecyclerViewE=interfazClickRecyclerViewE;
        this.existencias=new ArrayList<>();

    }

    public void agregarExistencia (Existencia existencia){
        this.existencias.add(existencia);
        this.notifyItemInserted(this.existencias.size());


    }

    public List<Existencia>getExistencias(){
        return existencias;
    }

    @NonNull
    @Override
    public ViewHolderExistencia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_recycler_existencia,parent,false);
        final ViewHolderExistencia viewHolder=new ViewHolderExistencia(vista);
        vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 interfazClickRecyclerViewE.onClick (v, existencias.get(viewHolder.getAdapterPosition()));
                Context context =v.getContext();
                int position=viewHolder.getAdapterPosition();
                Existencia existencia=existencias.get(position);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderExistencia holder, int position) {
        Existencia existencia=this.existencias.get(position);
        holder.getTvBodega().setText(String.valueOf(existencia.getC_bodega()));
        holder.getTvNombreB().setText(String.valueOf(existencia.getN_bodega()));
        holder.getTvExistencia().setText(String.valueOf(existencia.getExistencia()));
        holder.getTvUbicacion().setText(String.valueOf(existencia.getUbicacion()));
    }
    @Override
    public int getItemViewType(int position){
        int viewType=1;
        if(position==0)viewType=0;
        return viewType;
    }
    @Override
    public int getItemCount() {
        return this.existencias.size();
    }
}
