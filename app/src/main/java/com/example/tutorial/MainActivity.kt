package com.example.tutorial

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    //val は定数 var は普通の変数
    private var my_gold : Int = 1000
    private lateinit var haveChipText : TextView
    private lateinit var yourHand : TextView
    private lateinit var enemyImage : ImageView
    private lateinit var playerImage : ImageView
    enum class Action(val id: Int,val actionName:String){
        NONE(0,"なし"),
        ROCK(1,"グー"),
        SCISSORS(2,"チョキ"),
        PAPER(3,"パー");

        companion object {
            fun valueOf(typeId:Int) : Action {
                val filtered = Action.values().filter {it.id == typeId}.firstOrNull()
                return filtered ?: throw IllegalArgumentException("no such enum object for the typeId: " + typeId)
            }
        }

    }

    enum class Result(val id: Int){
        DRAW(0),
        WIN(1),
        LOSE(2),
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //レイアウトを呼び出している
        setContentView(R.layout.activity_main)

        haveChipText = findViewById<TextView>(R.id.textView3)
        enemyImage = findViewById<ImageView>(R.id.imageView)
        playerImage = findViewById<ImageView>(R.id.PlayerImage)
        yourHand = findViewById<TextView>(R.id.textView4)
        yourHand.text = "好きな手を出してください"

        val rockButton : Button = findViewById<Button>(R.id.button)
        val scissorsButton : Button = findViewById<Button>(R.id.button2)
        val paperButton : Button = findViewById<Button>(R.id.button3)

        rockButton.setOnClickListener { onClickButton(Action.ROCK)}
        scissorsButton.setOnClickListener { onClickButton(Action.SCISSORS)}
        paperButton.setOnClickListener { onClickButton(Action.PAPER)}

        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)

    }
    private fun onClickButton(action: Action){
        val chipText : EditText = findViewById<EditText>(R.id.text_et)
        var chip : Int = 0
        if(chipText.text.length >= 1){
            yourHand.text = "あなたの出した手は " + action.actionName + "です"
            openImage(action,playerImage)
            chip = chipText.text.toString().toInt()
            my_gold -= chip
            if(chip in 1..100){
                val result : Result = playGame(action)
                val winChip : Int = chip * dividend(result)
                my_gold += winChip
            }else if( chip <= 0){
                //0以下
            }else{
                //100以上
            }
            haveChipText.text = my_gold.toString() + "G"
        }else{
            val listener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        // OKボタンがクリックされた時の処理
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        // キャンセルボタンがクリックされた時の処理
                    }
                }
            }
            AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("かける金額を入力してください")
                .setPositiveButton("OK",listener).show()
        }
    }
    private fun dividend(result: Result) : Int{
        when(result){
            Result.WIN -> return 2
            Result.DRAW -> return 1
            Result.LOSE -> return 0
            else -> return -1
        }
    }
    private fun openImage(enemyAction: Action,image: ImageView){
        when(enemyAction){
            Action.ROCK -> image.setImageResource(R.drawable.gu)
            Action.SCISSORS -> image.setImageResource(R.drawable.choki)
            Action.PAPER -> image.setImageResource(R.drawable.pa)
            else -> {}
        }
    }

    private fun playGame(action: Action) : Result{
        //アクション
        val enemyAction : Action = Action.valueOf((1..3).random())
        openImage(enemyAction,enemyImage)
        if(action == enemyAction){
            //あいこの時
            return Result.DRAW

        }else{
            when(action){
                Action.ROCK -> if(enemyAction == Action.PAPER) return Result.LOSE
                Action.SCISSORS -> if(enemyAction == Action.ROCK) return Result.LOSE
                Action.PAPER -> if(enemyAction == Action.SCISSORS) return Result.LOSE
                else -> return Result.DRAW
            }
        }
        return Result.WIN
    }
}