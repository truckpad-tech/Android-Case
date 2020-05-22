package com.example.truckpad
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

class DataBase {
        protected lateinit var db:SQLiteDatabase
        private var name:String  = "TruckPAD_DB"
        private var Info:String = "CREATE TABLE IF NOT EXISTS INFORMATION(IDINFO INTEGER PRIMARY KEY AUTOINCREMENT, LAT_INI DOUBLE NOT NULL, LONG_INI DOUBLE NOT NULL, LAT_FIM DOUBLE NOT NULL, LONG_FIM DOUBLE NOT NULL, FUEL_COMS INTEGER NOT NULL, FUEL_PRICE DOUBLE NOT NULL, AXIS INTEGER NOT NULL, RETURN_SHIPMENT INTEGER NOT NULL);"
        private lateinit var ctx:Context

    constructor(ctx: Context) {
        this.ctx = ctx
        db = this.ctx.openOrCreateDatabase(name,Context.MODE_PRIVATE,null)
        this.db.execSQL(Info);
        Log.i("DATABASE","Conexão com o BD estabelecida.")
    }

        fun Insert(tabela:String, valores:ContentValues):Long{
            var id = db.insert("INFORMATION",null,valores);
            Log.i("DATABASE","Cadastrou registro em "+ tabela + " com o id ["+id+"]")
            return id
        }

        fun Search(colunas:Array<String>,where:String,OrderBy:String):Cursor{
            var c:Cursor

            if(!where.equals("")){
                c = db.query("INFORMATION",colunas,where,null,null,null,OrderBy)
            }
            else {
                c = db.query("INFORMATION",colunas,where,null,null,null,OrderBy)
            }

            Log.i("DATABASE","Realizou uma busca em "+ this.Info +" e retornou ["+c.getCount()+"] registros.")
            return c
        }

        fun Close(){
            if(db!=null){
                db.close();
                Log.i("DATABASE","Fechou o BD.")
            }
        }

}