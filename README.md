# A-star-vs-GraphPlan-on-PDDL-Problems

# Grupo

ANA PAULA BRUNO CARBONE - 9761805
BRUNO YUJI ISHIYAMA - 9779051
FERNANDO D’AVILA AXTHELM - 9778881

# Como executar o programa

Primeiramente importe o projeto em uma IDE como um "Existing Maven Projects", recomendamos o Eclipse.
Após as dependências serem carregadas na aplicação a utilização e configuração se torna simples.

A classe Application.java é onde será atribuido o Domínio e Problema PDDL, juntamente com o tipo de heurística a ser usada.

Os arquivos PDDL se encontram dentro da pasta resources/pddl_files, para os problemas e domínios defaults do Boxworld e Tyreworld e na pasta resources/pddl_tests para todos os problemas diferentes do BoxWorld.

O caminho relativo desses arquivos foi inserido como um Enum na Aplicação, logo alterar o valor deles se torna mais fácil, o mesmo foi feito com a definição das heurísticas.
