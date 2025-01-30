package com.example.apprepaso.Room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apprepaso.R
import com.example.apprepaso.databinding.ItemNotasBinding

class NoteAdapter(private var notas: MutableList<Note>, private var listener: onClickListener) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemNotasBinding.bind(view)

        fun setListener(note: Note) {
            binding.root.setOnClickListener {
                listener.onClick(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nota = notas[position]
        holder.setListener(nota)
        holder.binding.tituloNota.text = nota.title
        holder.binding.fechaNota.text = nota.date
    }

    override fun getItemCount(): Int {return notas.size}

    fun setNotas(notas: MutableList<Note>) {
        this.notas.clear()
        this.notas.addAll(notas)
        notifyDataSetChanged()
    }

}