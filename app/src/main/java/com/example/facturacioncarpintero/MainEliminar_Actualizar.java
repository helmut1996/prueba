package com.example.facturacioncarpintero;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facturacioncarpintero.ConexionBD.DBConnection;
import com.example.facturacioncarpintero.SQLite.conexionSQLiteHelper;
import com.squareup.picasso.Picasso;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class    MainEliminar_Actualizar extends Activity implements View.OnClickListener {

    private TextView titulo,precio,tvimagen,idproducto,Existencia;
    private EditText cantidad;
    private Button btn_actualizar,btn_eliminar;
    private ImageView imagen;
    private String nombre,img;
    private double prec;
    private int cant;
    private String idp;
    private  String idc;
    private String nc;
    private String nf;
    private String URL="http://ferreteriaelcarpintero.com/images/carpintero/";
    private  int Strock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_eliminar__actualizar);

        init();
        capturandoDatos();
        CargarImagen();
        MostrandoStock();

    }

    private void capturandoDatos() {
        /////pasando los datos del cliente
        Bundle extra=getIntent().getExtras();
        if (extra != null) {
            nombre=extra.getString("NombreProducto");
            cant=extra.getInt("CantidadProducto");
            prec=extra.getDouble("PrecioProducto");
            idp=extra.getString("IdProducto");
            img= extra.getString("NombreImagen");
            idc= extra.getString("IdCliente");
            nc=extra.getString("NombreCliente");
            nf=extra.getString("nombreUsuario");


            System.out.println("nombre P===>"+nombre);
            System.out.println("cantidad P===>"+cant);
            System.out.println("precio P===>"+prec);
            System.out.println("ID P===>"+idp);
            System.out.println("imagen P===>"+img);
            System.out.println("idcliente P===>"+idc);
            System.out.println("nombrecliente P===>"+nc);
            System.out.println("nombrefacturador P===>"+nf);
            titulo.setText(nombre);
            cantidad.setText(String.valueOf(cant));
            precio.setText(String.valueOf(prec));
            idproducto.setText(String.valueOf(idp));
            tvimagen.setText(img);

        }
    }

    private void init() {
        titulo=findViewById(R.id.titulo);
        idproducto=findViewById(R.id.idProductoActualizado);
        tvimagen=findViewById(R.id.tvimagenActualizada);
        precio=findViewById(R.id.precioActualizar);
        cantidad=findViewById(R.id.edit_Actualizar);
        btn_actualizar=findViewById(R.id.btn_Actualizar);
        btn_eliminar=findViewById(R.id.btn_Eliminar);
        imagen=findViewById(R.id.imagenActualizar);
        Existencia=findViewById(R.id.tvExistenciaActualizar);
        btn_actualizar.setOnClickListener(this);
        btn_eliminar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
   int ID= v.getId();
            switch (ID){
                case R.id.btn_Actualizar:
                    ActualizarDatosSQLite();
                    Intent intent =new Intent(getApplicationContext(),MainFactura.class);
                    intent.putExtra("IdCliente",idc);
                    intent.putExtra("NombreCliente",nc);
                    intent.putExtra("nombreUsuario",nf);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_Eliminar:
                    EliminarDatosSQLite();
                        Intent i =new Intent(getApplicationContext(),MainFactura.class);
                    i.putExtra("IdCliente",idc);
                    i.putExtra("NombreCliente",nc);
                    i.putExtra("nombreUsuario",nf);
                        startActivity(i);
                        finish();
                    break;
            }
    }
    private void ActualizarDatosSQLite() {
        /*mandando a llamar conexion a SQLite */
        conexionSQLiteHelper conn= new conexionSQLiteHelper(this,"bd_productos",null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        if (cantidad.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"No se Actualizo en cantidad vacia",Toast.LENGTH_LONG).show();
        } else if(Integer.parseInt(cantidad.getText().toString())==0){

            Toast.makeText(getApplicationContext(),"No se Actualizo en cantidad 0",Toast.LENGTH_LONG).show();
        }else if (Integer.parseInt(cantidad.getText().toString())>Strock){
            Toast.makeText(getApplicationContext(),"No se Actualizo sobre pasa Existencia",Toast.LENGTH_LONG).show();
        }

        else {
            String dbQuery = "update producto set cantidad ="+"'"+cantidad.getText().toString()+"'"+"where id ="+"'"+idproducto.getText().toString()+"'";
            db.execSQL(dbQuery);
            db.close();
            Toast.makeText(getApplicationContext(),"Actualizado!!!",Toast.LENGTH_SHORT).show();
        }

    }

    private void EliminarDatosSQLite() {
        conexionSQLiteHelper conn = new conexionSQLiteHelper(this,"bd_productos",null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        String dbQuery = "delete from producto where id ="+"'"+idproducto.getText().toString()+"'";
        db.execSQL(dbQuery);
        db.close();
        Toast.makeText(getApplicationContext(),"Registro Eliminado!!!",Toast.LENGTH_SHORT).show();
    }

    private void CargarImagen() {
        Picasso.get().load(URL+tvimagen.getText())
               // .error(R.drawable.error)
                .into(imagen);

    }


    public void MostrandoStock(){

        DBConnection dbConnection=new DBConnection();
        dbConnection.conectar();
        try {
            Statement st = dbConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery("\n" +
                    "select Stock from Inventario where idInventario='"+idproducto.getText().toString()+"'");
            while (rs.next()) {
                Strock = rs.getInt("Stock");

                System.out.println("==============>Existencia del producto:" + Strock);
                Existencia.setText("Existencia: "+String.valueOf(Strock));
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
