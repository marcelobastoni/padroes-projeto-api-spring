package one.digitalinnovation.gof.service.impl;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Fornecedor;
import one.digitalinnovation.gof.model.FornecedorRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.FornecedorService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link FornecedorService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 *
 * @author marcelobastoni
 */
@Service
public class FornecedorServiceImpl implements FornecedorService {

    // Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    // Strategy: Implementar os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<Fornecedor> buscarTodos() {
        // Buscar todos os Fornecedores.
        return fornecedorRepository.findAll();
    }

    @Override
    public Fornecedor buscarPorId(Long id) {
        // Buscar Fornecedor por ID.
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);
        return fornecedor.get();
    }

    @Override
    public void inserir(Fornecedor fornecedor) {
        salvarFornecedorComCep(fornecedor);
    }

    @Override
    public void atualizar(Long id, Fornecedor fornecedor) {
        // Buscar Fornecedor por ID, caso exista:
        Optional<Fornecedor> fornecedorBd = fornecedorRepository.findById(id);
        if (fornecedorBd.isPresent()) {
            salvarFornecedorComCep(fornecedor);
        }
    }

    @Override
    public void deletar(Long id) {
        // Deletar Fornecedor por ID.
        fornecedorRepository.deleteById(id);
    }

    private void salvarFornecedorComCep(Fornecedor fornecedor) {
        // Verificar se o Endereco do Fornecedor já existe (pelo CEP).
        String cep = fornecedor.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCEP e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        fornecedor.setEndereco(endereco);
        // Inserir Fornecedor, vinculando o Endereco (novo ou existente).
        fornecedorRepository.save(fornecedor);
    }

}
