Automata-Driven Stemming

A técnica de stemming é muito utilizada na recuperação da informação, e tem como intúito remover os sufixos e prefixos das palavras.

Este repositório apresenta uma nova aboradagem, utilizando automatos deterministicos finitos, para os algoritmos de stemming dem 3 línguas diferentes: inglês, espanhol e português.

O principal intúito desse repositório é expor uma prova de conceito na qual é possível verificar o ganho de performance quando utilizamos os automatos.

Para cada versão do algoritmo em automato, é também apresentada a versão do estado-da-arte.

Para funcionar, é necessário um input em TXT para cada língua contendo as palavras para realização do stemming. Cada linha no documento deve corresponder a uma palavra.

Exemplo em pt_br:

planetas
cometas
nebulosas
aglomerados
estrelas
galáxias
fenômenos
originam
fora
atmosfera
....

Para realização dos testes de performance foi utilizado o dump da base de dados da Wikipedia (não está a este repositório).

Há também um artigo anexo explicando melhor o ganho de performance de cada algorítmo.