package common.jcode.files.randomaccesSample.randomacces;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractRandomAccessFileManager<T>
{

      private static final String UTF_8 = "UTF-8";
      protected static final String SUFFIX = "s";
      protected static final String PREFIX = "%-";
      protected static final String STRING_EMPTY = "";
      protected Map<String,Integer> fieldMap;
      protected static final String RWS = "rws";
      protected final File file;
      protected final long recordLength;
      protected long rowsOfRecords;

      public AbstractRandomAccessFileManager(Map<String,Integer> fieldMap,File file)
      {
            this.fieldMap = fieldMap;
            this.file = file;
            recordLength = size(fieldMap);
            rowsOfRecords = file.length() / recordLength;
      }

      public abstract T get(String index) throws IOException;

      public abstract T get(int index) throws IOException;

      public boolean save(Map<String,String> record)
      {
            var index = rowsOfRecords;
            return save(record,index);
      }

      public boolean save(Map<String,String> record,long index)
      {
            var successfulInsert = false;
            try (var randomAccess = new RandomAccessFile(file,RWS))
            {
                  randomAccess.seek(index * recordLength);

                  successfulInsert = fieldMap.entrySet()
                                             .stream()
                                             .flatMap(entry-> returnDataFormat(entry,record))
                                             .map(mappedEntry-> savedData(mappedEntry,randomAccess))
                                             .reduce(Boolean::logicalAnd)
                                             .get();
                  rowsOfRecords++;
            }
            catch (IOException ex)
            {
                  successfulInsert = false;
            }
            return successfulInsert;
      }

      private Stream<Entry<Integer,String>> returnDataFormat(Entry<String,Integer> entry,Map<String,String> record)
      {
            var recordFieldValue = record.get(entry.getKey());
            var fieldValue = Objects.isNull(recordFieldValue) ? STRING_EMPTY : recordFieldValue;
            var fieldLength = entry.getValue();
            var formattedValue = String.format(PREFIX + fieldLength + SUFFIX,fieldValue);

            return Map.of(fieldLength,formattedValue)
                      .entrySet()
                      .stream();
      }

      private boolean savedData(Entry<Integer,String> mappedEntry,RandomAccessFile randomAccess)
      {
            try
            {
                  var bytesToWrite = mappedEntry.getValue()
                                                .getBytes(UTF_8);
                  var length = mappedEntry.getKey();

                  randomAccess.write(bytesToWrite,0,length);
                  return true;
            }
            catch (IOException ex)
            {
                  return false;
            }
      }

      private Long size(Map<String,Integer> fieldMap)
      {
            return Objects.requireNonNull(fieldMap)
                          .entrySet()
                          .stream()
                          .map(entry-> (long)entry.getValue())
                          .reduce((long)0,Long::sum);
      }

      public Map<String,Integer> getFieldMap()
      {
            return fieldMap;
      }

      public void setFieldMap(Map<String,Integer> fieldMap)
      {
            this.fieldMap = Objects.requireNonNull(fieldMap);
      }

}
