import os
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from matplotlib.backends.backend_pdf import PdfPages

# Configurar o estilo dos gráficos
sns.set(style="whitegrid")

# Definir o caminho relativo para o CSV
script_dir = os.path.dirname(__file__)  # Diretório do script
csv_path = os.path.join(script_dir, '..', '..', '..', '..', '..', '..', 'cache_results.csv')  # Caminho relativo ao CSV
csv_path = os.path.abspath(csv_path)  # Converter para caminho absoluto
print(f"Caminho absoluto do CSV: {csv_path}")

# Ler o CSV
df = pd.read_csv(csv_path)
print(df.head())

# Lista de métricas que desejamos plotar
metrics = ['Hits', 'Misses', 'Replacements', 'AverageAccessTime', 'TotalReplacementCost']

# Obter os tipos de carga únicos (Sequencial, Aleatória, Hotspots)
cargas = df['Carga'].unique()

# Definir o caminho relativo para salvar o PDF
pdf_path = os.path.join(script_dir, 'cache_plots.pdf')  # Caminho relativo para o PDF
pdf_path = os.path.abspath(pdf_path)  # Converter para caminho absoluto
print(f"Caminho absoluto do PDF: {pdf_path}")

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
