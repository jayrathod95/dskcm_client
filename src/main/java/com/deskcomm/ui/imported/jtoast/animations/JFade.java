package com.deskcomm.ui.imported.jtoast.animations;

import com.deskcomm.ui.imported.jtoast.stage.CustomStage;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class JFade implements JTray {

    private final Timeline showAnimation, dismissAnimation;
    private final SequentialTransition sq;
    private final CustomStage stage;
    private boolean trayIsShowing;

    /**
     * Initializes a fade type animation on a stage
     *
     * @param customStage The stage associate the fade animation with
     */
    public JFade(CustomStage customStage) {

        this.stage = customStage;


        showAnimation = setupShowAnimation();
        dismissAnimation = setupDismissAnimation();

        sq = new SequentialTransition(setupShowAnimation(), setupDismissAnimation());
    }

    /**
     * @return a constructed instance of a show fade animation
     */
    private Timeline setupShowAnimation() {

        Timeline tl = new Timeline();

        //Sets opacity to 0.0 instantly which is pretty much invisible
        KeyValue kvOpacity = new KeyValue(stage.opacityProperty(), 0.0);
        KeyFrame frame1 = new KeyFrame(Duration.ZERO, kvOpacity);

        //Sets opacity to 1.0 (fully visible) over the time of 3000 milliseconds.
        KeyValue kvOpacity2 = new KeyValue(stage.opacityProperty(), 1.0);
        KeyFrame frame2 = new KeyFrame(Duration.millis(200), kvOpacity2);

        tl.getKeyFrames().addAll(frame1, frame2);

        tl.setOnFinished(e -> trayIsShowing = true);

        return tl;
    }

    /**
     * @return a constructed instance of a dismiss fade animation
     */
    private Timeline setupDismissAnimation() {

        Timeline tl = new Timeline();

        //At this stage the opacity is already at 1.0

        //Lowers the opacity to 0.0 within 2000 milliseconds
        KeyValue kv1 = new KeyValue(stage.opacityProperty(), 0.0);
        KeyFrame kf1 = new KeyFrame(Duration.millis(100), kv1);

        tl.getKeyFrames().addAll(kf1);

        //Action to be performed when the animation has finished
        tl.setOnFinished(e -> {
            trayIsShowing = false;
            stage.close();
            stage.setLocation(stage.getBottomRight());
        });

        return tl;
    }

    /**
     * The type of animation this class plays
     *
     * @return The type of animation this class plays
     */
    @Override
    public AnimationType getAnimationType() {
        return AnimationType.FADE;
    }

    /**
     * Plays both the show and dismiss animation using a sequential transition object
     *
     * @param dismissDelay How long to delay the start of the dismiss animation
     */
    @Override
    public void playSequential(Duration dismissDelay) {
        sq.getChildren().get(1).setDelay(dismissDelay);
        sq.play();
    }

    /**
     * Plays the implemented show animation
     */
    @Override
    public void playShowAnimation() {
        showAnimation.play();
    }

    /**
     * Plays the implemented dismiss animation
     */
    @Override
    public void playDismissAnimation() {
        dismissAnimation.play();
    }

    /**
     * Signifies if the tray is current showing
     *
     * @return boolean resultant
     */
    @Override
    public boolean isShowing() {
        return trayIsShowing;
    }
}