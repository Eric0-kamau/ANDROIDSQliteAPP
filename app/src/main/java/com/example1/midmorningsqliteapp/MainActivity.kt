package com.example1.midmorningsqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var mEdtName:EditText
    lateinit var mEdtEmail:EditText
    lateinit var mEdtNumber:EditText
    lateinit var mBtnSave:Button
    lateinit var mBtnView:Button
    lateinit var mBtnDelete:Button
    lateinit var db:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mEdtName = findViewById(R.id.mEdtName)
        mEdtEmail = findViewById(R.id.mEdtEmail)
        mEdtNumber = findViewById(R.id.mEdtNumber)
        mBtnSave  = findViewById(R.id.mBtnSave)
        mBtnView = findViewById(R.id.mBtnView)
        mBtnDelete = findViewById(R.id.mEdtDelete)
        // Create a database called eMobilisDB
        db = openOrCreateDatabase("eMobilisDB", Context.MODE_PRIVATE, null)
        // Create a table called users in the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")
        //Set onClick listeners to buttons
        mBtnSave.setOnClickListener {
            //Receive data from the var
            var name = mEdtName.text.toString().trim()
            var email = mEdtEmail.text.toString().trim()
            var number = mEdtNumber.text.toString().trim()
            //Check if the user is submitting empty fields
            if (name.isEmpty() || email.isEmpty() || number.isEmpty()){
                //Display an error message using the defined message function
                message("EMPTY FIELDS!!!", "Please fill all inputs")
            }else{
                //Proceed to save
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+email+"','"+number+"')")
                clear()
                message("SUCCESS!!!", "User saved successfully")
            }
        }
        mBtnView.setOnClickListener {
            //Use cursor to select all the records
            var cursor = db.rawQuery("SELECT * FROM users", null)
            // Check if there is any record found
            if (cursor.count == 0){
                message("NO RECORDS!!!", "Sorry, no users were found!!!")
            }else{
                //Use string buffer to append all records to be displayed using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedNumber+"\n\n")
                }
                message("USERS",buffer.toString())
            }
        }
        mBtnDelete.setOnClickListener {
            val Number = mEdtNumber.text.toString().trim()
            if (Number.isEmpty()){
                message("EMPTY FIELDS!!!", "Please fill all inputs")
            }else{
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho='"+Number+"'",null)
                if (cursor.count == 0){
                    message("NO RECORD FOUND!!!", "Sorry, there's no user with provided ID")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+Number+"'")
                    clear()
                    message("SUCCESS!!!", "User deleted successfully")
                }
            }
        }
    }

    fun message(title:String, message: String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Cancel", null)
        alertDialog.create().show()
    }
    fun clear(){
        mEdtName.setText("")
        mEdtEmail.setText("")
        mEdtNumber.setText("")
    }
}