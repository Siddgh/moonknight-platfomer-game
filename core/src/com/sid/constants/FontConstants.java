package com.sid.constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontConstants {
    public static final String REGGAEONE = "ReggaeOne.ttf";

    public static BitmapFont getBitmapFontFromFonts(int size, String fontName) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();
        return font;
    }
}
