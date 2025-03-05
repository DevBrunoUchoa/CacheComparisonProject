package br.com.cacheComparison.test;

import br.com.cacheComparison.cache.CacheManager;
import br.com.cacheComparison.cache.FIFOCache;
import br.com.cacheComparison.cache.LRUCache;
import br.com.cacheComparison.logger.DataLogger;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;

public class CacheBenchmark {

    public static void main(String[] args) {
        int[] tamanhosCache = {3, 5, 7, 10}; // Diferentes tamanhos de cache
        int numOperacoes = 20;              // Número de operações para o teste oficial
        int warmUpOperacoes = 5;            // Número de operações para a fase de warm-up

        DataLogger dataLogger = null;
        try {
            dataLogger = new DataLogger("cache_results.csv");
            // Para cada tamanho de cache, execute os testes com cada tipo de carga
            for (int tamanho : tamanhosCache) {
                System.out.println("\n===== Testando cache com tamanho " + tamanho + " =====");

                testarCargaComWarmUp("Sequencial", tamanho, numOperacoes, warmUpOperacoes, CacheTipo.SEQUENCIAL, dataLogger);
                testarCargaComWarmUp("Aleatória", tamanho, numOperacoes, warmUpOperacoes, CacheTipo.ALEATORIA, dataLogger);
                testarCargaComWarmUp("Hotspots", tamanho, numOperacoes, warmUpOperacoes, CacheTipo.HOTSPOTS, dataLogger);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (dataLogger != null) {
                try {
                    dataLogger.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Enum para facilitar a escolha do tipo de carga
    public enum CacheTipo {
        SEQUENCIAL, ALEATORIA, HOTSPOTS
    }

    // Executa o teste para cada carga em ambos os caches (LRU e FIFO) com fase de warm-up
    private static void testarCargaComWarmUp(String cargaDescricao, int tamanho, int numOperacoes, int warmUpOperacoes, CacheTipo tipo, DataLogger logger) throws IOException {
        System.out.println("\n🔹 Carga " + cargaDescricao + ":");

        // Teste para LRU
        CacheManager<String, String> lruCache = new LRUCache<>(tamanho);
        List<String> carga = gerarCarga(numOperacoes, tipo);
        // Fase de warm-up
        executarCarga(lruCache, gerarCarga(warmUpOperacoes, tipo), false);
        // Teste oficial
        executarCarga(lruCache, carga, true);
        System.out.println("LRU - " + resultadoCache(lruCache));
        // Registra os dados para LRU
        logger.log("LRU", cargaDescricao, tamanho, lruCache.getHitCount(), lruCache.getMissCount(), lruCache.getReplacementCount(), lruCache.getAverageAccessTime(), lruCache.getTotalReplacementCost());

        // Teste para FIFO
        CacheManager<String, String> fifoCache = new FIFOCache<>(tamanho);
        carga = gerarCarga(numOperacoes, tipo);
        // Fase de warm-up
        executarCarga(fifoCache, gerarCarga(warmUpOperacoes, tipo), false);
        // Teste oficial
        executarCarga(fifoCache, carga, true);
        System.out.println("FIFO - " + resultadoCache(fifoCache));
        // Registra os dados para FIFO
        logger.log("FIFO", cargaDescricao, tamanho, fifoCache.getHitCount(), fifoCache.getMissCount(), fifoCache.getReplacementCount(), fifoCache.getAverageAccessTime(), fifoCache.getTotalReplacementCost());
    }

    // Gera uma carga de acordo com o tipo solicitado
    private static List<String> gerarCarga(int numOperacoes, CacheTipo tipo) {
        List<String> carga = new ArrayList<>();
        Random rand = new Random();
        String[] chaves = {"A", "B", "C", "D", "E", "F", "G"};

        switch (tipo) {
            case SEQUENCIAL:
                char letra = 'A';
                for (int i = 0; i < numOperacoes; i++) {
                    // Cicla entre A-G
                    carga.add(String.valueOf((char) (letra + (i % 7))));
                }
                break;
            case ALEATORIA:
                for (int i = 0; i < numOperacoes; i++) {
                    carga.add(String.valueOf((char) ('A' + rand.nextInt(7))));
                }
                break;
            case HOTSPOTS:
                for (int i = 0; i < numOperacoes; i++) {
                    // 70% das vezes acessa 'A', 30% acessa outras chaves
                    if (rand.nextDouble() < 0.7) {
                        carga.add("A");
                    } else {
                        carga.add(String.valueOf((char) ('B' + rand.nextInt(6))));
                    }
                }
                break;
        }
        return carga;
    }

    // Executa a carga no cache (simula acessos e inserções)
    private static void executarCarga(CacheManager<String, String> cache, List<String> carga, boolean coletarMetricas) {
        for (String key : carga) {
            cache.get(key);  // Simula o acesso
            cache.put(key, "Valor_" + key);  // Insere/atualiza o cache
        }
    }

    // Formata os resultados do cache para impressão
    private static String resultadoCache(CacheManager<String, String> cache) {
        return "Hits: " + cache.getHitCount() +
               ", Misses: " + cache.getMissCount() +
               ", Replacements: " + cache.getReplacementCount() +
               ", Tempo Médio de Acesso: " + cache.getAverageAccessTime() + "ns" +
               ", Custo Total de Substituições: " + cache.getTotalReplacementCost() +
               ", Distribuição de Acessos: " + cache.getAccessDistribution() +
               ", Estado Final: " + cache.toString();
    }
}
