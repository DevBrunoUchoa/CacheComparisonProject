package br.com.cacheComparison.cache;

import java.util.Map;

public interface CacheManager<K, V> {
    V get(K key);
    V put(K key, V value);
    
    int getHitCount();
    int getMissCount();
    int getReplacementCount();
    
    // Nova métrica: Tempo médio de acesso (em nanossegundos)
    double getAverageAccessTime();
    
    // Nova métrica: Custo total de substituições
    long getTotalReplacementCost();
    
    // Nova métrica: Distribuição de acessos (quantas vezes cada chave foi acessada)
    Map<K, Integer> getAccessDistribution();
}