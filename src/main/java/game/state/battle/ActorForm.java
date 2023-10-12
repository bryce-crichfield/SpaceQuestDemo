package game.state.battle;

import game.event.SubscriptionManager;
import game.form.element.FormElement;
import game.form.properties.FormBorder;
import game.form.properties.FormFill;
import game.state.battle.event.*;
import game.state.battle.world.Actor;
import game.event.Event;
import java.awt.*;
import java.util.Optional;

public class ActorForm extends FormElement {
    public final Event<Actor> actorProperty = new Event<>();
    public Optional<Actor> actor = Optional.empty();
    public ActorForm(int x, int y) {
        super(x, y, 25, 25);

        FormFill fill = new FormFill();
        fill.setPaint(Color.DARK_GRAY.darker().darker().darker());
        fill.setRoundness(25);
        setFill(fill);

        FormBorder border = new FormBorder();
        setBorder(border);

        FormElement actorTitle = new FormElement("");
        actorTitle.getBounds().setWidth(100);
        actorTitle.getBounds().setHeight(1);

        FormElement actorHealth = new FormElement("");
        actorHealth.getBounds().setWidth(100);
        actorHealth.getBounds().setHeight(1);

        FormElement actorPositionX = new FormElement("");
        actorPositionX.getBounds().setWidth(100);
        actorPositionX.getBounds().setHeight(1);

        FormElement actorPositionY = new FormElement("");
        actorPositionY.getBounds().setWidth(100);
        actorPositionY.getBounds().setHeight(1);

        actorProperty.listenWith(actor -> {
            actorTitle.getText().setValue(actor.getName());

            String health = String.valueOf(actor.getHealth());
            actorHealth.getText().setValue(health);

            String positionX = String.valueOf((int) actor.getX());
            actorPositionX.getText().setValue(positionX);

            String positionY = String.valueOf((int) actor.getY());
            actorPositionY.getText().setValue(positionY);
        });


        addChild(actorTitle);
        addChild(actorHealth);
        addChild(actorPositionX);
        addChild(actorPositionY);

        getLayout().execute(this);
    }

    private static boolean sameActor(Optional<Actor> actor, Actor other) {
        return actor.isPresent() && actor.get().equals(other);
    }

    public static void configureHovered(ActorForm form, SubscriptionManager subscribe) {
        form.setVisible(false);

        subscribe.on(ActorDamaged.event).run(attacked -> {
            if (sameActor(form.actor, attacked)) {
                form.actorProperty.fire(attacked);
            }
        });

        subscribe.on(ActorAnimated.event).run(moved -> {
            if (sameActor(form.actor, moved)) {
                form.actorProperty.fire(moved);
            }
        });

        subscribe.on(ActorHovered.event).run(hovered -> {
            form.setVisible(true);
            form.actor = Optional.of(hovered);
            form.actorProperty.fire(hovered);
        });

        subscribe.on(ActorUnhovered.event).run(unhovered -> {
            form.setVisible(false);
            form.actor = Optional.empty();
        });

        subscribe.on(ActorSelected.event).run(selected -> {
            form.setVisible(false);
            if (sameActor(form.actor, selected.getActor())) {
                form.actorProperty.fire(selected.getActor());
            }
        });
    }

    public static void configureSelected(ActorForm form, SubscriptionManager subscribe) {
        form.setVisible(false);

        subscribe.on(ActorDamaged.event).run(damaged -> {
            if (sameActor(form.actor, damaged)) {
                form.actorProperty.fire(damaged);
            }
        });

        subscribe.on(ActorAnimated.event).run(moved -> {
            if (sameActor(form.actor, moved)) {
                form.actorProperty.fire(moved);
            }
        });

        subscribe.on(ActorSelected.event).run(selected -> {
            form.setVisible(true);
            form.actor = Optional.of(selected.getActor());
            form.actorProperty.fire(selected.getActor());
        });

        subscribe.on(ActorDeselected.event).run(deselected -> {
            form.setVisible(false);
            form.actor = Optional.empty();
        });
    }

    @Override
    public void onRender(Graphics2D g2d) {
        super.onRender(g2d);
    }
}
