package ru.zipta.cardisplay2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * Created by helljump on 05.02.16.
 */
public class Meter extends Actor {

    float value;
    float currentValue;
    StateMachine<Meter, MeterState> stateMachine;

    public Meter(){
        stateMachine = new DefaultStateMachine<Meter, MeterState>(this, MeterState.GROW);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateMachine.update();
    }

    enum MeterState implements State<Meter> {
        GROW(){
            @Override
            public void enter(Meter entity) {
                entity.value = 200;
            }

            @Override
            public void update(Meter entity) {
                if( entity.value > entity.currentValue ){
                    entity.currentValue += 200 * Gdx.graphics.getDeltaTime();
                } else {
                    entity.stateMachine.changeState(LOW);
                }
            }

            @Override
            public void exit(Meter entity) {

            }

            @Override
            public boolean onMessage(Meter entity, Telegram telegram) {
                return false;
            }
        },

        LOW(){
            @Override
            public void enter(Meter entity) {
                entity.value = 0;
            }

            @Override
            public void update(Meter entity) {
                if( entity.value < entity.currentValue ){
                    entity.currentValue -= 200 * Gdx.graphics.getDeltaTime();
                } else {
                    entity.stateMachine.changeState(WAIT);
                    entity.currentValue = 0;
                }
            }

            @Override
            public void exit(Meter entity) {

            }

            @Override
            public boolean onMessage(Meter entity, Telegram telegram) {
                return false;
            }
        },

        WAIT(){
            @Override
            public void enter(Meter entity) {

            }

            @Override
            public void update(Meter entity) {
                entity.currentValue = entity.value;
            }

            @Override
            public void exit(Meter entity) {

            }

            @Override
            public boolean onMessage(Meter entity, Telegram telegram) {
                return false;
            }
        }
    }

}
