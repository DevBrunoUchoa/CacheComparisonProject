package br.com.cacheComparison.test;

import java.util.Random;

import br.com.cacheComparison.cache.CacheManager;
import br.com.cacheComparison.cache.FIFOCache;
import br.com.cacheComparison.cache.LRUCache;

import java.util.List;
import java.util.ArrayList;

public class CacheTest {
    public static void main(String[] args) {
        int[] tamanhosCache = {3, 5, 7, 10}; // Diferentes tamanhos de cache
        int numOperacoes = 20; // Número total de acessos

        for (int tamanho : tamanhosCache) {
            System.out.println("\n===== Testando cache com tamanho " + tamanho + " =====");

            CacheManager<String, String> lruCache = new LRUCache<>(tamanho);
            CacheManager<String, String> fifoCache = new FIFOCache<>(tamanho);

            System.out.println("\n🔹 Carga Sequencial:");
            testarCarga(lruCache, fifoCache, gerarCargaSequencial(numOperacoes));

            System.out.println("\n🔹 Carga Aleatória:");
            testarCarga(lruCache, fifoCache, gerarCargaAleatoria(numOperacoes));

            System.out.println("\n🔹 Carga com Hotspots:");
            testarCarga(lruCache, fifoCache, gerarCargaHotspots(numOperacoes));
        }
    }

    private static List<String> gerarCargaSequencial(int numOperacoes) {
        List<String> carga = new ArrayList<>();
        char letra = 'A';
        for (int i = 0; i < numOperacoes; i++) {
            carga.add(String.valueOf((char) (letra + (i % 7)))); // Cicla entre A-G
        }
        return carga;
    }

    private static List<String> gerarCargaAleatoria(int numOperacoes) {
        List<String> carga = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < numOperacoes; i++) {
            carga.add(String.valueOf((char) ('A' + rand.nextInt(7)))); // Letras de A a G
        }
        return carga;
    }

    private static List<String> gerarCargaHotspots(int numOperacoes) {
        List<String> carga = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < numOperacoes; i++) {
            if (rand.nextDouble() < 0.7) {
                carga.add("A"); // 70% das vezes acessa 'A' (hotspot)
            } else {
                carga.add(String.valueOf((char) ('B' + rand.nextInt(6)))); // Outros elementos
            }
        }
        return carga;
    }

    private static void testarCarga(CacheManager<String, String> lru, CacheManager<String, String> fifo, List<String> carga) {
        executarCarga("LRU", lru, carga);
        executarCarga("FIFO", fifo, carga);
    }

    private static void executarCarga(String nome, CacheManager<String, String> cache, List<String> carga) {
        for (String key : carga) {
            cache.get(key);  // Simula acesso
            cache.put(key, key);  // Insere no cache
        }

        System.out.println(nome + " - Hits: " + cache.getHitCount());
        System.out.println(nome + " - Misses: " + cache.getMissCount());
        System.out.println(nome + " - Replacements: " + cache.getReplacementCount());
        System.out.println(nome + " - Tempo Médio de Acesso: " + cache.getAverageAccessTime() + "ns");
        System.out.println(nome + " - Custo Total de Substituições: " + cache.getTotalReplacementCost());
        System.out.println(nome + " - Distribuição de Acessos: " + cache.getAccessDistribution());
        System.out.println("-----------------------------");
    }
}
