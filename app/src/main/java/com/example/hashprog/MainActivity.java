package com.example.hashprog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;

    private EditText editTextInput;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация элементов интерфейса
        editTextInput = findViewById(R.id.editTextInput);
        textViewResult = findViewById(R.id.textViewResult);

        Button buttonSelectFile = findViewById(R.id.buttonSelectFile);
        Button buttonEncode = findViewById(R.id.buttonEncode);
        Button buttonHash = findViewById(R.id.buttonHash);
        Button buttonEncrypt = findViewById(R.id.buttonEncrypt);

        // Открытие файлового менеджера
        buttonSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");  // Указываем любой тип файла
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, PICK_FILE_REQUEST);
            }
        });

        // Кнопка для кодирования в Base64
        buttonEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editTextInput.getText().toString();
                String encoded = Base64Utils.encodeToBase64(inputText);
                textViewResult.setText("Base64 Encoded: \n" + encoded);
            }
        });

        // Кнопка для хэширования SHA-256
        buttonHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editTextInput.getText().toString();
                String hashed = HashUtils.hashWithSHA256(inputText);
                textViewResult.setText("SHA-256 Hash: \n" + hashed);
            }
        });

        // Кнопка для шифрования AES
        buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editTextInput.getText().toString();
                try {
                    String key = AESUtils.generateKey();  // Генерация ключа AES
                    String encrypted = AESUtils.encrypt(inputText, key);
                    String decrypted = AESUtils.decrypt(encrypted, key);
                    textViewResult.setText("AES Encrypted: \n" + encrypted + "\n\nAES Decrypted: \n" + decrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                    textViewResult.setText("Error during AES encryption/decryption");
                }
            }
        });
    }

    // Обработка выбранного файла
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_FILE_REQUEST) {
            Uri uri = data.getData();
            if (uri != null) {
                // Чтение содержимого выбранного файла
                String fileContent = readFile(uri);
                if (fileContent != null) {
                    editTextInput.setText(fileContent);  // Отображаем содержимое в поле ввода
                }
            }
        }
    }

    // Метод для чтения содержимого выбранного файла
    private String readFile(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            inputStream.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
