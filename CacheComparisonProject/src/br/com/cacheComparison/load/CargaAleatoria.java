package br.com.cacheComparison.load;

import br.com.cacheComparison.cache.CacheManager;
import java.util.Random;

public class CargaAleatoria implements CargaDeAcesso<String, String> {
    private final String[] chaves = {"A", "B", "C", "D", "E", "F", "G"};
    private final Random random = new Random();

    @Override
    public void executar(CacheManager<String, String> cache) {
        System.out.println("Executando carga aleatória...");
        
        for (int i = 0; i < 10; i++) {  // Executa 10 operações aleatórias
            String chave = chaves[random.nextInt(chaves.length)];
            cache.put(chave, "Valor_" + chave);
            cache.get(chave);
        }
    }
}
