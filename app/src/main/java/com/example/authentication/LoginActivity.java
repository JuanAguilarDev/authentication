package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextView tvEmail, tvPassword;
    Button btnForget, btnAuth;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Asociate
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        btnAuth = findViewById(R.id.btnAuth);
        btnForget = findViewById(R.id.btnForgetPassword);

    }

    public void start(){
        if(!validate()){
            Toast.makeText(this, "Asegurece de que los campos se hayan llenado correctamente. ", Toast.LENGTH_SHORT).show();
        }else{
            
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