package com.example.dbex;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    EditText edtName,edtNum,edtSex,edtTem;
    Button btnreset,btninput,btnlookup,btnRevise,btnDelete;
    TextView tvName,tvadd,tvSex,tvTem;


    MyDBHelper myHelper;    //8.DB생성, 테이블 생성을 담당할 객체 선언
    SQLiteDatabase sqlDB;   //9. 4대쿼리(insert,select,update,delete) 사용할 변수 선언
    SQLiteDatabase sqlDB1;
    SQLiteDatabase sqlDB2;
    SQLiteDatabase sqlDB3;
    SQLiteDatabase sqlDB4;
    String myTable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();   //액션바 가리기

        edtName = (EditText)findViewById(R.id.edtName);
        edtNum =(EditText)findViewById(R.id.edtNum);
        btnreset = (Button)findViewById(R.id.btnreset);
        btninput=(Button)findViewById(R.id.btninput);
        btnlookup = (Button)findViewById(R.id.btnlookup);
        btnRevise=(Button)findViewById(R.id.btnRevise);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        tvName =(TextView)findViewById(R.id.tvName);
        tvadd = (TextView)findViewById(R.id.tvadd);
        edtSex= (EditText)findViewById(R.id.edtSex);
        edtTem=(EditText)findViewById(R.id.edtTem);
        tvSex=(TextView)findViewById(R.id.tvSex);
        tvTem=(TextView)findViewById(R.id.tvTem);


        myHelper = new MyDBHelper(this);
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();   //.getWritableDatabase();  읽고 쓰기용 DB 열기
                myHelper.onUpgrade(sqlDB,1,2);  //10.인수 3가지  (실제db변수,기존버전번호,새로운버전번호)
                sqlDB.close();//11.db는 열면 반드시 닫아야 한다.
                Toast.makeText(getApplicationContext(),"초기화되었습니다..",Toast.LENGTH_SHORT).show();
            }
        });

        btninput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                sqlDB1.execSQL("INSERT INTO groupTBL VALUES ('소방차',3);");

                String no1 = edtName.getText().toString();
                String no2 = edtNum.getText().toString();
                String no3 = edtSex.getText().toString();
                String no4 = edtTem.getText().toString();

                sqlDB = myHelper.getWritableDatabase();
                if((no1.equals(""))|| (no2.equals('0') || no2.equals(null) || no2.equals(""))||(no3.equals('0') || no3.equals(null) || no3.equals(""))||(no4.equals('0') || no4.equals(null) || no4.equals(""))) {
                    Toast.makeText(getApplicationContext(),"이름 또는 인원수를 입력하시오",Toast.LENGTH_SHORT).show();
                }else {

                    sqlDB.execSQL("INSERT INTO groupTBL VALUES ('" + no1 + "','" + no2 + "','" + no3 + "','" + no4 + "');" );
                    //12. .execSQL 쿼리를 담당하는 메소드 (insert,Update,delete 3가지 쿼리만 담당)
                    sqlDB.close();
                    edtName.setText("");  //에디트 텍스트 란을 공란으로 만들기 위해 만듬
                    edtNum.setText("");
                    edtSex.setText("");
                    edtTem.setText("");
                    Toast.makeText(getApplicationContext(),"데이터가 입력되었습니다.",Toast.LENGTH_SHORT).show();
                    btnlookup.callOnClick();
                }

            }

        });

        btnlookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getReadableDatabase(); //읽기전용
                Cursor cursor;  //Cursor  인터페이스 :
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;",null);  //groupTBL 테이블 에 필드전체를 조회한다. 조건은 부여하지 않았으므로 null. 조건을 부여하고 싶다면  " " 안에 WHERE 입력 후 조건 부여한다.
                // .rawQuery 쿼리를 담당하는 메소드2 ( Select 쿼리담당)

                String strNames = "그룹이름 \n-------------\n "; //"\r\n" 다음 행으로 넘겨줌
                String strNumbers = "인원 \n-------------\n ";   //"\r\n" 다음 행으로 넘겨줌
                String strSex = "성별 \n-------------\n ";
                String strTem = "소속 \n-------------\n ";

                while (cursor.moveToNext()) { //cursor.moveToNext()
                    strNames+=cursor.getString(0) + "\n";  //숫자는 열을 의미한다 0열
                    strNumbers += cursor.getInt(1)+ "\n";  //숫자는 열을 의미한다 1열
                    strSex += cursor.getString(2)+ "\n";
                    strTem+= cursor.getString(3)+ "\n";

                }

                tvName.setText(strNames);
                tvadd.setText(strNumbers);
                tvSex.setText(strSex);
                tvTem.setText(strTem);

                cursor.close();
                sqlDB.close();
            }
        });

        btnRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                myTable = "groupTBL";
                String no1 = "'" + edtName.getText().toString() + "'";
                String no2 = edtNum.getText().toString();
                String no3 = "'" + edtSex.getText().toString() + "'";
                String no4 = "'" + edtTem.getText().toString() + "'";

                if((no1.equals(""))|| (no2.equals('0') || no2.equals(null) || no2.equals(""))||(no3.equals('0') || no3.equals(null) || no3.equals(""))||(no4.equals('0') || no4.equals(null) || no4.equals(""))) {
                    Toast.makeText(getApplicationContext(),"이름 또는 인원수를 입력하시오",Toast.LENGTH_SHORT).show();
                }else {
                    String sqlUpdate = "UPDATE " + myTable + " SET gName = " + no1 + ", gNumber = " + no2 + ", gSex = " + no3 + ", gTem = " + no4 + " WHERE gName = " + no1 + ";";

                    sqlDB.execSQL(sqlUpdate);
                    edtName.setText("");
                    edtNum.setText("");
                    edtSex.setText("");
                    edtTem.setText("");
//                Toast.makeText(getApplicationContext(), "그룹명 : " + myFieldValue1 + "의 인원이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    sqlDB.close();
                    btnlookup.callOnClick();

                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                myTable = "groupTBL";
                String myFieldValue1 = "'" + edtName.getText().toString() + "'";
                String myFieldValue2 = edtNum.getText().toString();
                String sqlDelete = "DELETE FROM " + myTable + " WHERE gname = " + myFieldValue1 + ";";
                Log.d("SQL DELETE Statement : ", sqlDelete);
                sqlDB.execSQL(sqlDelete);
                edtName.setText("");
                edtNum.setText("");
                sqlDB.close();

                btnlookup.callOnClick();
            }
        });

    }

    //1.SQLiteOpenHelper 클래스를 상속받은 클래스 생성 후에, 메소드 생성 및 생성자 생성 (빨간 줄 표기 됨)

    public class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper( Context context) {  //2.Context context 만 제외하고 전부 지운다. Context(장소)를 제외한 나머지는 상속받을 것이 아니기 때문에 지움. 나머지는 내가 만들어서 사용한다.
            super(context,"groupDB2",null,1); //3.super (); 안에도 수정한다.  생성자에서 DB를 생성한다. (context-상속,  ""-db이름,  외부에서 받을 것,  내가만든 버전수)
        }

        @Override
        public void onCreate(SQLiteDatabase db) { //4. onCreate 메소드는 테이블을 생성하는 메소드 이다. ★★
            db.execSQL("CREATE TABLE groupTBL (gName TEXT  PRIMARY KEY, gNumber INTEGER, gSex TEXT, gTem TEXT);" );  //5. db를 생성하는 메소드    db.execSQL("CREATE TABLE 생성할 테이블 이름 (필드명 타입, 필드명타입);" );  지금의 경우 필드가 2개이다.  PRIMARY KEY는 고유값 설정
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");    //6. 테이블 삭제하는 명령어  DROP TABLE
            onCreate(db);  //7. 삭제 후 다시 생성하기 위해 onCreate 메소드 소환

        }
    }
}
