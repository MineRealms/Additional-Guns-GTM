package haloofblocks.additionalguns.config;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class RecipeConfigData {
    @SerializedName("recipes")
    private List<RecipeEntry> recipes = new ArrayList<>();

    public List<RecipeEntry> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeEntry> recipes) {
        this.recipes = recipes;
    }

    public static class RecipeEntry {
        @SerializedName("result")
        private String result;

        @SerializedName("ingredients")
        private List<IngredientEntry> ingredients = new ArrayList<>();

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public List<IngredientEntry> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<IngredientEntry> ingredients) {
            this.ingredients = ingredients;
        }
    }

    public static class IngredientEntry {
        @SerializedName("item")
        private String item;

        @SerializedName("count")
        private int count;

        public IngredientEntry() {}

        public IngredientEntry(String item, int count) {
            this.item = item;
            this.count = count;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}