package common.jcode.files.randomaccesSample.randomacces;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import common.jcode.files.randomaccesSample.pojos.Cake;

public class RandomAccessCakesManager extends AbstractRandomAccessFileManager<Cake>
{

      private static final String SEPARATOR = ",";
      public static final String PRICE = "PRICE";
      public static final String INGREDIENTS = "INGREDIENTS";
      public static final String DESCRIPTION = "DESCRIPTION";
      public static final String NAME = "NAME";
      private final File indexFile;
      private final Properties indexManager;
      public static Map<String,Integer> fieldMap = cakeMap();

      public RandomAccessCakesManager(File file,File indexFile)
      {
            super(fieldMap,file);
            this.indexFile = indexFile;
            indexManager = new Properties();
      }

      public boolean save(String index,Cake cake) throws IOException
      {
            indexManager.load(new FileReader(indexFile));
            var property = indexManager.getProperty(index,STRING_EMPTY);
            if (property.isBlank())
            {
                  throw new IOException("No existe ese registro");
            }
            return super.save(loadCakeData(cake),Long.parseLong(property));
      }

      @Override
      public Cake get(String index) throws IOException
      {
            indexManager.load(new FileReader(indexFile));
            var dataIndex = indexManager.getProperty(index,STRING_EMPTY);
            if (dataIndex.isBlank())
            {
                  throw new IOException("No existe ese registro");
            }
            return get(Integer.parseInt(dataIndex));
      }

      @Override
      public Cake get(int index) throws IOException
      {
            if (file.length() == 0)
            {
                  throw new IOException("No hay datos guardados");
            }
            try (var accessFile = new RandomAccessFile(file,RWS))
            {

                  var completeJump = index * recordLength;
                  if (completeJump > file.length())
                  {
                        throw new IOException("OutOfBounds");
                  }
                  accessFile.seek(completeJump);
                  var cake = new Cake();

                  fieldMap.entrySet()
                          .forEach(entryData-> buildCake(cake,entryData,accessFile));
                  return cake;
            }

      }

      private static Map<String,Integer> cakeMap()
      {
            var cakeMap = new HashMap<String,Integer>();
            cakeMap.put(DESCRIPTION,350);
            cakeMap.put(INGREDIENTS,200);
            cakeMap.put(PRICE,8);
            cakeMap.put(NAME,20);

            return cakeMap;
      }

      private void buildCake(Cake cake,Entry<String,Integer> entryData,RandomAccessFile accessFile)
      {
            var fieldLength = entryData.getValue();
            var bytes = new byte[fieldLength];
            try
            {
                  if (accessFile.read(bytes,0,fieldLength) == -1)
                  {
                        throw new IOException();
                  }
            }
            catch (IOException ex)
            {
                  System.out.println("Error al tratar de obtener el dato");
            }
            var cakeField = new String(bytes,Charset.defaultCharset()).strip();

            switch (entryData.getKey())
            {
                  case NAME:
                        cake.setCakeName(cakeField);
                        break;
                  case DESCRIPTION:
                        cake.setDescription(cakeField);
                        break;
                  case INGREDIENTS:
                        cake.setIngredients(Arrays.asList(cakeField.split(SEPARATOR)));
                        break;
                  case PRICE:
                        cake.setPrice(Float.parseFloat(cakeField));
                        break;
            }
      }

      public boolean save(Cake cake)
      {
            var record = loadCakeData(cake);
            var successfulInsert = super.save(record);
            if (successfulInsert)
            {
                  try
                  {
                        indexManager.load(new FileReader(indexFile));
                        successfulInsert = false;
                        indexManager.setProperty(record.get(NAME),rowsOfRecords - 1 + STRING_EMPTY);
                        indexManager.store(new FileWriter(indexFile),"Se ha guardado el registro ID-" + (rowsOfRecords - 1));
                  }
                  catch (IOException ex)
                  {
                        System.out.println("No se pudieron realizar los cambios");
                  }
            }
            return successfulInsert;
      }

      private HashMap<String,String> loadCakeData(Cake cake)
      {
            var ingredients = cake.getIngredients()
                                  .stream()
                                  .map(str-> str.concat(SEPARATOR))
                                  .reduce(STRING_EMPTY,String::concat);

            var record = new HashMap<String,String>();
            record.put(NAME,cake.getCakeName());
            record.put(DESCRIPTION,cake.getDescription());
            record.put(INGREDIENTS,ingredients);
            record.put(PRICE,cake.getPrice() + STRING_EMPTY);
            return record;
      }

}
