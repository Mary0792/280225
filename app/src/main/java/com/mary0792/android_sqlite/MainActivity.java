package com.mary0792.android_sqlite;


import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone;
    private Button buttonAddContact;
    private ListView listViewContacts;
    private DBHelper dbHelper;
    private ArrayList<String> contactsList;
    private ArrayAdapter<String> contactsAdapter;
    private ArrayList<Integer> contactIds;  // Para guardar los IDs de los contactos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar los componentes de la UI
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonAddContact = findViewById(R.id.buttonAddContact);
        listViewContacts = findViewById(R.id.listViewContacts);

        // Inicializar la base de datos
        dbHelper = new DBHelper(this);
        contactsList = new ArrayList<>();
        contactIds = new ArrayList<>();
        contactsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
        listViewContacts.setAdapter(contactsAdapter);

        // Cargar los contactos desde la base de datos
        loadContacts();

        // Configurar el botón para agregar un nuevo contacto
        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String phone = editTextPhone.getText().toString();

                // Agregar el contacto a la base de datos
                if (!name.isEmpty() && !phone.isEmpty()) {
                    dbHelper.addContact(name, phone);
                    loadContacts();  // Recargar la lista de contactos
                    editTextName.setText("");
                    editTextPhone.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Por favor ingrese un nombre y número", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el clic en la lista para eliminar un contacto
        listViewContacts.setOnItemClickListener((parent, view, position, id) -> {
            int contactId = contactIds.get(position);
            dbHelper.deleteContact(contactId);
            loadContacts();  // Recargar la lista después de eliminar
            Toast.makeText(MainActivity.this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
        });
    }

    // Método para cargar todos los contactos en la lista
    private void loadContacts() {
        Cursor cursor = dbHelper.getAllContacts();
        contactsList.clear();
        contactIds.clear();  // Limpiar la lista de IDs antes de cargar los nuevos

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PHONE));

                // Agregar a la lista de contactos
                contactsList.add(name + ": " + phone);
                contactIds.add(id);  // Guardar el ID del contacto

            } while (cursor.moveToNext());
            cursor.close();
        }

        contactsAdapter.notifyDataSetChanged();
    }
}

