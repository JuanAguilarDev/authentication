package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextView tvEmail, tvPassword;
    Button btnCreate;

    // FirebaseAuth
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        btnCreate = findViewById(R.id.btnAuth);
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    public void createUser(){
        if(!validate()){
            Toast.makeText(this, "Asegurece de haber completado correctamente los campos.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            String email = tvEmail.getText().toString().trim();
            String password = tvPassword.getText().toString().trim();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Ha ocurrido un error al crear el usuario.", Toast.LENGTH_SHORT).show();                        
                    }
                }
            });
        }
    }

    public boolean validate(){
        Boolean [] result = {validateEmail(), validatePassword()};
        for(int i = 0; i< result.length;i++){
            if(!result[i]){
                return false;
            }
        }
        return true;
    };

    public boolean validateEmail(){
        String em = tvEmail.getText().toString().trim();
        if(em.isEmpty()){
            tvEmail.setError("El campo no puede quedar vacío");
            return false;
        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(em).matches()){
            tvEmail.setError("Ingrese un email valido.");
            return false;
        }else{
            tvEmail.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){

        /*
        *           ^                                 # start of line
                    (?=.*[0-9])                       # digit [0-9]
                    (?=.*[a-z])                       # one lowercase character [a-z]
                    (?=.*[A-Z])                       # one uppercase character [A-Z]
                    (?=.*[!@#&()–[{}]:;',?/*~$^+=<>]) # one of the special character in this [..]
                    .                                 # matches anything
                    {8,20}                            # length at least 8 characters and maximum of 20 characters
                    $                                 # end of line
        *
        */

        String pass = tvPassword.getText().toString().trim();
        Pattern passwordRegex = Pattern.compile(
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"
        );

        if(pass.isEmpty()){
            tvPassword.setError("Este campo no puede quedar vacío ");
            return false;
        }else if(!passwordRegex.matcher(pass).matches()){
            tvPassword.setError("Esta contraseña es muy debil.");
            return false;
        } else{
            tvPassword.setError(null);
            return true;
        }
    }
}