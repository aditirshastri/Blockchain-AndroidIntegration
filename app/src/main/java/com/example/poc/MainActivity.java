package com.example.poc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;

public class MainActivity extends AppCompatActivity {
    String walletAddressVal;
    EditText walletInput;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        walletInput = (EditText) findViewById(R.id.walletInput);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletAddressVal = walletInput.getText().toString();
                //}
                // });


                TextView textView = findViewById(R.id.textView);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://rpc-mumbai.maticvigil.com/")//Change depending on RPC Endpoint
                       //https://rpc-mumbai.maticvigil.com/
                        //https://polygon-rpc.com
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                EInterface ethInterface = retrofit.create(EInterface.class);
                String[] walletAddress = {walletAddressVal};//Change depending on wallet address
                //"0x0C5c73f1687f1BfE565F64B5480e2bd323378D43"
                //current wallet address contains no MATIC; it is empty
                Call<EInterface.GetBalanceResponse> responseCall = ethInterface.retreiveBalance(
                        new EInterface.GetBalanceRequest(
                                "2.0",
                                1,
                                "getBalance",
                                //eth_getBalance
                                walletAddress
                        )
                );


                responseCall.enqueue(new Callback<EInterface.GetBalanceResponse>() {
                    @Override
                    public void onResponse(Call<EInterface.GetBalanceResponse> call, Response<EInterface.GetBalanceResponse> response) {
                        try {
                            if (response.isSuccessful()) {
                                textView.setText("Success: " + response.body().toString());
                            } else {
                                textView.setText("Failed to access MetaMask: " + response.errorBody().string());
                            }
                        } catch (IOException exception) {
                            textView.setText(exception.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<EInterface.GetBalanceResponse> call, Throwable t) {
                        t.printStackTrace();
                        textView.setText("onFailure: " + t.getMessage());
                    }
                });
            }

        });
    }}