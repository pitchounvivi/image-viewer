package com.example.visionneuse;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


//impleter l'interface avec SensorEventListener pour pouvoir écouter les capteurs
public class MainActivity extends AppCompatActivity implements SensorEventListener {

////ATTRIBUT
    //création variables
    SensorManager sm; //le SensorManager
    Sensor sensorLight; //le capteur de lumière
    Sensor sensorMouvement; //le capteur d'accélération/mouvement

    EditText etLight; //l'éditText où l'on veut afficher la valeur en lux

    ImageView ivManga; //l'endroit où l'on souhaite afficher les images

    //Tableau des images à afficher
    Integer[] images = {R.mipmap.manga_girl1, R.mipmap.manga_girl2, R.mipmap.manga_girl3, R.mipmap.manga_girl4, R.mipmap.manga_girl5, R.mipmap.manga_girl6, R.mipmap.manga_girl7};

    //variable pour le capteur de mouvement
    long lastTime = 0;
    int noImage = -1;



////LORS DE L'UTILISATION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //accéder au service système de gection des capteurs
        //instantiation de SensorManager
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        //choix du capteur
        sensorLight = sm.getDefaultSensor(Sensor.TYPE_LIGHT); //capteur lumière
        sensorMouvement = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //capteur mouvement


        //initialisation
        etLight = findViewById(R.id.etLight);
        ivManga = findViewById(R.id.ivManga);


    }


    // activation/enregistrment de l'écoute
    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sensorMouvement, SensorManager.SENSOR_DELAY_NORMAL);
    }


    //mettre l'e capteur en arrière plan / libérer le capteur
    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }



////LE SENSOREVENTLISTENER
    //méthode imposée par l'interface
    //récupération du résultat du capteur de lumière
    @Override
    public void onSensorChanged(SensorEvent event) {
        //déclaration valeur de lumière
        float lightValue;

        //déclaration des valeurs du capteur de mouvement
        float mouvX;
        float mouvY;
        float mouvZ;


        //Mettre à jour le capteur de lumière
        if (event.sensor.getType() == Sensor.TYPE_LIGHT){
            //attribution valeur de la lumière
            lightValue = event.values[0];

            //transformation en String
            String valeur = String.valueOf(lightValue);

            //édition du résultat du capteur de lumière
            etLight.setText(valeur);
        }


        //Mettre à jour le capteur de mouvement
        // REMARQUE n'ayant pas mis d'image par défaut dans image view je n'ai pas d'image au démarrage
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //attribution des valeurs du mouvement
            mouvX = event.values[0];
            mouvY = event.values[1];
            mouvZ = event.values[2];

            //valeur de l'accéléromètre
            float acc = (mouvX * mouvX + mouvY * mouvY + mouvZ * mouvZ) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

            //temp de mouvement
            long time = event.timestamp - lastTime;

            if ( acc>2 && time > 200){
                noImage = (noImage + 1)% images.length;
                ivManga.setImageResource(images[noImage]);
            }
        }
    }

    //méthode imposée par l'interface
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
















//NOTE pour moi même ;-)
//pour vérifer la liste des capteurs
//exemple:
//tvSensors = findViewById(R.id.tvSensors);
//SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
//sensors = sm.getSensorList(Sensor.TYPE_ALL);
//
//String liste ="";
//for(int i = 0; i<sensors.size();i++){
//      liste += sensors.get(i).getName() + "\r\n";
//}
//tvSensor.setText(liste);
