package common.jcode.files.randomaccesSample.pojos;

import java.util.List;

public final class Cake
{

						private String cakeName,description;
						private List<String> ingredients;
						private float price;

						public Cake()
						{}

						public Cake(String cakeName,String description,List<String> ingredients,float price)
						{
												this.cakeName = cakeName;
												this.description = description;
												this.ingredients = ingredients;
												this.price = price;
						}

						public String getCakeName()
						{
												return cakeName;
						}

						public String getDescription()
						{
												return description;
						}

						public List<String> getIngredients()
						{
												return ingredients;
						}

						public float getPrice()
						{
												return price;
						}

						public void setCakeName(String cakeName)
						{
												this.cakeName = cakeName;
						}

						public void setDescription(String description)
						{
												this.description = description;
						}

						public void setIngredients(List<String> ingredients)
						{
												this.ingredients = ingredients;
						}

						public void setPrice(float price)
						{
												this.price = price;
						}

						@Override
						public String toString()
						{
												StringBuilder builder = new StringBuilder();
												builder.append("Cake [cakeName=");
												builder.append(cakeName);
												builder.append(", description=");
												builder.append(description);
												builder.append(", ingredients=");
												ingredients.forEach(ingredient-> builder.append(ingredient)
																																																				.append(" "));
												builder.append(", price=");
												builder.append(price);
												builder.append("]");
												return builder.toString();
						}

}
