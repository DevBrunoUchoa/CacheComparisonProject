package br.com.cacheComparison.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class FIFOCache<K, V> implements CacheManager<K, V> {
    private final int capacity;
    private final HashMap<K, V> map;
    private final Queue<K> queue;
    private int hitCount = 0;
    private int missCount = 0;
    private int replacementCount = 0;
    
    // Variáveis para tempo de acesso
    private long totalAccessTime = 0;
    private long totalOperations = 0;
    
    // Variável para custo de substituição
    private long totalReplacementCost = 0;
    private static final int COST_PER_REPLACEMENT = 1;
    
    // Mapa para a distribuição de acessos
    private Map<K, Integer> accessDistribution = new HashMap<>();
    
    public FIFOCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.queue = new LinkedList<>();
    }
    
    @Override
    public V get(K key) {
        long startTime = System.nanoTime();
        V value = map.get(key);
        long elapsed = System.nanoTime() - startTime;
        totalAccessTime += elapsed;
        totalOperations++;
        
        // Atualiza a distribuição de acessos
        accessDistribution.put(key, accessDistribution.getOrDefault(key, 0) + 1);
        
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
        V previous = null;
        
        if (map.containsKey(key)) {
            previous = map.get(key);
            map.put(key, value);
        } else {
            if (map.size() >= capacity) {
                K oldestKey = queue.poll();
                if (oldestKey != null) {
                    map.remove(oldestKey);
                    replacementCount++;
                    totalReplacementCost += COST_PER_REPLACEMENT;
                }
            }
            map.put(key, value);
            queue.offer(key);
        }
        
        long elapsed = System.nanoTime() - startTime;
        totalAccessTime += elapsed;
        totalOperations++;
        
        // Atualiza a distribuição de acessos para a operação de put
        accessDistribution.put(key, accessDistribution.getOrDefault(key, 0) + 1);
        
        return previous;
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
    
    @Override
    public String toString() {
        return map.toString();  // Exibe os elementos armazenados no cache
    }

}
