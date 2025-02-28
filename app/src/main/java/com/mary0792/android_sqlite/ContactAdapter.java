package com.mary0792.android_sqlite;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<String> contactList;
    private Context context;
    private DBHelper dbHelper;

    // Constructor que recibe la lista de contactos
    public ContactAdapter(Context context, List<String> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.dbHelper = new DBHelper(context);
    }

    // Este método infla el layout de cada fila
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ContactViewHolder(view);
    }

    // Este método asigna datos a cada fila del RecyclerView
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        String contact = contactList.get(position);
        holder.contactTextView.setText(contact);

        // Configuramos el listener para eliminar un contacto
        holder.itemView.setOnClickListener(v -> showDeleteDialog(position));
    }

    // Devuelve el número de elementos en la lista
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // Clase que representa cada ítem de la lista
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactTextView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactTextView = itemView.findViewById(android.R.id.text1);
        }
    }

    // Método para mostrar el diálogo de confirmación antes de eliminar
    private void showDeleteDialog(int position) {
        String contactToDelete = contactList.get(position);

        new AlertDialog.Builder(context)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este contacto: " + contactToDelete + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Eliminar el contacto de la base de datos
                    String contactName = contactToDelete.split(" - ")[0]; // Extraer el nombre del contacto
                    dbHelper.deleteContact(contactName);
                    // Actualizar la lista después de la eliminación
                    contactList.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton("No", null)  // Si el usuario elige "No", no se hace nada
                .show();
    }
}


