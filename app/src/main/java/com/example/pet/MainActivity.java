package com.example.pet;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.squareup.picasso.Picasso;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    RecyclerView mRecyclerView;
    private ImageView image;
    private TextView text;
    List<Pet> pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar=(ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        image=(ImageView) findViewById(R.id.imageView);
        text=(TextView) findViewById(R.id.textView2);

        pet=new ArrayList<>();

        mRecyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mProgressBar.setVisibility(View.VISIBLE);

        PetAPI petAPI=PetAPI.retrofit.create(PetAPI.class);
        final Call<Pet> call =petAPI.getData();
        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                if (response.isSuccessful()){
                    Pet pet=response.body();
                    text.setText(pet.getName());
                    Picasso.with(MainActivity.this).load("https://cs6.pikabu.ru/post_img/big/2014/12/22/6/1419240084_1339461511.jpg").into(image);

                    mProgressBar.setVisibility(View.INVISIBLE);
                } else{
                    ResponseBody errorBody=response.errorBody();
                    try{
                        Toast.makeText(MainActivity.this, errorBody.string(),
                                Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable throwable) {
                Toast.makeText(MainActivity.this,"Что-то пошло не так "+throwable.getMessage(),
                        Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);

            }
        });

    }
}