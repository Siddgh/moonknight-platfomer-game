package com.sid.constants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class UIConstants {
    public static final String PLAY_PROMPT = "Play";
    public static final String USERNAME_PROMPT = "Username...";
    public static final String WELCOME_PROMPT = "Speed Runner";


    public static Label.LabelStyle getLabelStyleForSizeAndColor(int size, Color color) {
        return new Label.LabelStyle(FontConstants.getBitmapFontFromFonts(size, FontConstants.REGGAEONE), color);
    }
}
