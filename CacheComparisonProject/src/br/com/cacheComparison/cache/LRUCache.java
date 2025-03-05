package br.com.cacheComparison.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LRUCache<K, V> extends LinkedHashMap<K, V> implements CacheManager<K, V> {
    private final int capacity;
    private int hitCount = 0;
    private int missCount = 0;
    private int replacementCount = 0;
    
    // Variáveis para tempo de acesso
    private long totalAccessTime = 0;
    private long totalOperations = 0;
    
    // Variável para custo de substituição
    private long totalReplacementCost = 0;
    private static final int COST_PER_REPLACEMENT = 1; // custo fixo por substituição
    
    // Mapa para a distribuição de acessos
    private Map<K, Integer> accessDistribution = new HashMap<>();
    
    public LRUCache(int capacity) {
        // Parâmetros: capacidade, fator de carga, ordenação por acesso
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }
    
    @Override
    public V get(Object key) {
        long startTime = System.nanoTime();
        V value = super.get(key);
        long elapsed = System.nanoTime() - startTime;
        totalAccessTime += elapsed;
        totalOperations++;
        
        // Atualiza a distribuição de acessos (cast seguro, pois o cache trabalha com chaves do tipo K)
        if (key != null) {
            K k = (K) key;
            accessDistribution.put(k, accessDistribution.getOrDefault(k, 0) + 1);
        }
        
        if (value != null) {
            hitCount++;
        } else {
            missCount++;
        }
        return value;
    }
    
    @Override
    public V put(K key, V value) {
        long startTime = System.nanoTime();
        V previous = super.put(key, value);
        long elapsed = System.nanoTime() - startTime;
        totalAccessTime += elapsed;
        totalOperations++;
        
        // Atualiza a distribuição de acessos para a operação de put
        accessDistribution.put(key, accessDistribution.getOrDefault(key, 0) + 1);
        
        // Se a chave não existia e o tamanho passou do limite, conta como substituição
        if (previous == null && size() > capacity) {
            replacementCount++;
        }
        return previous;
    }
    
    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        boolean remove = size() > capacity;
        if (remove) {
            replacementCount++;
            totalReplacementCost += COST_PER_REPLACEMENT;
        }
        return remove;
    }
    
    @Override
    public int getHitCount() {
        return hitCount;
    }
    
    @Override
    public int getMissCount() {
        return missCount;
    }
    
    @Override
    public int getReplacementCount() {
        return replacementCount;
    }
    
    @Override
    public double getAverageAccessTime() {
        if (totalOperations == 0) return 0;
        return (double) totalAccessTime / totalOperations;
    }
    
    @Override
    public long getTotalReplacementCost() {
        return totalReplacementCost;
    }
    
    @Override
    public Map<K, Integer> getAccessDistribution() {
        return accessDistribution;
    }
}
