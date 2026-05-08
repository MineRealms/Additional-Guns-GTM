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
        private ResultEntry result;

        @SerializedName("result_item")
        private String resultItem;
        
        @SerializedName("materials")
        private List<IngredientEntry> materials = new ArrayList<>();
        
        @SerializedName("ingredients")
        private List<IngredientEntry> ingredients = new ArrayList<>();

        public String getResultString() {
            if (result != null && result.getItem() != null) {
                return result.getItem();
            }
            return resultItem;
        }
        
        public ResultEntry getResult() {
            return result;
        }

        public void setResult(ResultEntry result) {
            this.result = result;
        }
        
        public void setResultItem(String resultItem) {
            this.resultItem = resultItem;
        }

        public List<IngredientEntry> getIngredients() {
            return ingredients != null && !ingredients.isEmpty() ? ingredients : materials;
        }

        public void setIngredients(List<IngredientEntry> ingredients) {
            this.ingredients = ingredients;
        }
        
        public void setMaterials(List<IngredientEntry> materials) {
            this.materials = materials;
        }
    }

    public static class ResultEntry {
        @SerializedName("item")
        private String item;
        
        @SerializedName("tag")
        private String tag;

        public String getItem() {
            return item;
        }
        
        public String getTag() {
            return tag;
        }

        public void setItem(String item) {
            this.item = item;
        }
        
        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    public static class IngredientEntry {
        @SerializedName("item")
        private String item;
        
        @SerializedName("tag")
        private String tag;
        
        @SerializedName("count")
        private int count = 1;

        public IngredientEntry() {}

        public IngredientEntry(String item, int count) {
            this.item = item;
            this.count = count;
        }

        public String getItem() {
            return item;
        }
        
        public String getTag() {
            return tag;
        }
        
        public String getId() {
            return item != null ? item : tag;
        }

        public void setItem(String item) {
            this.item = item;
        }
        
        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}