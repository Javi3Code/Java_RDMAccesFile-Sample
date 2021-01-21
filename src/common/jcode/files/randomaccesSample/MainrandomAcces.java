package common.jcode.files.randomaccesSample;

import java.io.File;
import java.io.IOException;
import java.util.List;

import common.jcode.files.randomaccesSample.pojos.Cake;
import common.jcode.files.randomaccesSample.randomacces.RandomAccessCakesManager;

public final class MainrandomAcces
{

      private static final String BUNDT_CAKE = "BundtCake";
      private static final String BUNDT_DESC = "procedente de Estados Unidos, es un bizcocho que se hornea en molde especial que recibe el nombre "
                              + "de bundt, dando una forma redonda con agujero "
                              + "y del cual podemos encontrar infinidad de acabados artísticos que hacen de este bizcocho un aspecto muy original";

      private static final String CHEESE_CAKE = "CheeseCake";
      private static final String CHEESE_DESC = "Es uno de los más conocidos por todos y es “pastel de queso” pero aquí también "
                              + "podemos encontrar infinidad de recetas y terminaciones. ";

      private static final String DRIP_CAKE = "Dripcake";
      private static final String DRIP_DESC = "Suelen ser tarta con altura para que visualmente queden más bonitas, y las que predomina "
                              + "un gotero por todo el alrededor.";

      private static final String CHOCOLATE = "chocolate";
      private static final String BISCUIT = "bizcocho";
      private static final String SUGAR = "azúcar";
      private static final String CHEESE = "queso";
      private static final String PIECE_OF_FRUIT = "trozos de fruta";
      private static final String CANDY = "caramelos";

      public static void main(String[] args)
      {
            var indexFile = new File("src" + File.separatorChar + "resources" + File.separatorChar + "index" + File.separatorChar
                                    + "index.properties");
            var file = new File("cakes.cakes");
            var cakeManager = new RandomAccessCakesManager(file,indexFile);

            try
            {
                  cakeManager.save(new Cake(BUNDT_CAKE,BUNDT_DESC,List.of(SUGAR,BISCUIT,CHOCOLATE,PIECE_OF_FRUIT),10.4f));
                  cakeManager.save(new Cake(CHEESE_CAKE,CHEESE_DESC,List.of(SUGAR,BISCUIT,CHEESE),13.4f));
                  cakeManager.save(new Cake(DRIP_CAKE,DRIP_DESC,List.of(SUGAR,BISCUIT,CANDY),8.4f));

                  System.out.println(cakeManager.get(BUNDT_CAKE));

                  cakeManager.save(BUNDT_CAKE,new Cake(BUNDT_CAKE,"",List.of(SUGAR,BISCUIT,CHOCOLATE,PIECE_OF_FRUIT),10.4f));

                  System.out.println(cakeManager.get(BUNDT_CAKE));

                  Cake cake = new Cake("PastelDeCarne","Pastel De Carne",List.of("hojaldre","carne"),1.9f);
                  System.out.println(cakeManager.save("PastelDeCarne",cake));

            }
            catch (IOException ex)
            {
                  System.out.println(ex.getMessage());
            }
      }

}
