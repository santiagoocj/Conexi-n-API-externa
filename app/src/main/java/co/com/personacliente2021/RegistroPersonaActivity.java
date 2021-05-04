package co.com.personacliente2021;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.personacliente2021.dto.PersonaDTO;
import co.com.personacliente2021.model.Persona;
import co.com.personacliente2021.service.persona.PersonaServiceImpl;
import co.com.personacliente2021.service.tipodocumento.TipoDocumentoServiceImpl;
import co.com.personacliente2021.util.ActionBarUtil;

public class RegistroPersonaActivity extends AppCompatActivity {

    @BindView(R.id.txt_documento)
    EditText txtDocumento;

    @BindView(R.id.txt_nombre)
    EditText txtNombre;

    @BindView(R.id.txt_apellido)
    EditText txtApellido;

    @BindView(R.id.spinnerTipoDocumento)
    Spinner spinnerTipoDocumento;

    PersonaDTO persona;
    private Integer documentoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_persona);
        ButterKnife.bind(this);
        persona = new PersonaDTO();
        persona = (PersonaDTO) getIntent().getSerializableExtra("persona");
        if(persona != null){
            spinnerTipoDocumento.setSelection(persona.getIdTipoDocumento());
            txtDocumento.setText(persona.getNumeroDocumento());
            txtNombre.setText(persona.getNombre());
            txtApellido.setText(persona.getApellido());
        }
        ActionBarUtil.getInstance(this, true).setToolBar(getString(R.string.registro_persona), getString(R.string.insertar));
        listarTiposDocumentos();
        onSelectItemSpinner();

    }

    private void onSelectItemSpinner() {
        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 documentoSeleccionado = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void listarTiposDocumentos() {
        TipoDocumentoServiceImpl tipoDocumentoService = new TipoDocumentoServiceImpl(this);
        tipoDocumentoService.getTipoDocumento(spinnerTipoDocumento);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            guardarInformacion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void guardarInformacion() {
        PersonaServiceImpl personaService = new PersonaServiceImpl(this);
        if(persona != null){
            persona.setIdTipoDocumento(documentoSeleccionado);
            persona.setNumeroDocumento(txtDocumento.getText().toString());
            persona.setNombre(txtNombre.getText().toString());
            persona.setApellido(txtApellido.getText().toString());
            persona.setActivo(true);

            //confirmar insert en la DB
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroPersonaActivity.this);

            //ventana priorizada
            builder.setCancelable(false);
            builder.setTitle("Confirmar");
            builder.setMessage("¿Esta seguro de actualizar la información?");
            builder.setPositiveButton("Confirmar acción",
                    (dialog, which) -> {
                        Intent intent = new Intent(this,MainActivity.class);
                        personaService.actualizar(persona, intent, persona.getIdPersona());
                        goToMainActivty();
                    });
            builder.setNegativeButton(android.R.string.cancel,
                    (dialog, which) ->{});
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            PersonaDTO nuevaPersonaDTO = new PersonaDTO();
            nuevaPersonaDTO.setIdTipoDocumento(documentoSeleccionado);
            nuevaPersonaDTO.setNumeroDocumento(txtDocumento.getText().toString());
            nuevaPersonaDTO.setNombre(txtNombre.getText().toString());
            nuevaPersonaDTO.setApellido(txtApellido.getText().toString());
            nuevaPersonaDTO.setActivo(true);

            //confirmar insert en la DB
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroPersonaActivity.this);

            //ventana priorizada
            builder.setCancelable(false);
            builder.setTitle("Confirmar");
            builder.setMessage("¿Esta seguro de guardar la información?");
            builder.setPositiveButton("Confirmar acción",
                    (dialog, which) -> {
                        Intent intent = new Intent(this,MainActivity.class);
                        personaService.insertar(nuevaPersonaDTO);
                        goToMainActivty();
                    });
            builder.setNegativeButton(android.R.string.cancel,
                    (dialog, which) ->{});
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }


    public void goToMainActivty(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}