package br.com.cacheComparison.logger;

import java.io.FileWriter;
import java.io.IOException;

public class DataLogger {
    private FileWriter writer;

    // Construtor: abre o arquivo e escreve o cabeçalho do CSV
    public DataLogger(String fileName) throws IOException {
        writer = new FileWriter(fileName);
        // Cabeçalho: CacheType,Carga,CacheSize,Hits,Misses,Replacements,AverageAccessTime,TotalReplacementCost
        writer.write("CacheType,Carga,CacheSize,Hits,Misses,Replacements,AverageAccessTime,TotalReplacementCost\n");
    }

    // Método para registrar os dados de cada teste
    public void log(String cacheType, String carga, int cacheSize, int hits, int misses, int replacements, double avgAccessTime, long totalReplacementCost) throws IOException {
        String line = cacheType + "," + carga + "," + cacheSize + "," + hits + "," + misses + "," + replacements + "," + avgAccessTime + "," + totalReplacementCost + "\n";
        writer.write(line);
    }

    // Fecha o arquivo
    public void close() throws IOException {
        writer.flush();
        writer.close();
    }
}
