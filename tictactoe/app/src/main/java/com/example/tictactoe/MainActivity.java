package com.example.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView[][] b = new ImageView[3][3];
    ImageView[][] fc = new ImageView[0][0];
    int counter = 0,p_1_points=0,p_2_points=0,game_num=0;
    TextView p_1,p_2;
    TextView best_score;
    Random rnd = new Random();
    Random game = new Random();
    Button reset;
    Chronometer chrono;
    Boolean running = true;
    float time_elapsed;
    DatabaseHandler db;
    Button scores;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String ImageId = "b_" + i + j;
                int resid = getResources().getIdentifier(ImageId, "id", getPackageName());
                b[i][j] = findViewById(resid);
                b[i][j].setOnClickListener(this);
                b[i][j].setTag("box");
            }
            reset = findViewById(R.id.reset);
        }

        p_1 = findViewById(R.id.p_1);
        p_2 = findViewById(R.id.p_2);
        chrono = findViewById(R.id.chrono);
        scores = findViewById(R.id.Scores);
        db = new DatabaseHandler(this);
        settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
        editor = settings.edit();
        best_score = findViewById(R.id.best_score);

        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,display.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (running){
            chrono.setTextColor(Color.WHITE);
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
            running = false;
            best_score.setText("BEST TIME : "+settings.getFloat("Score_2",0.0f ));
        }

        switch (game_num) {


            case 0:
            {
                thambee_3:
                for (int i = 0; i < 3; i++) {
                    if (((ImageView) v).getTag() == "box" && counter % 2 == 0) {
                        ((ImageView) v).setImageResource(R.drawable.x);
                        ((ImageView) v).setTag("x");
                        break thambee_3;
                    } else {
                        continue thambee_3;
                    }
                }

                if (checkforwin()) {

                    if (counter % 2 == 0) {
                        Log.d("stk", "Player x wins");
                        p_1_points++;
                        Toast.makeText(this, "Hurray you won", Toast.LENGTH_SHORT).show();

                        resetboard();
                        updatescore();
                        datasave();
                    } else {
                        Log.d("stk", "Player y wins");
                        Toast.makeText(this, "Well Played! Better luck next time", Toast.LENGTH_SHORT).show();
                        p_2_points++;
                        resetboard();
                        updatescore();
                    }
                }else {

                    counter++;

                    thambee_4:

                    if(b[1][1].getTag() =="box"){
                        b[1][1].setImageResource(R.drawable.o);
                        b[1][1].setTag("o");
                        break thambee_4;
                    } else {

                        String[][] bc = new String[3][3];

                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                bc[i][j] = b[i][j].getTag().toString();
                            }
                        }
                        for (int i = 0; i < 3; i++) {
                            if (((bc[i][0].equals(bc[i][1]))&&(bc[i][0].equals("x")))|| ((bc[i][1].equals(bc[i][2]))&&(bc[i][1].equals("x"))) || ((bc[i][0].equals(bc[i][2])&&(bc[i][2].equals("x"))))) {
                                for (int j = 0; j < 3; j++) {
                                    if (bc[i][j].equals("box")) {
                                        b[i][j].setImageResource(R.drawable.o);
                                        b[i][j].setTag("o");
                                        break thambee_4;
                                    }
                                }
                            }

                        }

                        for (int i = 0; i < 3; i++) {
                            if (((bc[0][i].equals(bc[1][i]))&&(bc[0][i].equals("x"))) || ((bc[1][i].equals(bc[2][i]))&&(bc[1][i].equals("x"))) || ((bc[0][i].equals(bc[2][i]))&&(bc[2][i].equals("x")))) {                                for (int j = 0; j < 3; j++) {
                                    if (bc[j][i].equals("box")) {
                                        b[j][i].setImageResource(R.drawable.o);
                                        b[j][i].setTag("o");
                                        break thambee_4;
                                    }
                                }
                            }

                        }

                        for (int i=0;i<3;i++){
                            if (bc[i][0].equals("x")||bc[i][1].equals("x")||bc[i][2].equals("x")){
                                for (int j = 0; j < 3; j++) {
                                    if (bc[i][j].equals("box")) {
                                        b[i][j].setImageResource(R.drawable.o);
                                        b[i][j].setTag("o");
                                        break thambee_4;
                                    }
                                }
                            }
                            if (bc[0][i].equals("x")||bc[1][i].equals("x")||bc[2][i].equals("x")){
                                for (int j = 0; j < 3; j++) {
                                    if (bc[j][i].equals("box")) {
                                        b[j][i].setImageResource(R.drawable.o);
                                        b[j][i].setTag("o");
                                        break thambee_4;
                                    }
                                }
                            }
                        }
                    }

                    if (checkforwin()) {

                        if (counter % 2 == 0) {
                            Log.d("stk", "Player x wins");
                            p_1_points++;
                            Toast.makeText(this, "Hurray you won", Toast.LENGTH_SHORT).show();
                            resetboard();
                            updatescore();
                            datasave();
                        } else {
                            Log.d("stk", "Player y wins");
                            Toast.makeText(this, "Well Played! Better luck next time", Toast.LENGTH_SHORT).show();
                            p_2_points++;
                            resetboard();
                            updatescore();
                        }
                    }else {
                        counter++;
                    }
                }

                counter=0;
                for (int i=0;i<3;i++){
                    for (int j=0;j<3;j++){
                        if(!(b[i][j].getTag()=="box")){
                            counter++;
                            if (counter>=9){
                                resetboard();
                            }
                        }
                    }
                }
                counter=0;
            }

            break;

            case 1:
            {
                thambee_3:
                for (int i = 0; i < 3; i++) {
                    if (((ImageView) v).getTag() == "box" && counter % 2 == 0) {
                        ((ImageView) v).setImageResource(R.drawable.x);
                        ((ImageView) v).setTag("x");
                        break thambee_3;
                    } else {
                        continue thambee_3;
                    }
                }

                if (checkforwin()) {

                    if (counter % 2 == 0) {
                        Log.d("stk", "Player x wins");
                        p_1_points++;
                        Toast.makeText(this, "Hurray you won", Toast.LENGTH_SHORT).show();
                        resetboard();
                        updatescore();
                        datasave();
                    } else {
                        Log.d("stk", "Player y wins");
                        Toast.makeText(this, "Well Played! Better luck next time", Toast.LENGTH_SHORT).show();
                        p_2_points++;
                        resetboard();
                        updatescore();
                    }
                }else {

                    counter++;

                    thambee_4:

                    if(b[1][1].getTag() =="box"){
                        b[1][1].setImageResource(R.drawable.o);
                        b[1][1].setTag("o");
                        break thambee_4;
                    }else if ((b[1][1].getTag() =="x")&&(b[0][0].getTag()=="box")){
                        b[0][0].setImageResource(R.drawable.o);
                        b[0][0].setTag("o");
                        break thambee_4;
                    } else if ((b[1][1].getTag()=="o" && b[0][1].getTag()=="box")&&((b[0][0].getTag()=="x"&&b[2][2].getTag()=="x")||(b[0][2].getTag()=="x"&&b[2][0].getTag()=="x"))){
                        if ((b[0][0].getTag()=="x"&&b[2][2].getTag()=="x")||(b[0][2].getTag()=="x"&&b[2][0].getTag()=="x")){
                            if (b[0][1].getTag()=="box"){
                                b[0][1].setImageResource(R.drawable.o);
                                b[0][1].setTag("o");
                            }
                        }

                    }else {

                        String[][] bc = new String[3][3];

                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                bc[i][j] = b[i][j].getTag().toString();
                            }
                        }

                        for (int i=0;i<3;i++){
                            if (((bc[i][0].equals(bc[i][1]))&&(bc[i][0].equals("o")))|| ((bc[i][1].equals(bc[i][2]))&&(bc[i][1].equals("o"))) || ((bc[i][0].equals(bc[i][2])&&(bc[i][2].equals("o"))))){
                                for (int j=0;j<3;j++){
                                    if (bc[i][j].equals("box")){
                                        b[i][j].setImageResource(R.drawable.o);
                                        b[i][j].setTag("o");
                                    }
                                }
                            }
                        }

                        if ((bc[0][0].equals("o"))&&(bc[1][1].equals("o"))){
                            if (bc[2][2].equals("box")) {
                                b[2][2].setImageResource(R.drawable.o);
                                b[2][2].setTag("o");
                                break thambee_4;
                            }
                        }

                        if ((bc[2][2].equals("o"))&&(bc[1][1].equals("o"))){
                            if (bc[0][0].equals("box")) {
                                b[0][0].setImageResource(R.drawable.o);
                                b[0][0].setTag("o");
                                break thambee_4;
                            }
                        }

                        if ((bc[0][2].equals("o"))&&(bc[1][1].equals("o"))){
                            if (bc[2][0].equals("box")) {
                                b[2][0].setImageResource(R.drawable.o);
                                b[2][0].setTag("o");
                                break thambee_4;
                            }
                        }

                        if ((bc[2][0].equals("o"))&&(bc[1][1].equals("o"))){
                            if (bc[0][2].equals("box")) {
                                b[0][2].setImageResource(R.drawable.o);
                                b[0][2].setTag("o");
                                break thambee_4;
                            }
                        }

                        for (int i = 0; i < 3; i++) {
                            if (((bc[0][i].equals(bc[1][i]))&&(bc[0][i].equals("o"))) || ((bc[1][i].equals(bc[2][i]))&&(bc[1][i].equals("o"))) || ((bc[0][i].equals(bc[2][i]))&&(bc[2][i].equals("o")))) {                                for (int j = 0; j < 3; j++) {
                                if (bc[j][i].equals("box")) {
                                    b[j][i].setImageResource(R.drawable.o);
                                    b[j][i].setTag("o");
                                    break thambee_4;
                                }
                            }
                            }

                        }



                        Log.d("stk","next");
                        for (int i = 0; i < 3; i++) {
                            if (((bc[i][0].equals(bc[i][1]))&&(bc[i][0].equals("x")))|| ((bc[i][1].equals(bc[i][2]))&&(bc[i][1].equals("x"))) || ((bc[i][0].equals(bc[i][2])&&(bc[i][2].equals("x"))))) {
                                for (int j = 0; j < 3; j++) {
                                    if (bc[i][j].equals("box")) {
                                        b[i][j].setImageResource(R.drawable.o);
                                        b[i][j].setTag("o");
                                        break thambee_4;
                                    }
                                }
                            }

                        }

                        for (int i = 0; i < 3; i++) {
                            if (((bc[0][i].equals(bc[1][i]))&&(bc[0][i].equals("x"))) || ((bc[1][i].equals(bc[2][i]))&&(bc[1][i].equals("x"))) || ((bc[0][i].equals(bc[2][i]))&&(bc[2][i].equals("x")))) {                                for (int j = 0; j < 3; j++) {
                                if (bc[j][i].equals("box")) {
                                    b[j][i].setImageResource(R.drawable.o);
                                    b[j][i].setTag("o");
                                    break thambee_4;
                                }
                            }
                            }

                        }

                        if ((bc[0][0].equals("x"))&&(bc[1][1].equals("x"))){
                            if (bc[2][2].equals("box")) {
                                b[2][2].setImageResource(R.drawable.o);
                                b[2][2].setTag("o");
                                break thambee_4;
                            }
                        }

                        if ((bc[2][2].equals("x"))&&(bc[1][1].equals("x"))){
                            if (bc[0][0].equals("box")) {
                                b[0][0].setImageResource(R.drawable.o);
                                b[0][0].setTag("o");
                                break thambee_4;
                            }
                        }

                        if ((bc[0][2].equals("x"))&&(bc[1][1].equals("x"))){
                            if (bc[2][0].equals("box")) {
                                b[2][0].setImageResource(R.drawable.o);
                                b[2][0].setTag("o");
                                break thambee_4;
                            }
                        }

                        if ((bc[2][0].equals("x"))&&(bc[1][1].equals("x"))){
                            if (bc[0][2].equals("box")) {
                                b[0][2].setImageResource(R.drawable.o);
                                b[0][2].setTag("o");
                                break thambee_4;
                            }
                        }

                        for (int i=0;i<3;i++){
                            if (bc[i][0].equals("x")||bc[i][1].equals("x")||bc[i][2].equals("x")){
                                for (int j = 0; j < 3; j++) {
                                    if (bc[i][j].equals("box")) {
                                        b[i][j].setImageResource(R.drawable.o);
                                        b[i][j].setTag("o");
                                        break thambee_4;
                                    }
                                }
                            }
                            if (bc[0][i].equals("x")||bc[1][i].equals("x")||bc[2][i].equals("x")){
                                for (int j = 0; j < 3; j++) {
                                    if (bc[j][i].equals("box")) {
                                        b[j][i].setImageResource(R.drawable.o);
                                        b[j][i].setTag("o");
                                        break thambee_4;
                                    }
                                }
                            }
                        }
                    }

                    if (checkforwin()) {

                        if (counter % 2 == 0) {
                            Log.d("stk", "Player x wins");
                            p_1_points++;
                            Toast.makeText(this, "Hurray you won", Toast.LENGTH_SHORT).show();
                            resetboard();
                            updatescore();
                            datasave();
                        } else {
                            Log.d("stk", "Player y wins");
                            Toast.makeText(this, "Well Played! Better luck next time", Toast.LENGTH_SHORT).show();
                            p_2_points++;
                            resetboard();
                            updatescore();
                        }
                    }else {
                        counter++;
                    }
                }

                counter=0;
                for (int i=0;i<3;i++){
                    for (int j=0;j<3;j++){
                        if(!(b[i][j].getTag()=="box")){
                            counter++;
                            if (counter>=9){
                                resetboard();
                            }
                        }
                    }
                }
                counter=0;
            }

            break;



        }

    }

    private void updatescore() {
        p_1.setText("Player X : "+p_1_points);
        p_2.setText("Player O : "+p_2_points);

    }

    private void resetboard() {
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                b[i][j].setImageResource(R.drawable.b);
                b[i][j].setTag("box");
                game_num = game.nextInt(2);
            }
        }
        counter=0;
        chrono.stop();
        chrono.setTextColor(Color.RED);
        time_elapsed =  (SystemClock.elapsedRealtime()-chrono.getBase());
        running = true;
    }

    private void datasave(){
        boolean insert = db.addScore(time_elapsed/1000);
        Log.d("stk",""+time_elapsed);
        if (insert==true){
            Toast.makeText(MainActivity.this,"Succes",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,"Sorry",Toast.LENGTH_SHORT).show();
        }

        editor.putFloat("Score_1",10.0f);
        float current = settings.getFloat("Score_2",100.0f);
        if ((time_elapsed/1000)<current){
            editor.putFloat("Score_2",time_elapsed/1000);
            editor.commit();
            Log.d("stk","hello "+settings.getFloat("Score_2",0.0f));
            best_score.setText("BEST TIME : "+settings.getFloat("Score_2",0.0f));
        }else {
            Log.d("stk",""+(time_elapsed/1000));
            Log.d("stk","hello "+settings.getFloat("Score_2",0.0f));
        }

    }

    private boolean checkforwin() {

        String[][] by = new String[3][3];

        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                by[i][j] = b[i][j].getTag().toString();
            }

        }
        for (int i=0;i<3;i++){
            if (by[i][0].equals(by[i][1])&&by[i][1].equals(by[i][2])&&!by[i][1].equals("box")){
                return true;
            }
        }

        for (int i=0;i<3;i++){
            if (by[0][i].equals(by[1][i])&&by[1][i].equals(by[2][i])&&!by[0][i].equals("box")){
                return true;
            }
        }

        if(by[0][0].equals(by[1][1])&&by[1][1].equals(by[2][2])&&!by[0][0].equals("box")){
            return true;
        }

        if(by[0][2].equals(by[1][1])&&by[1][1].equals(by[2][0])&&!by[1][1].equals("box")){
            return true;
        }
        return false;
    }

    public void perform(View view) {
        resetboard();
        p_2_points=0;
        p_1_points=0;
        updatescore();
        chrono.setTextColor(Color.WHITE);
        chrono.setText("00:00");
        db.clearall();
    }

    public void chrono(View view) {

    }

}


