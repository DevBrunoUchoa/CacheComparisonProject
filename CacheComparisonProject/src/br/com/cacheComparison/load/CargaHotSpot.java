package br.com.cacheComparison.load;

import br.com.cacheComparison.cache.CacheManager;
import java.util.Random;

public class CargaHotSpot implements CargaDeAcesso<String, String> {
    private final String[] chaves = {"A", "B", "C", "D", "E", "F", "G"};
    private final Random random = new Random();

    @Override
    public void executar(CacheManager<String, String> cache) {
        System.out.println("Executando carga com hotspots...");
        for (int i = 0; i < 10; i++) {
            String chave;
            if (random.nextDouble() < 0.7) {  // 70% das vezes, acessamos A, B ou C
                chave = chaves[random.nextInt(3)];
            } else {  // 30% das vezes, acessamos qualquer outra chave
                chave = chaves[random.nextInt(chaves.length)];
            }
            cache.put(chave, "Valor_" + chave);
            cache.get(chave);
        }
    }
}
