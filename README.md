# FlappyBird
This is a simple FlappyBird game.
The BackGround class refers to the moving background and the floor.
It contains a constructor that builds the new BackGround in every frame when we we call it.The BackGround is moved by using the concept of parallax scrolling
Bird Class refers to the bird object and it takes input from touch and updates the new position of bird every frame based on the values of gravity and initial velocity.
Pipe class is responsible for creating pipes and moving them with given velocity we set in the beginning.
HUD is buttons class that lets user to restart or quit the game when it reaches gameover.
parallaxActivity class sets display and parallaxView draws on the screen calling constructors from all other classes  
