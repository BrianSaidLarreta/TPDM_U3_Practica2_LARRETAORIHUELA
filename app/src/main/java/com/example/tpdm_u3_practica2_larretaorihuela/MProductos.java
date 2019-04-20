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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MProductos extends AppCompatActivity {
    EditText codigo,nombre, descripcion;
    Button insertar, eliminar, modificar, mostrar;
    FirebaseFirestore bd;
    List<Map> productos;
    CollectionReference prod;
    ListView lista;
    String con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mproductos);

        codigo = findViewById(R.id.idP);
        nombre =findViewById(R.id.nom);
        descripcion = findViewById(R.id.desc);

        insertar = findViewById(R.id.btnInsertar);
        eliminar = findViewById(R.id.btnEliminar);
        modificar = findViewById(R.id.btnModificar);
        mostrar = findViewById(R.id.btnMostrar);

        lista = findViewById(R.id.listaProductos);
        con = "";
        bd = FirebaseFirestore.getInstance();

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarProducto();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarProductoID();
            }
        });

        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostrarProductoID();
                llenarlista();
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarProducto();
            }
        });

    }

    private void insertarProducto(){
        Productos prod = new Productos(codigo.getText().toString(),nombre.getText().toString(),descripcion.getText().toString());

        bd.collection("productos")
                .document(codigo.getText().toString())
                .set(prod)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MProductos.this, "INSERCIÓN EXITOSA",
                                Toast.LENGTH_SHORT).show();
                        codigo.setText("");nombre.setText("");descripcion.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MProductos.this,
                                "ERROR AL INSERTAR", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarProductoID(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        final EditText idEliminar = new EditText(this);
        idEliminar.setHint("NO DEBE ESTAR VACÍO");

        alerta.setTitle("ATENCION").setMessage("ESCRIBA EL ID:")
                .setView(idEliminar)
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(idEliminar.getText().toString().isEmpty()){
                            Toast.makeText(MProductos.this, "EL ID ESTA VACIO",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        eliminarProducto(idEliminar.getText().toString());
                    }
                })
                .setNegativeButton("Cancelar",null)
                .show();
    }

    private  void eliminarProducto(String id){
        bd.collection("productos")
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MProductos.this,
                                "SE HA ELIMINADO CORRECTAMENTE!", Toast.LENGTH_SHORT).show();
                        codigo.setText("");nombre.setText("");descripcion.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MProductos.this,
                                "NO EXISTE EL PRODUCTO CON ESTE ID!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarProductoID(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        final EditText idProducto = new EditText(this);
        idProducto.setHint("NO DEBE SER VACÍO");

        alerta.setTitle("BUSQUEDA").setMessage("ESCRIBA EL CODIGO")
                .setView(idProducto)
                .setPositiveButton("buscar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(idProducto.getText().toString().isEmpty()){
                            Toast.makeText(MProductos.this,
                                    "DEBES ESCRIBIR UN CODIGO", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mostrarProducto(idProducto.getText().toString());
                    }
                })
                .setNegativeButton("cancelar",null)
                .show();
    }

    private void mostrarProducto(final String cod){
        bd.collection("productos")
                .whereEqualTo("codigo",cod)
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
                                        codigo.setText(dato.get("codigo").toString());
                                        descripcion.setText(dato.get("descripcion").toString());
                                    }
                                }
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MProductos.this,
                                                "NO EXISTE EL PRODUCTO CON ESTE ID!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
    }

    private void modificarProducto(){
        Map<String, Object> data = new HashMap<>();
        data.put("codigo",codigo.getText().toString());
        data.put("nombre",nombre.getText().toString());
        data.put("descripcion",descripcion.getText().toString());

        bd.collection("productos")
                .document(codigo.getText().toString())
                .update(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MProductos.this,
                        "ERROR AL ACTUALIZAR",
                        Toast.LENGTH_SHORT).show();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MProductos.this,
                                "ACTUALIZACIÓN EXITOSA",
                                Toast.LENGTH_SHORT).show();
                        codigo.setText("");nombre.setText("");descripcion.setText("");
                    }
                });
    }

    private void llenarlista(){
        prod.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if  (queryDocumentSnapshots.size()<=0){
                    Toast.makeText(MProductos.this,
                            "No hay Productos registrados",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                productos=new ArrayList<>();
                for (QueryDocumentSnapshot registro:queryDocumentSnapshots){
                    Productos producto = registro.toObject(Productos.class);
                    Map<String,Object> datos=new HashMap<>();
                    datos.put("nombre",producto.getNombre());
                    datos.put("codigo",producto.getCodigo());
                    datos.put("descripcion",producto.getDescripcion());
                    productos.add(datos);
                    //llenarLista();
                }
            }
        });
    }

}
