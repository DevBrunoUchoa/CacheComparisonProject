package br.com.cacheComparison.load;

import br.com.cacheComparison.cache.CacheManager;

public class CargaSequencial implements CargaDeAcesso<String, String> {
    private final String[] chaves = {"A", "B", "C", "D", "E", "F", "G"};

    @Override
    public void executar(CacheManager<String, String> cache) {
    	System.out.println("Executando carga sequencial...");
    	
        for (String chave : chaves) {
            cache.put(chave, "Valor_" + chave);
            cache.get(chave);
        }
    }
}
