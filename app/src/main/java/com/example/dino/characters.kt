package com.example.dino

/* Here is the file with the class of characters ( the "dino" who will run) i would like to make a super-class to create a  general character
and then to make two subclass dinosaure for the character dino and tiger for character tiger.
i want to use the "Heritage and polymorphisme" on this part of the code.
That's why i create for example a function "move" on the super class that we will redefine it on dinosaure and tiger (for example changing the speed)
For the rules of the character i want him when we press anywhere on the screen the character jump over the obstacle.

 */

open class characters() {
    open fun move(){

    }

}



class dinosaure() : characters() {
    override fun move(){

    }

}



class tiger() : characters() {

    override fun move(){

    }


}