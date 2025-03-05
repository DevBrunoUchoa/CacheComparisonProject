import os
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from matplotlib.backends.backend_pdf import PdfPages

# Configurar o estilo dos gráficos
sns.set(style="whitegrid")

# Definir o caminho completo para o CSV !!!
csv_path = r"C:\Users\Math7\OneDrive\Área de Trabalho\CacheComparisonProject\cache_results.csv"
df = pd.read_csv(csv_path)
print(df.head())

# Lista de métricas que desejamos plotar
metrics = ['Hits', 'Misses', 'Replacements', 'AverageAccessTime', 'TotalReplacementCost']

# Obter os tipos de carga únicos (Sequencial, Aleatória, Hotspots)
cargas = df['Carga'].unique()

# Definir o caminho completo para salvar o PDF na pasta desejada
pdf_path = r"C:\Users\Math7\OneDrive\Área de Trabalho\CacheComparisonProject\src\br\com\cacheComparison\cache_plots.pdf"

with PdfPages(pdf_path) as pdf:
    # Para cada padrão de carga, plote cada métrica em função do tamanho do cache, diferenciando LRU e FIFO
    for carga in cargas:
        df_carga = df[df['Carga'] == carga]
        for metric in metrics:
            plt.figure(figsize=(10, 6))
            sns.lineplot(data=df_carga, x='CacheSize', y=metric, hue='CacheType', marker='o')
            plt.title(f'{metric} vs Cache Size ({carga} Load)')
            plt.xlabel('Cache Size')
            plt.ylabel(metric)
            plt.legend(title='CacheType')
            plt.tight_layout()
            pdf.savefig()  # Adiciona a figura atual ao PDF
            plt.close()

print(f"Gráficos salvos em {pdf_path}")
