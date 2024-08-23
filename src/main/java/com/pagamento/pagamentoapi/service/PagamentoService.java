package com.pagamento.pagamentoapi.service;

import com.pagamento.pagamentoapi.model.Pagamento;
import com.pagamento.pagamentoapi.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    public Pagamento criarPagamento(Pagamento pagamento) throws Exception {
        if ((pagamento.getMetodoPagamento().equalsIgnoreCase("cartao_credito") ||
                pagamento.getMetodoPagamento().equalsIgnoreCase("cartao_debito"))) {

            if (pagamento.getNumeroCartao() == null || pagamento.getNumeroCartao().isEmpty()) {
                throw new Exception("Número do cartão é obrigatório para pagamentos com cartão de crédito ou débito.");
            }
        } else {
            pagamento.setNumeroCartao(null);
        }

        pagamento.setStatus("Pendente de Processamento");

        return pagamentoRepository.save(pagamento);
    }


    public Pagamento atualizarStatus(Long id, String novoStatus) throws Exception {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new Exception("Pagamento não encontrado"));

        if ("Processado com Sucesso".equalsIgnoreCase(pagamento.getStatus())) {
            throw new Exception("Pagamento já processado com sucesso e não pode ser alterado.");
        }

        if ("Inativo".equalsIgnoreCase(pagamento.getStatus())) {
            if ("Pendente de Processamento".equalsIgnoreCase(novoStatus)) {
                pagamento.setStatus(novoStatus);
            } else {
                throw new Exception("Pagamentos inativos só podem voltar para Pendente de Processamento.");
            }
        }
        else if ("Processado com Falha".equalsIgnoreCase(pagamento.getStatus())) {
            if ("Pendente de Processamento".equalsIgnoreCase(novoStatus)) {
                pagamento.setStatus(novoStatus);
            } else {
                throw new Exception("Pagamentos com falha só podem voltar para Pendente de Processamento.");
            }
        }
        else if ("Pendente de Processamento".equalsIgnoreCase(pagamento.getStatus())) {
            if ("Processado com Sucesso".equalsIgnoreCase(novoStatus) || "Processado com Falha".equalsIgnoreCase(novoStatus)) {
                pagamento.setStatus(novoStatus);
            } else {
                throw new Exception("Status inválido para a alteração.");
            }
        }

        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listarPagamentos(Integer codigoDebito, String cpfCnpj, String status) {
        if (codigoDebito != null) {
            return pagamentoRepository.findByCodigoDebito(codigoDebito);
        } else if (cpfCnpj != null) {
            return pagamentoRepository.findByCpfCnpjPagador(cpfCnpj);
        } else if (status != null) {
            return pagamentoRepository.findByStatus(status);
        } else {
            return pagamentoRepository.findAll();
        }
    }

    public Pagamento excluirLogicamente(Long id) throws Exception {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new Exception("Pagamento não encontrado"));

        if ("Pendente de Processamento".equalsIgnoreCase(pagamento.getStatus())) {
            pagamento.setStatus("Inativo");
            return pagamentoRepository.save(pagamento);
        } else {
            throw new Exception("Apenas pagamentos pendentes podem ser excluídos");
        }
    }
}
