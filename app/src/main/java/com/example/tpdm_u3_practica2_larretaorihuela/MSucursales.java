package com.example.tpdm_u3_practica2_larretaorihuela;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MSucursales extends AppCompatActivity {
    EditText id,nombre,domicilio;
    Button insertar,eliminar,modificar,buscar;
    FirebaseFirestore bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msucursales);

        id = findViewById(R.id.idS);
        nombre = findViewById(R.id.nomS);
        domicilio = findViewById(R.id.dom);

        insertar = findViewById(R.id.btnInsertarS);
        eliminar = findViewById(R.id.btnEliminarS);
        modificar = findViewById(R.id.btnModificarS);
        buscar = findViewById(R.id.btnMostrarS);

        bd = FirebaseFirestore.getInstance();

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarSucursal();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarSucursalID();
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarSucursal();
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSucursalID();
            }
        });

    }

    private void insertarSucursal(){
        Sucursales suc = new Sucursales(id.getText().toString(),nombre.getText().toString(),domicilio.getText().toString());

        bd.collection("sucursales")
                .document(id.getText().toString())
                .set(suc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MSucursales.this, "INSERCIÓN EXITOSA",
                                Toast.LENGTH_SHORT).show();
                        id.setText("");nombre.setText("");domicilio.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MSucursales.this,
                                "ERROR AL INSERTAR", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarSucursalID(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        final EditText idEliminar = new EditText(this);
        idEliminar.setHint("NO DEBE ESTAR VACÍO");

        alerta.setTitle("ATENCION").setMessage("ESCRIBA EL ID:")
                .setView(idEliminar)
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(idEliminar.getText().toString().isEmpty()){
                            Toast.makeText(MSucursales.this, "EL ID ESTA VACIO",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        eliminarProducto(idEliminar.getText().toString());
                    }
                })
                .setNegativeButton("Cancelar",null)
                .show();
    }

    private  void eliminarProducto(String ids){
        bd.collection("sucursales")
                .document(ids)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MSucursales.this,
                                "SE HA ELIMINADO CORRECTAMENTE!", Toast.LENGTH_SHORT).show();
                        id.setText("");nombre.setText("");domicilio.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MSucursales.this,
                                "NO EXISTE EL PRODUCTO CON ESTE ID!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarSucursalID(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        final EditText idSucursal = new EditText(this);
        idSucursal.setHint("NO DEBE SER VACÍO");

        alerta.setTitle("BUSQUEDA").setMessage("ESCRIBA EL CODIGO")
                .setView(idSucursal)
                .setPositiveButton("buscar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(idSucursal.getText().toString().isEmpty()){
                            Toast.makeText(MSucursales.this,
                                    "DEBES ESCRIBIR UN CODIGO", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mostrarSucursal(idSucursal.getText().toString());
                    }
                })
                .setNegativeButton("cancelar",null)
                .show();
    }

    private void mostrarSucursal(final String cod){
        bd.collection("sucursales")
                .whereEqualTo("id",cod)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        Query q = queryDocumentSnapshots.getQuery();

                        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot registro : task.getResult()){
                                        Map<String, Object> dato = registro.getData();

                                        nombre.setText(dato.get("nombre").toString());
                                        id.setText(dato.get("id").toString());
                                        domicilio.setText(dato.get("domicilio").toString());
                                    }
                                }
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MSucursales.this,
                                                "NO EXISTE EL PRODUCTO CON ESTE ID!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
    }

    private void modificarSucursal(){
        Map<String, Object> data = new HashMap<>();
        data.put("id",id.getText().toString());
        data.put("nombre",nombre.getText().toString());
        data.put("domicilio",domicilio.getText().toString());

        bd.collection("sucursales")
                .document(id.getText().toString())
                .update(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MSucursales.this,
                        "ERROR AL ACTUALIZAR",
                        Toast.LENGTH_SHORT).show();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MSucursales.this,
                                "ACTUALIZACIÓN EXITOSA",
                                Toast.LENGTH_SHORT).show();
                        id.setText("");nombre.setText("");domicilio.setText("");
                    }
                });
    }

}
