# Cenários operacionais para validação

Estes exemplos transformam o domínio e as regras de [02-modelo-do-dominio.md](02-modelo-do-dominio.md) em situações concretas da loja. Servem para revisar o comportamento esperado antes da implementação.

## 1. Entrada física digitada pelo operador

**Situação:** chegam produtos à loja e o operador registra a entrada pelo nome definido no catálogo e utilizado na Olist.

1. O operador digita o nome e o sistema afunila a lista de produtos existentes enquanto ele escreve.
2. Os resultados mostram nome, número da figura, SKU/código de barras e saldo/localização para distinguir itens parecidos.
3. Se o produto existir, o operador seleciona e o sistema carrega seus dados; produtos com saldo zero também aparecem para permitir reposição.
4. Se não houver correspondência, o operador inicia o cadastro usando o nome definido pela loja (igual ao da Olist) e informa o SKU único e demais dados de identificação.
5. O operador informa quantidade recebida. O sistema sugere posições livres, reutilizando espaços em caixas existentes antes de propor uma nova caixa; o operador pode ajustar essa alocação.
6. Depois de revisar e confirmar as posições sugeridas ou ajustadas, o operador confirma a entrada.

**Resultado esperado:** a entrada fica ligada ao produto correto do catálogo e às posições físicas confirmadas. Importar XML da nota do fornecedor não faz parte desse fluxo inicial.

## 2. Funko normal vendido online

**Situação:** o operador precisa retirar um Funko antes de emitir a NF no Olist.

1. O operador inicia uma separação pré-nota e busca pelo nome do produto.
2. O sistema mostra o SKU e a localização, por exemplo, caixa 12, posição 4.
3. O operador confirma a retirada; a posição fica marcada como em separação e não pode ser alocada a outra operação.
4. O operador emite a NF no Olist e importa o XML no sistema.
5. O sistema associa a linha do XML à separação e confirma a venda uma única vez pela chave da NF.
6. O operador confere, embala e envia o vídeo ao registro da venda.
7. O sistema registra a baixa definitiva e libera a posição para futura reposição. Se o `inventory-service` estiver indisponível, a venda fica confirmada com baixa pendente; a mensagem durável será processada quando o serviço voltar.

**Variação:** se a separação for cancelada antes da emissão da NF, o operador devolve o item à posição; o sistema registra a devolução da separação e reabre aquela posição.

## 3. Funko médio ou grande na embalagem de armazenagem

**Situação:** o operador precisa localizar um Funko médio ou grande, que será enviado na mesma embalagem em que está armazenado.

1. O sistema busca pelo nome e mostra SKU, saldo e o tipo correto de embalagem: caixa intermediária para Funko médio ou `SN` para Funko grande.
2. O operador confirma a embalagem associada ao produto; a leitura por QR está definida para Funko grande, e sua adoção para a caixa intermediária ainda está em aberto.
3. Ao concluir a venda, a embalagem em que o produto estava armazenado acompanha o item e é marcada como consumida.
4. A embalagem consumida não volta à lista de embalagens disponíveis.

**Resultado esperado:** a caixa intermediária e a caixa `SN` grande permanecem tipos distintos; nenhuma é reutilizada depois de acompanhar o produto vendido.

## 4. Venda presencial

**Situação:** o cliente compra no balcão e leva o produto.

1. O operador localiza o produto e registra a venda manualmente.
2. Se houver NF, o operador pode associar o documento fiscal à venda.
3. O sistema baixa o estoque e registra o usuário responsável.
4. Não é criada etiqueta de conferência nem vídeo de embalagem.

## 5. Conciliação de uma venda de marketplace

**Situação:** uma venda online tem ID próprio e o marketplace deposita o líquido dias depois.

1. Ao registrar a venda, o operador informa o ID do marketplace, valor bruto e líquido previsto; componentes como tarifa, frete ou ajuste são registrados quando disponíveis.
2. Depois, ao ver o crédito no extrato bancário, o operador lança manualmente data, valor e identificação do crédito.
3. O sistema encontra a venda pelo marketplace + ID e compara líquido previsto e valor recebido.
4. Se os valores coincidem, o operador conclui a conciliação.
5. Se diferirem, o sistema destaca a diferença para revisão, sem alterar os valores originais.

**Resultado esperado:** cada venda/ID e crédito são registrados uma vez e a conciliação fica rastreável. Se Financeiro estiver parado, a fila conserva o evento e cria o registro quando o serviço voltar.

**Falha repetida:** após retentativas limitadas, a mensagem vai para a dead-letter queue para revisão, com alerta. Não fica num ciclo infinito de falhas.

## 6. Devolução ou cancelamento

**Situação:** o cancelamento ou a devolução pode ocorrer em qualquer etapa.

1. O operador autorizado registra o tipo de ocorrência, etapa, motivo e usuário responsável.
2. A venda e os movimentos originais permanecem no histórico.
3. Se o produto retorna fisicamente, o operador avalia sua condição.
4. Só um item marcado como vendável volta ao saldo disponível; item danificado recebe outra destinação registrada.

**Ponto a validar:** quais motivos e destinos de produto são necessários na primeira versão.

## 7. Administração de acessos

**Situação:** o administrador habilita acesso financeiro para um usuário.

1. O administrador abre a tela de usuários e seleciona a pessoa.
2. Liga o acesso `FINANCEIRO` e salva.
3. O sistema registra quem alterou o acesso e quando.
4. O usuário passa a acessar as operações financeiras; os demais módulos continuam conforme suas próprias opções liga/desliga.

Novos módulos acrescentam um novo valor ao enum e uma opção à tela administrativa.

## Critérios transversais

- Nenhuma operação altera estoque sem registrar usuário e data/hora.
- Repetir a importação da mesma NF ou registrar o mesmo crédito não duplica venda, recebimento ou baixa.
- Erros são corrigidos por operações compensatórias auditáveis; registros históricos não são apagados.
- Todas as mudanças de estado ocorrem por ações de domínio, sem setters públicos.
