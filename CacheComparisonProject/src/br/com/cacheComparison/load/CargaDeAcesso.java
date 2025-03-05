package br.com.cacheComparison.load;

import br.com.cacheComparison.cache.CacheManager;

public interface CargaDeAcesso<K, V> {
	void executar(CacheManager<K, V> cache);
}