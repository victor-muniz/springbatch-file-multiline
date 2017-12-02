# springbatch-file-multiline
Processamento batch de vários arquivos de entrada com linhas com layout diferentes e gera vários arquivos com o mesmo layout.

# Layout do Arquivo de Entrada

O arquivo de entrada é formado pelos os seguintes tipos:

**Tipo HEADER** --> Contém o cnpj e nome do estabelecimento separado por **#|** <br>
**Tipo Detail** --> Contém o cnpj e os valores em reais dos meses de janeiro a dezembro separado por **#|** <br>
**Tipo FOOTER** --> Não contém nenhum informação, server para indicar o fim das informações de um estabelecimento <br>

# Exemplo

**HEADER**#|03.847.655/0001-98#|Padaria São Jorge<br>
**DETAIL**#|03.847.655/0001-98#|100#|200#|300#|400#|500#|600#|700#|800#|900#|1000#|1100#|1200<br>
**FOOTER**

