package haloofblocks.additionalguns.client.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RecipeEditorScreen extends Screen {
    private final Minecraft minecraft;

    public RecipeEditorScreen() {
        super(Component.literal("Recipe Editor"));
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
    }
}