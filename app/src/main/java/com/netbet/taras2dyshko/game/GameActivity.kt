package com.netbet.taras2dyshko.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.netbet.taras2dyshko.R
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class GameActivity : AppCompatActivity() {

    private lateinit var board: RelativeLayout
    private lateinit var border: RelativeLayout
    private lateinit var buttons: LinearLayout
    private lateinit var upButton: Button
    private lateinit var downButton: Button
    private lateinit var leftButton: Button
    private lateinit var rightButton: Button
    private lateinit var pauseButton: Button
    private lateinit var newGame: Button
    private lateinit var resume: Button
    private lateinit var playAgain: Button
    private lateinit var score: Button
    private lateinit var score2: Button
    private lateinit var imageAvatar: ImageView
    private var scorex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        initView()
        val meat = ImageView(this)
        val snake = ImageView(this)
        val snakeSegments =
            mutableListOf(snake) // Create a list of snake segments
        val handler = Handler()
        var delayMillis = 30L // Set delay value
        var currentDirection = "right" // Set current direction


        board.visibility = View.INVISIBLE
        playAgain.visibility = View.INVISIBLE
        score.visibility = View.INVISIBLE
        score2.visibility = View.INVISIBLE
        turnOffBottomPanel()
        newGame.setOnClickListener {
            board.visibility = View.VISIBLE
            newGame.visibility = View.INVISIBLE
            resume.visibility = View.INVISIBLE
            score2.visibility = View.VISIBLE
            imageAvatar.visibility = View.INVISIBLE
            turnOnBottomPanel()
            snake.setImageResource(R.drawable.snake)
            snake.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            board.addView(snake)
            snakeSegments.add(snake) // Add the new snake segment to the list


            var snakeX = snake.x
            var snakeY = snake.y


            meat.setImageResource(R.drawable.meat)
            meat.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            board.addView(meat)

            val random = Random() // create a Random object
            val randomX =
                random.nextInt(801) - 400 // generate a random x-coordinate between -400 and 400
            val randomY =
                random.nextInt(801) - 400 // generate a random y-coordinate between -400 and 400


            meat.x = randomX.toFloat()
            meat.y = randomY.toFloat()


            fun checkFoodCollision() {
                val distanceThreshold = 50

                val distance = sqrt((snake.x - meat.x).pow(2) + (snake.y - meat.y).pow(2))

                if (distance < distanceThreshold) { // Check if the distance between the snake head and the meat is less than the threshold

                    val newSnake =
                        ImageView(this) // Create a new ImageView for the additional snake segment
                    newSnake.setImageResource(R.drawable.snake)
                    newSnake.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    board.addView(newSnake)

                    snakeSegments.add(newSnake) // Add the new snake segment to the list

                    val randomX =
                        random.nextInt(801) - -100
                    val randomY =
                        random.nextInt(801) - -100


                    meat.x = randomX.toFloat()
                    meat.y = randomY.toFloat()


                    delayMillis-- // Reduce delay value by 1
                    scorex++

                    score2.text = "SCORE - " + scorex.toString() // Update delay text view


                }
            }
            val runnable = object : Runnable {
                override fun run() {

                    var speed = 8

                    for (i in snakeSegments.size - 1 downTo 1) { // Update the position of each snake segment except for the head
                        snakeSegments[i].x = snakeSegments[i - 1].x
                        snakeSegments[i].y = snakeSegments[i - 1].y
                    }


                    when (currentDirection) {
                        "up" -> {
                            snakeY -= speed
                            if (snakeY < -490) { // Check if the ImageView goes off the top of the board
                                snakeY = -490f
                                border.setBackgroundColor(getResources().getColor(R.color.red))
                                playAgain.visibility = View.VISIBLE
                                currentDirection = "pause"
                                buttons.visibility = View.INVISIBLE
                                pauseButton.visibility = View.INVISIBLE
                                setScore()
                            }

                            snake.translationY = snakeY
                        }

                        "down" -> {
                            snakeY += speed
                            val maxY =
                                board.height / 2 - snake.height + 30 // Calculate the maximum y coordinate
                            if (snakeY > maxY) { // Check if the ImageView goes off the bottom of the board
                                snakeY = maxY.toFloat()
                                border.setBackgroundColor(getResources().getColor(R.color.red))
                                playAgain.visibility = View.VISIBLE
                                currentDirection = "pause"
                                buttons.visibility = View.INVISIBLE
                                pauseButton.visibility = View.INVISIBLE
                                setScore()
                            }
                            snake.translationY = snakeY
                        }

                        "left" -> {
                            snakeX -= speed
                            if (snakeX < -490) { // Check if the ImageView goes off the top of the board
                                snakeX = -490f
                                border.setBackgroundColor(getResources().getColor(R.color.red))
                                playAgain.visibility = View.VISIBLE
                                currentDirection = "pause"
                                buttons.visibility = View.INVISIBLE
                                pauseButton.visibility = View.INVISIBLE
                                setScore()
                            }
                            snake.translationX = snakeX
                        }

                        "right" -> {
                            snakeX += speed
                            val maxX =
                                board.height / 2 - snake.height + 30 // Calculate the maximum y coordinate
                            if (snakeX > maxX) { // Check if the ImageView goes off the bottom of the board
                                snakeX = maxX.toFloat()
                                border.setBackgroundColor(getResources().getColor(R.color.red))
                                playAgain.visibility = View.VISIBLE
                                currentDirection = "pause"
                                buttons.visibility = View.INVISIBLE
                                pauseButton.visibility = View.INVISIBLE
                                setScore()
                            }
                            snake.translationX = snakeX
                        }

                        "pause" -> {
                            snakeX += 0
                            snake.translationX = snakeX
                        }
                    }

                    checkFoodCollision()
                    handler.postDelayed(this, delayMillis)
                }
            }

            handler.postDelayed(runnable, delayMillis)

// Set button onClickListeners to update the currentDirection variable when pressed
            upButton.setOnClickListener {
                currentDirection = "up"
            }
            downButton.setOnClickListener {
                currentDirection = "down"
            }
            leftButton.setOnClickListener {
                currentDirection = "left"
            }
            rightButton.setOnClickListener {
                currentDirection = "right"
            }
            pauseButton.setOnClickListener {
                currentDirection = "pause"
                board.visibility = View.INVISIBLE
                newGame.visibility = View.VISIBLE
                resume.visibility = View.VISIBLE
                imageAvatar.visibility = View.VISIBLE
                turnOffBottomPanel()
            }
            resume.setOnClickListener {
                currentDirection = "right"
                board.visibility = View.VISIBLE
                newGame.visibility = View.INVISIBLE
                resume.visibility = View.INVISIBLE
                imageAvatar.visibility = View.INVISIBLE
                turnOnBottomPanel()
            }
            playAgain.setOnClickListener {

                recreate()
            }

        }


    }

    fun setScore() {
        score.text = "SCORE - $scorex"
        score.visibility = View.VISIBLE
        score2.visibility = View.INVISIBLE
    }

    fun turnOffBottomPanel() {
        upButton.visibility = View.INVISIBLE
        downButton.visibility = View.INVISIBLE
        leftButton.visibility = View.INVISIBLE
        rightButton.visibility = View.INVISIBLE
        score2.visibility = View.INVISIBLE
        pauseButton.visibility = View.INVISIBLE
    }

    fun turnOnBottomPanel() {
        upButton.visibility = View.VISIBLE
        downButton.visibility = View.VISIBLE
        leftButton.visibility = View.VISIBLE
        rightButton.visibility = View.VISIBLE
        score2.visibility = View.VISIBLE
        pauseButton.visibility = View.VISIBLE
    }


    fun initView() {
        board = findViewById(R.id.board)
        border = findViewById(R.id.relativeLayout)
        buttons = findViewById(R.id.buttons)
        upButton = findViewById(R.id.up)
        downButton = findViewById(R.id.down)
        leftButton = findViewById(R.id.left)
        rightButton = findViewById(R.id.right)
        pauseButton = findViewById(R.id.pause)
        newGame = findViewById(R.id.newGame)
        resume = findViewById(R.id.resume)
        playAgain = findViewById(R.id.playAgain)
        score = findViewById(R.id.score)
        score2 = findViewById(R.id.score2)
        imageAvatar = findViewById(R.id.imageAvatar)
    }

    //add Intent
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, GameActivity::class.java))
        }
    }

}