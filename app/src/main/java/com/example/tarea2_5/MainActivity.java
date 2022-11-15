package com.example.tarea2_5;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private ImageView im1;

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
        Intent intent=null;

        switch(vista.getId()){
            case R.id.btnLanzarNavegador:
                intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.iestrassierra.com/"));
                startActivity(intent);
                break;
            case R.id.btnLanzarContacto:
                intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)555900800"));
                startActivity(intent);
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
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                // Lanzamos la actividad y esperamos su resultado
                lanzadorActividades.launch(intent);
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

}