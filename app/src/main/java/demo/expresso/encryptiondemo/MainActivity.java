package demo.expresso.encryptiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText msg, password;
    Button encryptBtn,decryptBtn;
    TextView result,result2;

    String outputStr,inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msg =(EditText)findViewById(R.id.message);
        password =(EditText)findViewById(R.id.password);
        result =(TextView) findViewById(R.id.textView);
        result2 =(TextView) findViewById(R.id.textView1);
        encryptBtn =(Button)findViewById(R.id.encrypt);
        decryptBtn =(Button)findViewById(R.id.decrpt);
        encryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inputPassword=password.getText().toString();
                    outputStr =encryptData(msg.getText().toString(),password.getText().toString());
                    result.setText(outputStr);

                    result2.setText(CryptoHelper.encrypt(msg.getText().toString(),inputPassword,"12345678"));


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        decryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inputPassword=password.getText().toString();
                    outputStr =decryptData(outputStr,inputPassword);
                    result.setText(outputStr);

                    result2.setText(CryptoHelper.decrypt(msg.getText().toString(),inputPassword,"12345678"));


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public String encryptData(String data, String password) throws Exception{
        SecretKeySpec key =generateKey(password);
        Cipher c =Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal =c.doFinal(data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;
    }

    public String decryptData(String outputStr,String password)throws Exception{
        SecretKeySpec key =generateKey(password);
        Cipher c =Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedVal =Base64.decode(outputStr,Base64.DEFAULT);
        byte[] desVal =c.doFinal(decodedVal);
        String decryptedValue =new String(desVal);
        return decryptedValue;

    }

    public SecretKeySpec generateKey(String password)throws Exception{
        final MessageDigest digest =MessageDigest.getInstance("SHA-256");
        byte[] bytes=  password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec  secretKeySpec =new SecretKeySpec(key ,"AES");
        return secretKeySpec;
    }

}
