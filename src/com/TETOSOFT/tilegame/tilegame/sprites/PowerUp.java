package com.TETOSOFT.tilegame.tilegame.sprites;

import java.lang.reflect.Constructor;

import com.TETOSOFT.tilegame.graphics.Sprite;

/**
    A PowerUp class is a Sprite that the player can pick up.
*/
public abstract class PowerUp extends Sprite {

    public PowerUp(com.TETOSOFT.tilegame.graphics.Animation anim) {
        super(anim);
    }

    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(
                new Object[] {(com.TETOSOFT.tilegame.graphics.Animation)anim.clone()});
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }


    /**
        A Star PowerUp. Gives the player points.
    */
    public static class Star extends PowerUp {
        public Star(com.TETOSOFT.tilegame.graphics.Animation anim) {
            super(anim);
        }
    }


    /**
        A Music PowerUp. Changes the game music.
    */
    public static class Music extends PowerUp {
        public Music(com.TETOSOFT.tilegame.graphics.Animation anim) {
            super(anim);
        }
    }


    /**
        A Goal PowerUp. Advances to the next map.
    */
    public static class Goal extends PowerUp {
        public Goal(com.TETOSOFT.tilegame.graphics.Animation anim) {
            super(anim);
        }
    }

}
