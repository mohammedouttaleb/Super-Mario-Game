package com.TETOSOFT.tilegame.sprites;

import com.TETOSOFT.graphics.Animation;

/**
    The Player.
*/
public class Player extends Creature 
{


    private static final float JUMP_SPEED = -.95f;

    private static float effectiveJumpSpeed = JUMP_SPEED;
    private static long jumpTimer = 1;
    private boolean isMidJump = false;

    private static boolean onGround;

    public Player(Animation left, Animation right, Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }


//    @Override
//    public boolean isFlying() {
//        return isMidJump;
//    }



    public void fly() {
        //long startTime = System.currentTimeMillis();
        if (onGround) {
            onGround = false;
            this.jumpTimer = System.currentTimeMillis();
        }
        long currentTime = System.currentTimeMillis();
        System.out.println(jumpTimer + "ms --> " + currentTime + "ms");
        System.out.println(currentTime - jumpTimer);
//        jumpTimer -= (currentTime - jumpTimer);
        if (currentTime - jumpTimer <= 450) {
            setVelocityY(JUMP_SPEED);
            isMidJump = true;
        }
        else {
            isMidJump = false;
            return;
        }
    }

    public void collideHorizontal() {
        setVelocityX(0);
    }


    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }


    public void setY(float y) {
        // check if falling
        if (Math.round(y) > Math.round(getY())) {
            onGround = false;
        }
        super.setY(y);
    }


    public void wakeUp() {
        // do nothing
    }


    /**
        Makes the player jump if the player is on the ground or
        if forceJump is true.
    */
    public void jump(boolean forceJump) {
        if (onGround || forceJump) {
            onGround = false;
            setVelocityY(JUMP_SPEED);
        }
    }


    public float getMaxSpeed() {
        return 0.5f;
    }
	public static boolean isOnGround() {
		return onGround;
	}


	public static void setOnGround(boolean onGround) {
		Player.onGround = onGround;
	}


}
