# springbatch-file-multiline
Processa vários arquivos de entrada com linhas com layout diferentes e gera vários arquivos com o mesmo layout.

# Layout do Arquivo de Entrada
O arquivo de entrada é formado pelos os seguintes tipos:

Tipo HEADER -->  contém o cnpj e nome do estabelecimento separado por #|

Tipo Detail --> contém o cnpj e os valores em reais dos meses de janeiro a dezembro separado por #|

Tipo FOOTER --> não contém nenhum informação, server para indicar o fim das informações de um estabelecimento

#Exemplo:<br>
HEADER#|1#|Estabelecimento 1<br>
DETAIL#|1#|100#|200#|300#|400#|500#|600#|700#|800#|900#|1000#|1100#|1200<br>
FOOTER<br>

