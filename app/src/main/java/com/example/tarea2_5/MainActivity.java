package com.example.tarea2_5;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private ImageView im1;
    private static final int REQUEST_CALL_PHONE = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1 ;
    private Intent intent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1=findViewById(R.id.txtMostrar);
        im1=findViewById(R.id.imageView);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return true;
    }

    public void llamarIntent(View vista){

        switch(vista.getId()){
            case R.id.btnLanzarNavegador:
                intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.iestrassierra.com/"));
                startActivity(intent);
                break;
            case R.id.btnLanzarContacto:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // No tenemos permiso.¿Debemos mostrar una explicación para pedir el permiso?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CALL_PHONE)) {
                        // Muestra una explicación del porqué pedir el permiso.
                        Toast.makeText(this, "Se necesita permiso para realizar llamadas",
                                Toast.LENGTH_SHORT).show();
                    }
                    // Solicitamos el permiso.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CALL_PHONE);
                } else {
                    //Tenemos permiso.
                    llamar();
                }
                break;
            case R.id.btnMarcarTelefono:
                intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:(+34)555900800"));
                startActivity(intent);
                break;
            case R.id.btnMostrarMapa:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:37.8847,-4.77915?z=13"));
                startActivity(intent);
                break;
            case R.id.btnBuscarMapa:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=IES Trassierra"));
                startActivity(intent);
                break;
            case R.id.btnTomarFoto:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {
                        // Muestra una explicación del por qué de perdir el permiso.
                        Toast.makeText(this, "Se necesita permiso para acceder a la cámara.",
                                Toast.LENGTH_SHORT).show();
                    }
                    // Solicitamos el permiso.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_IMAGE_CAPTURE);
                } else {
                    //Tenemos permiso.
                    capturar();
                }


                break;
            case R.id.btnBuscarContactos:
                intent=new Intent(Intent.ACTION_VIEW,Uri.parse("content://contacts/" +
                        "people/"));
                startActivity(intent);
                break;
        }
    }
    ActivityResultLauncher<Intent> lanzadorActividades = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String resultado = result.getData().toUri(0);
                        tv1.setText(resultado);
                        // Mostramos la foto de la cámara en el ImageView
                        // Los objetos son pasados entre las Actividades mediante
                        // objetos de la clase Parcelable, de forma parecida a como
                        // se pasan los tipos básicos mediante objetos de la clase
                        // Bundle.
                        im1.setImageBitmap((Bitmap) result.getData().getParcelableExtra("data"));
                    }
                }
            });


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    // Permiso aceptado y procedemos a realizar la llamada
                    llamar();
                else
                    // Permiso denegado
                    Toast.makeText(this, "No se ha aceptado el permiso", Toast.LENGTH_SHORT).show();
                return;
            // Gestionar el resto de permisos
            case REQUEST_IMAGE_CAPTURE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    // Permiso aceptado y procedemos a realizar la llamada
                    capturar();
                else
                    // Permiso denegado
                    Toast.makeText(this, "No se ha aceptado el permiso", Toast.LENGTH_SHORT).show();
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void capturar() {
        intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // Lanzamos la actividad y esperamos su resultado
        lanzadorActividades.launch(intent);
    }
    private void llamar() {
        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)555900800"));
        startActivity(intent);
    }
}
