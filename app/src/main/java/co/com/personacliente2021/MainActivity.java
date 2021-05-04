package co.com.personacliente2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.personacliente2021.service.persona.PersonaServiceImpl;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listViewPersonas)
    ListView listViewPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PersonaServiceImpl personaService = new PersonaServiceImpl(this);
        personaService.getPersona(listViewPersonas);

   }

    public void goToRegistroPersona(View view) {
        Intent intent = new Intent(MainActivity.this,RegistroPersonaActivity.class);
        startActivity(intent);
    }
}