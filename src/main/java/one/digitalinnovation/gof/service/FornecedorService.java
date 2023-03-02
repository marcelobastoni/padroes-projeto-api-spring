package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Fornecedor;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio do fornecedor.
 * Desta forma se necessário, podemos ter múltiplas implementações dessa mesma interface.
 *
 * @author marcelobastoni
 */

public interface FornecedorService {

    Iterable<Fornecedor> buscarTodos();

    Fornecedor buscarPorId(Long id);

    void inserir(Fornecedor fornecedor);

    void atualizar(Long id, Fornecedor fornecedor);

    void deletar(Long id);
}
