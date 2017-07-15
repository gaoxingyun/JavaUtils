package com.xy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2017/6/28.
 */
public class JacksonCsvUtils {

    /**
     * csv file to Beans.
     */
    public static <T> List<T> csvFileToBean(String fileName, String fileEncoding,  Class<T> cls) throws IOException {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.TRIM_SPACES).enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);

        CsvSchema schema = mapper.schemaFor(cls);

        File csvFile = new File(fileName);
        Reader reader = new InputStreamReader(new FileInputStream(fileName), fileEncoding);
        MappingIterator<T> it = mapper.readerFor(cls).with(schema).readValues(reader);
        List<T> list = it.readAll();
        return list;
    }

    /**
     * csv string to Beans.
     */
    public static <T> List<T> csvStrToBean(String csvData, Class<T> cls) throws IOException {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.TRIM_SPACES).enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);

        CsvSchema schema = mapper.schemaFor(cls);

        MappingIterator<T> it = mapper.readerFor(cls).with(schema).readValues(csvData);
        List<T> list = it.readAll();
        return list;
    }
    
}
